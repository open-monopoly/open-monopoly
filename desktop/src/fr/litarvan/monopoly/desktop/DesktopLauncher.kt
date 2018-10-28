package fr.litarvan.monopoly.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import fr.litarvan.monopoly.MonopolyGame

object DesktopLauncher
{
    @JvmStatic
    fun main(arg: Array<String>)
    {
        val config = LwjglApplicationConfiguration()
        config.title = "OpenMonopoly ${MonopolyGame.VERSION}"
        config.width = 1000
        config.height = 750
        config.resizable = true
        config.samples = 4 // Antialias

        LwjglApplication(MonopolyGame(), config)
    }
}
