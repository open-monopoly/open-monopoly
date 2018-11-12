package fr.litarvan.monopoly.rule

import com.badlogic.gdx.graphics.Color
import com.beust.klaxon.Json

data class GameState(
        val players: Array<Player>,
        var playing: Int,

        var turn: Int = 0,
        var freeParking: Int = 0,

        var rolled: Int = 0,
        var didDouble: Boolean = false,

        var waitingForBuy: Boolean = false
)

data class Player(
        val name: String,
        val color: Color,
        var money: Int = 2000,
        var pos: Int = 0,
        var jailed: Boolean = false
)

data class Board(
        val moneyName: String,
        val startMoney: Int,
        val startBonus: Int,
        val cases: Array<Case>
)

data class Case(
        val type: CaseType,
        val family: Int = -1,
        val case: Int,
        val name: String,
        val price: Int = -1,
        val housePrice: Int = -1,
        val mortgage: Int = -1,
        val values: Array<Int> = emptyArray(),

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
    FREE_PARK,
    START,
    CUSTOM
}