package fr.litarvan.monopoly

import com.badlogic.gdx.Gdx.*
import com.badlogic.gdx.graphics.GL20
import fr.litarvan.monopoly.core.OMScreen
import ktx.app.KtxGame

class OpenMonopoly : KtxGame<OMScreen>()
{
    override fun create()
    {
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    fun load()
    {
        arrayOf(
            OMGameScreen(this)
        ).forEach { addScreen(it) }

        setScreen<OMGameScreen>()
    }

    override fun render()
    {
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT or if (graphics.bufferFormat.coverageSampling) GL20.GL_COVERAGE_BUFFER_BIT_NV else 0)

        super.render()
    }

    override fun resize(width: Int, height: Int)
    {
        gl.glViewport(0, 0, graphics.width, graphics.height)

        super.resize(width, height)
    }

    companion object
    {
        const val VERSION = "1.0.0"
    }
}