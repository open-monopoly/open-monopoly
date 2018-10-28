package fr.litarvan.monopoly

import fr.litarvan.monopoly.core.GameObject
import fr.litarvan.monopoly.core.GameScreen
import fr.litarvan.monopoly.core.act

class OMGameScreen(game: MonopolyGame) : GameScreen(game)
{
    private val board = GameObject("board")

    init
    {
        this += board

        cam.act {
            position.set(-15f, 10f, -15f)
            lookAt(15f, 0f, 15f)
        }
    }
}