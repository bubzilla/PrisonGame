package prisongame.prisongame.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import oshi.jna.platform.mac.SystemB;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;
import prisongame.prisongame.profile.ProfileKt;

import java.util.HashMap;
import java.util.UUID;

public class AcceptCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var profile = ProfileKt.getProfile((Player) sender);
        var invite = profile.getInvite();

        if (invite == null) {
            sender.sendMessage("You haven't been invited!");
            return true;
        }

        switch (invite) {
            case NURSE -> PrisonGame.setNurse((Player) sender);
            case GUARD -> PrisonGame.setGuard((Player) sender);
            case SWAT -> PrisonGame.setSwat((Player) sender);
            case WARDEN -> {
                MyListener.playerJoin(PrisonGame.warden, false);
                PrisonGame.warden = null;
                ((Player) sender).performCommand("warden");

                for (var player : Bukkit.getOnlinePlayers()) {
                    var playerProfile = ProfileKt.getProfile(player);

                    if (playerProfile.getInvite() == Role.WARDEN)
                        playerProfile.setInvite(null);
                }
            }
            default -> sender.sendMessage("You haven't been invited!");
        }
        if (invite.ordinal() > 0 && invite != Role.WARDEN) {
            if (!PrisonGame.savedPlayerGuards.containsKey(PrisonGame.warden.getUniqueId())) {
                Bukkit.broadcastMessage(ChatColor.AQUA + "Creating warden save file...");
                HashMap<UUID, Integer> roleHashMap = new HashMap<>();
                roleHashMap.put(((Player) sender).getUniqueId(), invite.ordinal());
                PrisonGame.savedPlayerGuards.put(PrisonGame.warden.getUniqueId(), roleHashMap);
            } else {
                Bukkit.broadcastMessage(ChatColor.AQUA + "Saving warden save file...");
                HashMap<UUID, Integer> roleHashMap = PrisonGame.savedPlayerGuards.get(PrisonGame.warden.getUniqueId());
                if (PrisonGame.savedPlayerGuards.get(PrisonGame.warden.getUniqueId()).containsKey(((Player) sender).getUniqueId())) {
                    roleHashMap.remove(((Player) sender).getUniqueId());
                }
                roleHashMap.put(((Player) sender).getUniqueId(), invite.ordinal());
                PrisonGame.savedPlayerGuards.put(PrisonGame.warden.getUniqueId(), roleHashMap);
            }
        }

        profile.setInvite(null);

        return true;
    }
}