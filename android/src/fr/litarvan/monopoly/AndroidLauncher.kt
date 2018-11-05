package fr.litarvan.monopoly

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val config = AndroidApplicationConfiguration()
        config.hideStatusBar = true
        config.numSamples = 2 // Antialias

        initialize(OpenMonopoly(), config)

        // Don't forget on the bottom : https://github.com/libgdx/libgdx/wiki/Managing-your-assets
    }
}
