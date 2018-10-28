package fr.litarvan.monopoly

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import fr.litarvan.monopoly.core.OMScreen

class LoadingScreen(game: MonopolyGame) : OMScreen(game)
{
    private val renderer = ShapeRenderer()

    init
    {
        Assets.load()
    }

    override fun render(delta: Float)
    {
        super.render(delta)

        val width = Gdx.graphics.width

        renderer.begin(ShapeRenderer.ShapeType.Filled)

        renderer.color = Color.GRAY
        renderer.rect(100f, 150f, width - 200f, 35f)

        renderer.color = Color.WHITE
        renderer.rect(100f, 150f, (width - 200f) * Assets.progress, 35f)

        renderer.end()

        if (Assets.finished) {
            game.load()
        }
    }
}