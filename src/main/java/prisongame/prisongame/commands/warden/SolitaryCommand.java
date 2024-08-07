package prisongame.prisongame.commands.warden;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;
import prisongame.prisongame.profile.ProfileKt;

public class SolitaryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                Player g = Bukkit.getPlayer(args[0]);
                Integer solitcount = 0;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getDisplayName().contains("SOLITARY")) {
                        solitcount += 1;
                    }
                }
                if (solitcount < 3) {
                    if (PrisonGame.solitcooldown <= 0) {
                        var profile = ProfileKt.getProfile(g);
                        if (g.isOnline() && g != sender && profile.getRole() == Role.PRISONER) {
                            if (g.isDead())
                                g.spigot().respawn();
                            if (g.getGameMode() == GameMode.SPECTATOR) {
                                PrisonGame.solitcooldown = (20 * 60) * 2;
                                Bukkit.broadcastMessage(ChatColor.GRAY + g.getName() + " was sent to solitary!");
                                g.setGameMode(GameMode.ADVENTURE);
                                g.removePotionEffect(PotionEffectType.LUCK);
                                profile.setEscaped(true);
                                PrisonGame.solittime.put(g, 20 * 120);
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + g.getName() + " only prison:solit");
                                Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                                    g.teleport(PrisonGame.active.getSolitary().getLocation());
                                }, 3);
                                g.sendTitle("", "You're in solitary.", 10, 0, 10);
                                g.addPotionEffect(PotionEffectType.WATER_BREATHING.createEffect(Integer.MAX_VALUE, 1));
                                Player p = g;
                                p.setCustomName(ChatColor.GRAY + "[" + ChatColor.BLACK + "SOLITARY" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY + p.getName());
                                p.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.BLACK + "SOLITARY" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY + p.getName());
                                p.setDisplayName(ChatColor.GRAY + "[" + ChatColor.BLACK + "SOLITARY" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY + p.getName());

                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Solitary is on cooldown!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Solitary can't hold more than 3 people!");
                }
            }
        }

        return true;
    }
}