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
    private val boardSize = 20f

    private val board = GameObject("board")
    private val rules = MonopolyRules(arrayOf(Player("Litarvan", Color.BLUE), Player("Zbeub", Color.YELLOW)))
    private val players = mutableListOf<GameObject>()

    init
    {
        this += board

        val builder = ModelBuilder()
        var x = -(boardSize / 2f) + 1f - 0.75f

        rules.state.players.forEach {
            val obj = GameObject(builder.createBox(0.25f, 0.25f, 0.25f,
                    Material(ColorAttribute.createDiffuse(it.color)),
                    (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()))

            /*obj.x = x + 0.175f
            obj.z = -(boardSize / 2f) + 1f + 0.175f
            obj.y = 0.25f

            x += 1.5f*/
            players += obj
        }

        players.forEach { this += it }

        cam.act {
            position.set(-15f, 10f, -15f)
            lookAt(7.5f, -2.5f, 7.5f)
        }

        update()
    }

    fun update()
    {
        val edgeCenter = 1.175f

        val shift = -(boardSize / 2f) + edgeCenter
        val pShift = 0.45f

        val caseShift = (boardSize - (edgeCenter * 4f)) / 11f
        val caseCenter = caseShift / 2f

        rules.state.players.forEachIndexed { i, player ->
            var x = 0f
            var z = 0f

            if (player.pos < 10) {
                x = shift
                z = shift

                if (player.pos == 0) {
                    x += edgeCenter + caseCenter + (caseCenter * player.pos)
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