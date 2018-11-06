package fr.litarvan.monopoly.rule

class EventHistory(val base: GameState)
{
    private val history = mutableListOf<Event>()
    private var cache = base.copy()

    val events: Array<Event>
        get() = history.toTypedArray()

    val size: Int
        get() = history.size

    var state = cache.copy()
        private set

    fun emit(event: Event)
    {
        history += event
        //cache = build()
        event.apply(cache)
        state = cache.copy()
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