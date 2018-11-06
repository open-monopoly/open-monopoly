package fr.litarvan.monopoly.rule

import java.util.*

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
        } else {
            move(PlayerMovementType.MOVE, first + second)
        }

        val pos = state.players[state.playing].pos

        when(pos) {
            // START
            0 -> {
                history += PlayerReceiveMoney(state.playing, 400)
            }

            // TAXES
            4 -> {
                history += PlayerPayToBank(state.playing, 200)
            }

            // JAIL
            10 -> {}

            // FREE PARK
            20 -> {
                history += PlayerReceiveFreePark(state.playing)
            }

            // GO TO JAIL
            30 -> {
                move(PlayerMovementType.STRAIGHT, 10)
                history += PlayerJailed(state.playing)
            }

            // TAX
            38 -> {
                history += PlayerPayToBank(state.playing, 100)
            }

            // LUCK & COMMUNITY
            7, 22, 36 -> {

            }

            2, 17, 33 -> {

            }

            else -> {
                // PROPERTY
            }
        }
    }

    private fun move(type: PlayerMovementType, ofOrTo: Int)
    {
        history += PlayerMovement(state.playing, type, ofOrTo)

        if (type == PlayerMovementType.MOVE) {
            val pos = state.players[state.playing].pos

            if (pos != 0 && ofOrTo > pos) {
                history += PlayerReceiveMoney(state.playing, 200)
            }
        }
    }

    fun endTurn()
    {
        if (!canEnd)
        {
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
}