package fr.litarvan.monopoly.core

import com.badlogic.gdx.graphics.PerspectiveCamera

fun PerspectiveCamera.act(block: PerspectiveCamera.() -> Unit): PerspectiveCamera {
    block()
    this.update()

    return this
}