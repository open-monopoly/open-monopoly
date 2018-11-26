package fr.litarvan.monopoly.rule

import com.badlogic.gdx.graphics.Color
import com.beust.klaxon.Json
import fr.litarvan.monopoly.Assets
import java.util.*

private val rand = Random()

data class GameState(
        val players: Array<Player>,
        var playing: Int,

        var turn: Int = 0,
        var freeParking: Int = 0,

        var rolled: Int = 0,
        var didDouble: Boolean = false,

        var waitingForBuy: Boolean = false,

        var chanceCard: Int = rand.nextInt(Assets.board.chance.size),
        var communityChestCard: Int = rand.nextInt(Assets.board.communityChest.size)
)

data class Player(
        val name: String,
        val color: Color,
        var money: Int,
        var pos: Int,
        var jailed: Boolean = false
)

data class Board(
        val moneyName: String,
        val startMoney: Int,
        val startBonus: IntArray,
        val jailCaution: Int,
        val cases: Array<Case>,
        val chance: Array<Card>,
        val communityChest: Array<Card>
)

data class Case(
        val type: CaseType,
        val family: Int = -1,
        val case: Int,
        val name: String,
        val price: Int = -1,
        val housePrice: Int = -1,
        val mortgage: Int = -1,
        val values: IntArray = IntArray(0),

        @Json(ignored = true)
        var owner: Int = -1, // = Unowned

        @Json(ignored = true)
        var mortgaged: Boolean = false,

        @Json(ignored = true)
        var houses: Int = 0
)

enum class CaseType {
    PROPERTY,
    RAILROAD,
    COMPANY,
    CHANCE,
    COMMUNITY_CHEST,
    TAX,
    JAIL,
    GO_TO_JAIL,
    FREE_PARKING,
    START,
    CUSTOM
}

data class Card(
        val text: String,
        val type: CardType,
        val params: IntArray = IntArray(0)
)

enum class CardType {
    PAY,
    RECEIVE,
    MOVE,
    MOVE_STRAIGHT,
    MOVE_OF,
    MOVE_START,
    REPAIR,
    JAIL,
    FREE_JAIL,
    PAY_OR_CHANCE,
    BIRTHDAY
}