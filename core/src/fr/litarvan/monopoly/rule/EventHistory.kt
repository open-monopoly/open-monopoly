package fr.litarvan.monopoly.rule

class EventHistory(val base: GameState)
{
    private val history = mutableListOf<Event>()
    private var cache = base.copy()

    val events: Array<Event>
        get() = history.toTypedArray()

    val state: GameState
        get() = cache.copy()

    val size: Int
        get() = history.size

    fun emit(event: Event)
    {
        history += event
        cache = build()
    }

    fun build(): GameState
    {
        val state = base.copy()
        history.forEach { it.apply(state) }

        return state
    }

    operator fun plusAssign(event: Event)
    {
        emit(event)
    }

    operator fun get(index: Int): Event
    {
        return history[index]
    }
}