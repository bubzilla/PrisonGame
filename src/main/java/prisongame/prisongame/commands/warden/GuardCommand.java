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

public class GuardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                Player g = Bukkit.getPlayer(args[0]);
                var profile = ProfileKt.getProfile(g);
                if (g.isOnline() && g != sender && profile.getRole() == Role.PRISONER) {
                    profile.setInvite(Role.GUARD);
                    sender.sendMessage(ChatColor.AQUA + "Succesfully asked player to be a guard!");
                    g.sendMessage(ChatColor.BLUE + "The wardens wants you to be a guard! use '/accept'");
                } else {
                    sender.sendMessage(ChatColor.BLUE + "We had troubles promoting this player.");
                }
            }
        }

        return true;
    }
}
