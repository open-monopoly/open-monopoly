package fr.litarvan.monopoly.rule

data class GameState(var turn: Int, var playing: Int, var rolled: Int, var didDouble: Boolean, val players: Array<Player>)

abstract class Event
{
    abstract fun apply(state: GameState)
}

class DiceRoll(val first: Int, val second: Int): Event()
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

        if (state.turn >= state.players.size)
        {
            state.turn = 0
        }

        state.rolled = 0
        state.didDouble = false
    }
}

class PlayerMovement(val player: Int, val type: PlayerMovementType, val to: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.players[player].pos += to
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

enum class PlayerMovementType
{
    MOVE,
    STRAIGHT
}