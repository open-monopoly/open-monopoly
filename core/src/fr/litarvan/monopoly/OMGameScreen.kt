package fr.litarvan.monopoly

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import fr.litarvan.monopoly.core.GameObject
import fr.litarvan.monopoly.core.GameScreen
import fr.litarvan.monopoly.core.act
import fr.litarvan.monopoly.rule.*

class OMGameScreen(game: OpenMonopoly) : GameScreen(game)
{
    private val board = GameObject("board")
    private val rules = MonopolyRules(arrayOf(Player("Litarvan", Color.BLUE), Player("Zbeub", Color.ORANGE)))
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
            position.set(12f, 5f, 12f)
            lookAt(-5f, -10f, -5f)

            /*position.set(0f, 20f, 0f)
            lookAt(0f, 0f, 0f)*/
        }

        update()
    }

    fun update()
    {
        val events = rules.poll()
        events.forEach {
            when (it) {
                is DiceRoll           -> println("Player {${it.player}} roll dices : ${it.first} & ${it.second}" + (if (it.first == it.second) " [DOUBLE]" else ""))
                is PlayerMovement     -> println("Player {${it.player}} move (${it.type}) of/to  ${it.ofOrTo}")
                is TurnEnd            -> println("Player {${it.player}} end turn")
                is PlayerReceiveMoney -> println("Player {${it.player}) receives ${it.amount}$")
                is PlayerJailed       -> println("Player {${it.player}} is JAILED !")
                is PlayerUnjailed     -> println("Player {${it.player}} is FREED from jail !")
                else                  -> println("Event : $it")
            }
        }

        val shift = Board.renderSize / 2f - Board.outerBorder - Board.caseHeight / 2f
        val pShift = 0.4f

        val step = { pos: Int -> Board.caseHeight / 2f + (Board.innerBorder + Board.caseWidth) * (pos % 10) - Board.caseWidth / 2f }

        rules.state.players.forEachIndexed { i, player ->
            var x = 0f
            var z = 0f

            if (player.pos < 10) {
                x = shift
                z = shift

                if (player.pos > 0) {
                    x -= step(player.pos)
                    z += Board.caseTop / 2
                }
            } else if (player.pos < 20) {
                x = -shift
                z = shift

                if (player.pos > 10) {
                    z -= step(player.pos)
                    x -= Board.caseTop / 2
                }
            } else if (player.pos < 30) {
                x = -shift
                z = -shift

                if (player.pos > 20) {
                    x += step(player.pos)
                    z -= Board.caseTop / 2
                }
            } else if (player.pos < 40) {
                x = shift
                z = -shift

                if (player.pos > 30) {
                    z += step(player.pos)
                    x += Board.caseTop / 2
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

    override fun render(delta: Float)
    {
        super.render(delta)

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            rules.roll()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            rules.endTurn()
        }

        if (rules.hasPoll) {
            update()
        }
    }
}