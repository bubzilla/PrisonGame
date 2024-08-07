package prisongame.prisongame.commands.warden;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;
import prisongame.prisongame.profile.ProfileKt;

public class PassCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Only players can execute this command."));
            return true;
        }

        if (player.getHealth() < 10) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>You cannot pass under 5 hearts."));
            return true;
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase(sender.getName())) {
                sender.sendMessage(ChatColor.RED + "You cannot pass to yourself.");
                return true;
            }

            Player g = Bukkit.getPlayer(args[0]);

            if (g == null || !g.isOnline()) {
                sender.sendMessage(ChatColor.BLUE + "We had troubles promoting this player.");
                return true;
            }

            var profile = ProfileKt.getProfile(g);
            profile.setRole(Role.WARDEN);
            sender.sendMessage(ChatColor.AQUA + "Succesfully asked player to be the warden!");
            g.sendMessage(ChatColor.RED + "The wardens wants you to be the warden! use '/accept'");
        }
        return true;
    }
}