package prisongame.prisongame.commands.warden;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;
import prisongame.prisongame.profile.ProfileKt;

import java.util.HashMap;
import java.util.UUID;

public class FireCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                Player g = Bukkit.getPlayer(args[0]);
                var profile = ProfileKt.getProfile(g);
                if (g.isOnline() && g != sender && profile.getRole() != Role.PRISONER) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + sender.getName() + " only prison:strike");
                    Bukkit.broadcastMessage(ChatColor.GOLD + g.getName() + " was fired.");
                    MyListener.playerJoin(g, false);
                    g.sendTitle(ChatColor.RED + "you were fired.", "", 20, 60, 20);
                    if (PrisonGame.savedPlayerGuards.containsKey(PrisonGame.warden.getUniqueId())) {
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Saving warden save file...");
                        HashMap<UUID, Integer> roleHashMap = PrisonGame.savedPlayerGuards.get(PrisonGame.warden.getUniqueId());
                        if (PrisonGame.savedPlayerGuards.get(PrisonGame.warden.getUniqueId()).containsKey((g.getUniqueId()))) {
                            roleHashMap.remove(g.getUniqueId());
                        }
                        PrisonGame.savedPlayerGuards.put(PrisonGame.warden.getUniqueId(), roleHashMap);
                    }
                }
            }
        }

        return true;
    }
}
