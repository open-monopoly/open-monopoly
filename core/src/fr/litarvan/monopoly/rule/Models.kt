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

class Player(
        val name: String,
        val color: Color,
        var money: Int = 2000,
        var pos: Int = 0,
        var jailed: Boolean = false
)

class Property(
        val type: PropertyType,
        val family: Int,
        val case: Int,
        val name: String,
        val price: Int,
        val housePrice: Int,
        val mortgage: Int,
        val values: Array<Int>,

        @Json(ignored = true)
        var owner: Int = -1, // = Unowned

        @Json(ignored = true)
        var mortgaged: Boolean = false,

        @Json(ignored = true)
        var houses: Int = 0
)

enum class PropertyType {
    NORMAL,
    RAILROAD,
    COMPANY
}