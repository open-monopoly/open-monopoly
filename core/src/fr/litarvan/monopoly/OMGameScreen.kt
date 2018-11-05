package fr.litarvan.monopoly

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import fr.litarvan.monopoly.core.GameObject
import fr.litarvan.monopoly.core.GameScreen
import fr.litarvan.monopoly.core.act
import fr.litarvan.monopoly.rule.Player

class OMGameScreen(game: OpenMonopoly) : GameScreen(game)
{
    private val boardSize = 20f

    private val board = GameObject("board")
    private val p = arrayOf(Player("Litarvan", Color.BLUE), Player("Zbeub", Color.YELLOW))
    private val players = mutableListOf<GameObject>()

    init
    {
        this += board

        val builder = ModelBuilder()
        var x = -(boardSize / 2f) + 1f - 0.75f

        p.forEach {
            val obj = GameObject(builder.createBox(0.25f, 0.25f, 0.25f,
                    Material(ColorAttribute.createDiffuse(it.color)),
                    (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()))

            obj.x = x + 0.175f
            obj.z = -(boardSize / 2f) + 1f + 0.175f
            obj.y = 0.25f

            x += 1.5f
            players += obj
        }

        players.forEach { this += it }

        cam.act {
            position.set(-15f, 10f, -15f)
            lookAt(7.5f, -2.5f, 7.5f)
        }
    }
}