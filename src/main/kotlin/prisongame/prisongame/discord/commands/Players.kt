
package prisongame.prisongame.discord.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import prisongame.prisongame.PrisonGame
import prisongame.prisongame.lib.Role
import prisongame.prisongame.profile.profile

fun players(event: SlashCommandInteractionEvent) {
    val warden = PrisonGame.warden
    val embed = EmbedBuilder()

    val players = Bukkit.getOnlinePlayers()
    val prisoners = players.filter { it.profile.role == Role.PRISONER }
    val guards = players.filter { it.profile.role != Role.WARDEN && it !in prisoners }

    val verb = if (players.size == 1) "is" else "are"
    val noun = if (players.size == 1) "player" else "players"
    embed.setTitle("There $verb ${players.size} $noun online.")

    if (warden != null)
        embed.setDescription("""
            **[WARDEN] ${warden.name}** [${warden.ping}ms]
            ${PrisonGame.wardentime[warden]!! / 20 / 60} minutes
        """.trimIndent())
    else embed.setDescription("No warden!")

    embed.addField(
        "Guards (${guards.size})",
        guards.joinToString("\n") {
            val display = PlainTextComponentSerializer.plainText().serialize(it.displayName())
            if (it.isDead) ":skull_crossbones: **${it.name}**"
            else "**$display** [${it.ping}ms]"
        },
        false)

    embed.addField(
        "Prisoners (${prisoners.size})",
        prisoners.joinToString("\n") {
            val display = PlainTextComponentSerializer.plainText().serialize(it.displayName())
            if (it.isDead) ":skull_crossbones: **${it.name}**"
            else "**$display** [${it.ping}ms]"
        },
        false)

    event.replyEmbeds(embed.build()).setEphemeral(true).queue();
}