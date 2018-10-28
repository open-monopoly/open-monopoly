package fr.litarvan.monopoly.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import fr.litarvan.monopoly.Assets
import fr.litarvan.monopoly.MonopolyGame

abstract class GameScreen(game: MonopolyGame) : OMScreen(game)
{
    protected val cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()).act {
        near = 1f
        far = 300f
    }

    protected val environment = Environment().apply {
        add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
        set(ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f))
    }

    private val worldBatch = ModelBatch()

    private var background = ModelInstance(Assets.model("background"))
    private val backgroundBatch = ModelBatch()
    private val backgroundEnvironment = Environment().apply {
        set(ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f))
    }

    private val objects = arrayListOf<GameObject>()

    fun add(obj: GameObject)
    {
        objects += obj
    }

    inline operator fun plusAssign(obj: GameObject)
    {
        add(obj)
    }

    override fun render(delta: Float)
    {
        worldBatch.begin(cam)
        objects.forEach { worldBatch.render(it, environment) }
        worldBatch.end()

        backgroundBatch.begin(cam)
        backgroundBatch.render(background, backgroundEnvironment)
        backgroundBatch.end()
    }

    override fun resize(width: Int, height: Int)
    {
        cam.act {
            viewportWidth = width.toFloat()
            viewportHeight = height.toFloat()
        }
    }

    override fun dispose()
    {
        worldBatch.dispose()
        backgroundBatch.dispose()
    }
}