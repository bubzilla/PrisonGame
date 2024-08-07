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

public class SwatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (PrisonGame.swat) {
            if (args.length >= 1) {
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player g = Bukkit.getPlayer(args[0]);
                    if (g.isOnline() && g != sender) {
                        var profile = ProfileKt.getProfile(g);
                        profile.setInvite(Role.SWAT);
                        sender.sendMessage(ChatColor.DARK_GRAY + "Succesfully asked player to be a SWAT guard!");
                        g.sendMessage(ChatColor.DARK_GRAY + "The wardens wants you to be a SWAT guard! use '/accept'");
                    } else {
                        sender.sendMessage(ChatColor.BLUE + "We had troubles promoting this player. If they're a guard/nurse, demote them, then promote them  to swat again!");
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have the 'SWAT GUARDS' upgrade! Buy it from the guard shop!");
        }

        return true;
    }
}