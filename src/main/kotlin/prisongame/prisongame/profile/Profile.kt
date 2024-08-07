package prisongame.prisongame.profile

import org.bukkit.entity.Player
import prisongame.prisongame.keys.Keys
import prisongame.prisongame.lib.Role
import kotlin.random.Random

private val profiles = mutableMapOf<Player, Profile>()

class Profile(val player: Player) {
    // Status
    var role = Role.PRISONER
    var escaped = false
    var invite: Role? = null
    var lastMessage: String? = null
    var prisonerNumber = Random.nextInt(100, 999)
    var startOfDayMoney: Double = Keys.MONEY.get(player, 0.0)
    var solitaryTimer = 0

    // Effects
    var strength = 0.0
    var speed = 0.0
}

val Player.profile
    get() = profiles.computeIfAbsent(this) { Profile(this) }