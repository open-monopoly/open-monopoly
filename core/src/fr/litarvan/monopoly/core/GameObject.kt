package fr.litarvan.monopoly.core

import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.RenderableProvider
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import fr.litarvan.monopoly.Assets

class GameObject(model: String) : RenderableProvider
{
    private val model = ModelInstance(Assets.model(model))

    var x: Float
        get() = model.transform.`val`[Matrix4.M03]
        set(v) { model.transform.`val`[Matrix4.M03] = v }

    var y: Float
        get() = model.transform.`val`[Matrix4.M13]
        set(v) { model.transform.`val`[Matrix4.M13] = v }

    var z: Float
        get() = model.transform.`val`[Matrix4.M23]
        set(v) { model.transform.`val`[Matrix4.M23] = v }

    override fun getRenderables(renderables: Array<Renderable>?, pool: Pool<Renderable>?)
    {
        model.getRenderables(renderables, pool)
    }
}
