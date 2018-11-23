package fr.litarvan.monopoly.rule

import java.util.*
import fr.litarvan.monopoly.Assets.board
import fr.litarvan.monopoly.rule.CaseType.*

class MonopolyRules(players: Array<Player>)
{
    private val random = Random()
    private val history = EventHistory(GameState(players, random.nextInt(players.size)))
    private var pollIndex = 0

    val state: GameState
        get() = history.state
    val player: Player
        get() = state.players[state.playing]
    val hasPoll: Boolean
        get() = history.size - pollIndex > 0
    val canEnd: Boolean
        get() = state.rolled != 0 && !state.didDouble && !state.waitingForBuy && player.money >= 0

    fun roll()
    {
        val state = history.state

        if (state.rolled > 0 && !state.didDouble) {
            return
        }

        val first = random.nextInt(6) + 1
        val second = random.nextInt(6) + 1

        history += DiceRoll(state.playing, first, second)

        if (player.jailed) {
            if (first != second) {
                return
            }

            history += PlayerUnjailed(state.playing)
        } else if (first == second && state.rolled == 3) {
            history += PlayerJailed(state.playing)
            return
        }

        move(PlayerMovementType.MOVE, first + second)

        val pos = state.players[state.playing].pos
        val case = board.cases[pos]

        if (state.playing == case.owner) {
            return
        }

        when (case.type) {
            PROPERTY, RAILROAD, COMPANY -> {
                if (case.owner == -1) {
                    if (case.price <= player.money) {
                        history += PlayerCanBuy(state.playing, case.case)
                    }
                } else {
                    val value: Int = when (case.type) {
                        PROPERTY -> {
                            var v = case.values[case.houses]
                            val completeFamily = board.cases.filter { it.family == case.family }.find { it.owner != case.owner } == null

                            if (completeFamily) {
                                v *= 2
                            }

                            v
                        }
                        RAILROAD -> case.values[board.cases.filter { it.type == RAILROAD && it.owner == case.owner }.size - 1]
                        COMPANY -> case.values[
                            if (board.cases.find { it.type == COMPANY && it.owner == case.owner } == null) 0 else 1
                        ] * (first + second)
                        else -> 0 // Can't happen
                    }

                    history += PlayerPayToPlayer(state.playing, case.owner, value)
                }
            }
            CHANCE -> {
                // TODO: Chance & community chests
            }
            COMMUNITY_CHEST -> {

            }
            TAX -> {
                history += PlayerPayToFreeParking(state.playing, case.price)
            }
            JAIL -> {
                // ... Visit
            }
            GO_TO_JAIL -> {
                move(PlayerMovementType.STRAIGHT, 10)
                history += PlayerJailed(state.playing)
            }
            FREE_PARKING -> {
                history += PlayerReceiveFreeParking(state.playing)
            }
            START -> {
                history += PlayerReceiveMoney(state.playing, board.startBonus[1])
            }
            CUSTOM -> {
                // TODO: Mod engine ?
            }
        }
    }

    fun buy()
    {
        if (!state.waitingForBuy) {
            return
        }

        val case = board.cases.find { it.case == player.pos }!!

        if (case.owner != -1) {
            return
        }

        history += PlayerPayToBank(state.playing, case.price)
        history += PlayerBuyCase(state.playing, player.pos)
    }

    fun dontBuy()
    {
        if (!state.waitingForBuy) {
            return
        }

        history += PlayerDontBuy(state.playing, player.pos)
    }

    fun buyHouses(case: Int, amount: Int)
    {
        // TODO: "House line" mechanic !
    }

    fun payCaution()
    {
        if (!player.jailed) {
            return
        }

        history += PlayerPayToFreeParking(state.playing, board.jailCaution) // TODO: Move caution to json
        history += PlayerUnjailed(state.playing)
    }

    fun endTurn()
    {
        if (!canEnd) {
            return
        }

        history += TurnEnd(state.playing)
    }

    fun poll(): Array<Event>
    {
        val array = arrayOfNulls<Event>(history.size - pollIndex)
        array.forEachIndexed { i, _ -> array[i] = history[pollIndex + i] }

        pollIndex = history.size

        return array.requireNoNulls()
    }

    private fun move(type: PlayerMovementType, ofOrTo: Int)
    {
        val oldPos = player.pos
        val startCase = board.cases.find { it.type == START }!!.case // TODO: Handle error

        history += PlayerMovement(state.playing, type, ofOrTo)

        if (type == PlayerMovementType.MOVE) {
            val pos = player.pos
            val throughStart = when {
                pos > oldPos -> startCase in (oldPos + 1)..(pos - 1)
                else -> startCase > oldPos || startCase < pos
            } // TODO: Should work, but further checking is needed

            if (throughStart) {
                println("Through start: pos: $pos, old pos: $oldPos, startCase: $startCase" )
                history += PlayerReceiveMoney(state.playing, board.startBonus[0])
            }
        }
    }
}