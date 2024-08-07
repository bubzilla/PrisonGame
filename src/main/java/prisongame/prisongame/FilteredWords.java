package prisongame.prisongame;

import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import prisongame.prisongame.config.filter.ContentMatch;
import prisongame.prisongame.config.filter.Filter;
import prisongame.prisongame.config.filter.FilterAction;
import prisongame.prisongame.discord.DiscordKt;

import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class FilteredWords {
    public static String filterMessage = "I FUCKING LOVE AMONG US!!! YESS!!! AMONGER!! SUSS!!! SUSSY!!! SUSSY BAKA!! SUSS!! WALTUH!! KINDA SUS WALTUH!!";

    private static String replaceConsecutiveDuplicates(String msg) {
        final Pattern pattern = Pattern.compile("(.)\\1*", Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(msg);

        StringBuilder sanitized = new StringBuilder();

        while (matcher.find())
            sanitized.append(matcher.group(1));

        return sanitized.toString();
    }

    public static void alert(Player violator, String message, String word, String context) {
        for (var player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("pbb.staff"))
                continue;

            sendAlert(Audience.audience(player), violator, message, word, context);
        }

        sendAlert(Audience.audience(Bukkit.getConsoleSender()), violator, message, word, context);

        var now = Instant.now();
        var embed = new EmbedBuilder();
        embed.addField("Time", String.format(
                "<t:%s> (<t:%s:R>)",
                now.getEpochSecond(),
                now.getEpochSecond()
        ), true);
        embed.setDescription(String.format(
                "**%s** was filtered due to using the word **%s** (context: **%s**). Their message is below.\n**%s**",
                violator.getName(),
                word,
                context,
                message
        ));
        embed.setColor(new Color(0xFF5555));

        var reportButton = Button.link(String.format(
                "https://support.minehut.com/hc/en-us/requests/new?ticket_form_id=5333154152723&tf_subject=%s&tf_%s=%s&tf_%s=%s&tf_%s=%s&tf_description=%s",
                "User+using+slurs+on+server",
                "5333187573779", // Report or Appeal dropdown
                "report_user",
                "360022850354", // Minehut Server Name,
                PrisonGame.brand,
                "13297886460307", // Minehut Server ID
                "652d32ec45e32f2364aab056",
                URLEncoder.encode(String.format(
                        """
                        User '%s' was using slurs on %s.
                        They have already been punished on %s.
                                                
                        Time: %s
                        """.trim(),
                        violator.getName(),
                        PrisonGame.brand,
                        PrisonGame.brand,
                        now,
                        now.toEpochMilli()), StandardCharsets.UTF_8)
        ), "Report User");

        var banButton = Button.danger(String.format("ban_%s", violator.getUniqueId()), "Ban (for slurs)");

        if (!getConfig().getDev())
            DiscordKt.filterChannel.sendMessageEmbeds(embed.build())
                    .addActionRow(reportButton, banButton)
                    .queue();
    }

    private static void sendAlert(Audience audience, Player violator, String message, String word, String context) {
        audience.sendMessage(PrisonGame.mm.deserialize(
                "<gray><player> was filtered due to using the word '<word>' (context: <context>). Their message is below.\n<message>",
                Placeholder.component("player", violator.name().color(NamedTextColor.WHITE)),
                Placeholder.component("word", Component
                        .text(word)
                        .color(NamedTextColor.WHITE)),
                Placeholder.component("context", Component
                        .text(context)
                        .color(NamedTextColor.WHITE)),
                Placeholder.component("message", Component
                        .text(message)
                        .color(NamedTextColor.WHITE))
        ));
    }

    public static @Nullable Filter takeActionIfNotClean(Player player, String message, String context) {
        var result = isClean(message);

        if (result != null) {
            var name = result.getFirst();
            var filter = result.getSecond();

            alert(player, message, name, context);
            return filter;
        }

        return null;
    }

    public static @Nullable Pair<String, Filter> isClean(String msg) {
        String sanitized = replaceConsecutiveDuplicates(msg
                .replaceAll("\\s+", ""));

        for (var entry : getConfig().getFilter().entrySet()) {
            var name = entry.getKey();
            var filter = entry.getValue();
            var input = filter.getContentMatch() == ContentMatch.ORIGINAL ? msg : sanitized;

            if (filter.test(input))
                return new Pair<>(name, filter);
        }

        return null;
    }
}
