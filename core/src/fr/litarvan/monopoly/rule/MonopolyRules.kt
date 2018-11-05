package fr.litarvan.monopoly.rule

import java.util.*

class MonopolyRules(players: Array<Player>)
{
    private val random = Random()
    private val history = EventHistory(GameState(0, random.nextInt(players.size), 0, false, players))
    private var pollIndex = 0

    val state: GameState
        get() = history.state
    val player: Player
        get() = state.players[state.playing]

    fun roll()
    {
        if (state.rolled > 0 && !state.didDouble) {
            return
        }

        val first = random.nextInt(6) + 1
        val second = random.nextInt(6) + 1

        history += DiceRoll(first, second)

        if (player.jailed) {
            if (first != second) {
                return
            }

            history += PlayerUnjailed(state.playing)
        }

        history += PlayerMovement(state.playing, PlayerMovementType.MOVE, first + second)

        act()
    }

    fun act()
    {

    }

    fun endTurn()
    {
        if (history.state.rolled == 0 || history.state.didDouble)
        {
            return
        }

        history += TurnEnd(state.playing)
    }

    fun poll(): Array<Event>
    {
        val array = arrayOfNulls<Event>(history.size - pollIndex - 1)
        array.forEachIndexed { i, _ -> array[i] = history[pollIndex + i] }

        pollIndex = history.size -  1

        return array.requireNoNulls()
    }
}