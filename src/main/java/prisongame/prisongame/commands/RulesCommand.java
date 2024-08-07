package prisongame.prisongame.commands;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.config.Config;

import java.util.ArrayList;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class RulesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>You aren't a player."));
            return true;
        }

        var config = getConfig().getGeneral();
        var invite = config.getDiscordInvite();
        var rules = config.getRules();
        var header = PrisonGame.mm.deserialize(
                "<color:#5865f2><brand> Rules</color>\n\n",
                Placeholder.component("brand", Component.text(PrisonGame.brand))
        );

        var book = Book.builder();
        var components = new ArrayList<Component>();

        // First page
        var firstPage = header;

        for (var i = 0; i < rules.size(); i++) {
            var rule = rules.get(i);

            firstPage = firstPage.append(displayRule(rule, i));
        }

        firstPage = firstPage.append(PrisonGame.mm.deserialize(
                "\n<#19911d>More info is available on the latter pages."));
        components.add(firstPage);

        // Latter pages
        for (var i = 0; i < rules.size(); i++) {
            var rule = rules.get(i);
            var pages = rule.getPages();
            var amount = pages.size();
            var component = header
                    .append(displayRule(rule, i))
                    .append(Component.newline());

            for (var pageIndex = 0; pageIndex < amount; pageIndex++) {
                var page = pages.get(pageIndex);
                component = component.append(page);

                if (amount > 1 && pageIndex < amount - 1) {
                    component = component.append(PrisonGame.mm.deserialize("\n  <#19911d>MORE ON NEXT PAGE"));
                    components.add(component);
                    component = header;
                }
            }

            components.add(component);
        }

        // Last page
        components.add(PrisonGame.mm.deserialize(
                String.format(
                        "We had to simplify the rules a bit for conciseness within this book. If you want, a more detailed version of the rules are available on our discord server.\n\n\n<pad_one><#8283cf><click:open_url:https://discord.gg/%s>CLICK TO JOIN<pad_two>DISCORD",
                        invite
                ),
                Placeholder.component("pad_one", Component.text(" ".repeat(5))),
                Placeholder.component("pad_two", Component.text(" ".repeat(16))
        )));

        player.openBook(book.pages(components).build());

        return true;
    }

    private Component displayRule(Config.General.Rule rule, int index) {
        return PrisonGame.mm.deserialize(
                String.format(
                        "<click:change_page:%s><b><index>.</b> <caption>\n",
                        index + 2
                ),
                Placeholder.component("index", Component.text(index + 1)),
                Placeholder.component("caption", rule.getName())
        );
    }
}
