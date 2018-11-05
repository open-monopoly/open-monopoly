package fr.litarvan.monopoly

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import fr.litarvan.monopoly.core.GameObject
import fr.litarvan.monopoly.core.GameScreen
import fr.litarvan.monopoly.core.act
import fr.litarvan.monopoly.rule.MonopolyRules
import fr.litarvan.monopoly.rule.Player

class OMGameScreen(game: OpenMonopoly) : GameScreen(game)
{
    private val board = GameObject("board")
    private val rules = MonopolyRules(arrayOf(Player("Litarvan", Color.BLUE), Player("Zbeub", Color.YELLOW)))
    private val players = mutableListOf<GameObject>()

    init
    {
        this += board

        val builder = ModelBuilder()

        rules.state.players.forEach {
            val obj = GameObject(builder.createBox(0.25f, 0.25f, 0.25f,
                    Material(ColorAttribute.createDiffuse(it.color)),
                    (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()))

            players += obj
        }

        players.forEach { this += it }

        cam.act {
            position.set(-15f, 10f, -15f)
            lookAt(7.5f, -2.5f, 7.5f)

            /*position.set(0f, 20f, 0f)
            lookAt(0f, 0f, 0f)*/
        }

        update()
    }

    fun update()
    {
        val shift = Board.renderSize / 2f - Board.outerBorder - Board.caseHeight / 2f
        val pShift = 0.4f

        val step = { pos: Int -> Board.caseHeight / 2f + (Board.innerBorder + Board.caseWidth) * (pos % 10) - Board.caseWidth / 2f }

        rules.state.players.forEachIndexed { i, player ->
            var x = 0f
            var z = 0f

            if (player.pos < 10) {
                x = -shift
                z = -shift

                if (player.pos > 0) {
                    x += step(player.pos)
                    z -= Board.caseTop / 2
                }
            } else if (player.pos < 20) {
                x = shift
                z = -shift

                if (player.pos > 10) {
                    z += step(player.pos)
                    x += Board.caseTop / 2
                }
            } else if (player.pos < 30) {
                x = shift
                z = shift

                if (player.pos > 20) {
                    x -= step(player.pos)
                    z += Board.caseTop / 2
                }
            } else if (player.pos < 40) {
                x = -shift
                z = shift

                if (player.pos > 30) {
                    z -= step(player.pos)
                    x -= Board.caseTop / 2
                }
            }

            when (i) {
                0 -> {
                    x += pShift
                    z += pShift
                }
                1 -> {
                    x -= pShift
                    z += pShift
                }
                2 -> {
                    x -= pShift
                    z -+ pShift
                }
                3 -> {
                    x += pShift
                    z -= pShift
                }
            }

            players[i].x = x
            players[i].z = z
            players[i].y = 0.25f
        }
    }
}