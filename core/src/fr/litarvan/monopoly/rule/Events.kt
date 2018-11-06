package fr.litarvan.monopoly.rule

data class GameState(
        val players: Array<Player>,
        var playing: Int,

        var turn: Int = 0,
        var freePark: Int = 0,

        var rolled: Int = 0,
        var didDouble: Boolean = false,

        var waitingForBuy: Boolean = false
)

abstract class Event
{
    abstract fun apply(state: GameState)
}

class DiceRoll(val player: Int, val first: Int, val second: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.rolled++
        state.didDouble = first == second
    }
}

class TurnEnd(val player: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.turn += 1
        state.playing += 1

        if (state.playing >= state.players.size)
        {
            state.playing = 0
        }

        state.rolled = 0
        state.didDouble = false
    }
}

class PlayerMovement(val player: Int, val type: PlayerMovementType, val ofOrTo: Int): Event()
{
    override fun apply(state: GameState)
    {
        var pos = state.players[player].pos

        if (type == PlayerMovementType.MOVE) {
            pos += ofOrTo
        } else {
            pos = ofOrTo
        }

        if (pos >= 40) {
            pos -= 40
        }

        state.players[player].pos = pos
    }
}

class PlayerJailed(val player: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.players[player].jailed = true
        state.didDouble = false
    }
}

class PlayerUnjailed(val player: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.players[player].jailed = false
    }
}

class PlayerReceiveMoney(val player: Int, val amount: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.players[player].money += amount
    }
}

class PlayerReceiveFreePark(val player: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.players[player].money += state.freePark
        state.freePark = 0
    }
}

enum class PlayerMovementType
{
    MOVE,
    STRAIGHT
}