package fr.litarvan.monopoly

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.utils.Array
import fr.litarvan.monopoly.rule.Board

object Assets
{
    private val assets = mapOf(
        // Models
        "background"    to "model:background.g3db",
        "board"         to "model:Board.g3db"
    )

    private val loader = AssetManager()

    fun model(name: String): Model = asset(name)
    private inline fun <T> asset(name: String): T = loader[path(name)]

    val progress get() = loader.progress
    val finished get() = loader.update()

    fun load()
    {
        loader.setLoader(Board::class.java, JsonLoader<Board>(InternalFileHandleResolver()))

        assets.forEach {
            loader.load(path(it.key), when (it.value.substring(0, it.value.indexOf(':'))) {
                "texture" -> Texture::class
                "model" -> Model::class
                else -> Unit::class
            }.java)
        }
    }

    private fun path(asset: String): String {
        val split = assets[asset]!!.split(':')
        return "${split[0]}s/${split[1]}"
    }

    fun dispose()
    {
        loader.dispose()
    }
}

class JsonLoader<T>(val resolver: FileHandleResolver) : AssetLoader<T, AssetLoaderParameters<T>>(resolver)
{
    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: AssetLoaderParameters<T>?): Array<AssetDescriptor<Any>>
    {
        AssetDescriptor
        resolver.resolve(fileName).readString()
    }
}