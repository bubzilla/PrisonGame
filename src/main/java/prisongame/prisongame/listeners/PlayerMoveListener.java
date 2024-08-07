package prisongame.prisongame.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.profile.ProfileKt;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (PrisonGame.isInside(event.getPlayer(), PrisonGame.active.getFirstTrack().getLocation(), PrisonGame.active.getSecondTrack().getLocation()) && PrisonGame.active.getFirstTrack().getY() > event.getPlayer().getLocation().getY()) {
            var profile = ProfileKt.getProfile(event.getPlayer());
            profile.setSpeed(profile.getSpeed() + 0.5);
            event.getPlayer().sendTitle("", ChatColor.GREEN + String.valueOf(profile.getSpeed()) + "/120", 0, 10, 10);
            if (profile.getSpeed() >= 120) {
                profile.setSpeed(0.0);
                if (event.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
                    event.getPlayer().addPotionEffect(PotionEffectType.SPEED.createEffect(event.getPlayer().getPotionEffect(PotionEffectType.SPEED).getDuration() + 20 * 25, 0));
                } else {
                    event.getPlayer().addPotionEffect(PotionEffectType.SPEED.createEffect(20 * 30, 0));
                }
            }
        }
    }
}
