package prisongame.prisongame.profile.key

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType
import prisongame.prisongame.instance
import kotlin.reflect.KProperty

private val camelCase = Regex("([a-z]+|[A-Z][a-z]+)")

class Key<P, C : Any>(val type: PersistentDataType<P, C>) {
    private lateinit var key: NamespacedKey

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Key<P, C> {
        if (!this::key.isInitialized) {
            val name = camelCase.findAll(property.name)
                .joinToString("_") { it.value.lowercase() }
            key = NamespacedKey(instance, name)
        }

        return this
    }

    operator fun set(holder: PersistentDataHolder, value: C) {
        holder.persistentDataContainer.set(key, type, value)
    }

    operator fun get(holder: PersistentDataHolder): C {
        return holder.persistentDataContainer.get(key, type) as C
    }

    operator fun minusAssign(holder: PersistentDataHolder) {
        holder.persistentDataContainer.remove(key)
    }
}

operator fun <P, C : Any> PersistentDataHolder.set(key: Key<P, C>, value: C)  {
    key[this] = value
}

operator fun <P, C : Any> PersistentDataHolder.get(key: Key<P, C>): C {
    return key[this]
}

operator fun <P, C : Any> PersistentDataHolder.minusAssign(key: Key<P, C>) {
    key -= this
}