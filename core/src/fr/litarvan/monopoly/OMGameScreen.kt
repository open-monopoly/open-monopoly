package fr.litarvan.monopoly

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import fr.litarvan.monopoly.core.GameObject
import fr.litarvan.monopoly.core.GameScreen
import fr.litarvan.monopoly.core.act
import fr.litarvan.monopoly.rule.*

class OMGameScreen(game: OpenMonopoly) : GameScreen(game)
{
    private val board = GameObject("board")
    private val rules: MonopolyRules
    private val players = mutableListOf<GameObject>()
    private val shapeRenderer = ShapeRenderer()

    init
    {
        val startCase = Assets.board.cases.find { it.type == CaseType.START }!!.case
        val startMoney = Assets.board.startMoney

        rules = MonopolyRules(arrayOf(
                Player("Litarvan", Color.BLUE, startMoney, startCase),
                Player("Zbeub", Color.ORANGE, startMoney, startCase)
        ))

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
                is PlayerPayToFreeParking -> println("Player {${it.player}} pays ${it.amount}$ to free parking")
                is PlayerBuyCase -> println("Player {${it.player}) bought case ${it.case} (${rules.board.cases[it.case].name})")
                is PlayerReceiveFreeParking -> println("Player {${it.player}} receives ${it.amount}$ from free parking")
                is PlayerCanBuy -> println("Player {${it.player}} is being asked to buy case ${it.case} (${rules.board.cases[it.case].name})")
                is PlayerPayToBank -> println("Player {${it.player}} pays ${it.amount}$ to bank")
                is PlayerPayToPlayer -> println("Player {${it.player}} pays ${it.amount}$ to player {${it.to}}")
                is PlayerDontBuy -> println("Player {${it.player}) is not buying case ${it.case} (${rules.board.cases[it.case].name})")
                is PlayerPickChanceCard -> println("Player {${it.player}) picked Chance card : '${it.card}'")
                is PlayerPickCommunityChestCard -> println("Player {${it.player}) picked Community Chest card : '${it.card}'")
                else                  -> println("Event : $it")
            }
        }

        val shift = BoardInfos.renderSize / 2f - BoardInfos.outerBorder - BoardInfos.caseHeight / 2f
        val pShift = 0.4f

        val step = { pos: Int -> BoardInfos.caseHeight / 2f + (BoardInfos.innerBorder + BoardInfos.caseWidth) * (pos % 10) - BoardInfos.caseWidth / 2f }

        rules.state.players.forEachIndexed { i, player ->
            var x = 0f
            var z = 0f

            if (player.pos < 10) {
                x = shift
                z = shift

                if (player.pos > 0) {
                    x -= step(player.pos)
                    z += BoardInfos.caseTop / 2
                }
            } else if (player.pos < 20) {
                x = -shift
                z = shift

                if (player.pos > 10) {
                    z -= step(player.pos)
                    x -= BoardInfos.caseTop / 2
                }
            } else if (player.pos < 30) {
                x = -shift
                z = -shift

                if (player.pos > 20) {
                    x += step(player.pos)
                    z -= BoardInfos.caseTop / 2
                }
            } else if (player.pos < 40) {
                x = shift
                z = -shift

                if (player.pos > 30) {
                    z += step(player.pos)
                    x += BoardInfos.caseTop / 2
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            rules.buy()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            rules.endTurn()
        }

        if (rules.hasPoll) {
            update()
        }

        val width = 215f
        val height = 50f

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        rules.state.players.forEachIndexed { i, it ->
            var x = 0f
            var y = 0f

            shapeRenderer.color = it.color
            when (i) {
                0 -> { y = Gdx.graphics.height - height }
                1 -> { x = Gdx.graphics.width - width; y = Gdx.graphics.height - height }
                2 -> { x = Gdx.graphics.width - width }
            }

            shapeRenderer.rect(x, y, width, height)
        }

        shapeRenderer.end()
    }

    override fun dispose()
    {
        super.dispose()

        shapeRenderer.dispose()
    }
}