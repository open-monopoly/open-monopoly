package fr.litarvan.monopoly.rule

import fr.litarvan.monopoly.Assets

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

class PlayerReceiveFreeParking(val player: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.players[player].money += state.freeParking
        state.freeParking = 0
    }
}

class PlayerPayToBank(val player: Int, val amount: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.players[player].money -= amount
    }
}

class PlayerPayToFreeParking(val player: Int, val amount: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.players[player].money -= amount
        state.freeParking += amount
    }
}

class PlayerPayToPlayer(val player: Int, val to: Int, val amount: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.players[player].money -= amount
        state.players[to].money += amount
    }
}

class PlayerCanBuy(val player: Int, val case: Int): Event()
{
    override fun apply(state: GameState)
    {
        state.waitingForBuy = true
    }
}

class PlayerBuyCase(val player: Int, val case: Int): Event()
{
    override fun apply(state: GameState)
    {
        val c = Assets.board.cases.find { it.case == case }!!
        c.owner = player

        state.waitingForBuy = false
    }
}

enum class PlayerMovementType
{
    MOVE,
    STRAIGHT
}