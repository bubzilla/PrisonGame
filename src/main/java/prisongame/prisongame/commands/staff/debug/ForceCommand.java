package prisongame.prisongame.commands.staff.debug;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;
import prisongame.prisongame.profile.ProfileKt;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ForceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a player."));
            return true;
        }

        var takeAction = false;
        var list = Arrays.stream(args).collect(Collectors.toList());
        if (list.contains("--action") || list.contains("-a")) {
            list.remove("--action");
            list.remove("-a");
            takeAction = true;
        }

        var player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>That player isn't online."));
            return true;
        }

        if (args.length == 1) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Please provide a role."));
            return true;
        }

        try {
            var profile = ProfileKt.getProfile(player);
            var role = Role.valueOf(args[1].toUpperCase());
            profile.setRole(role);

            if (takeAction)
                switch (role) {
                    case WARDEN -> {
                        if (PrisonGame.warden != null)
                            MyListener.playerJoin(PrisonGame.warden, false);

                        player.performCommand("warden");
                    }
                    case SWAT -> PrisonGame.setSwat(player);
                    case GUARD -> PrisonGame.setGuard(player);
                    case NURSE -> PrisonGame.setNurse(player);
                }

            sender.sendMessage(PrisonGame.mm.deserialize(
                    "<gray>Forced <white><player></white> to be <white><role></white>.",
                    Placeholder.component("player", player.name()),
                    Placeholder.component("role", Component.text(role.name()))
            ));
        } catch (IllegalArgumentException exception) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Invalid role."));
        }

        return true;
    }
}
