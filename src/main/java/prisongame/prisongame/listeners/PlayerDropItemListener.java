package prisongame.prisongame.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.Role;
import prisongame.prisongame.profile.ProfileKt;

public class PlayerDropItemListener implements Listener {
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        var player = event.getPlayer();
        var drop = event.getItemDrop();
        var item = drop.getItemStack();
        var meta = item.getItemMeta();
        var type = item.getType();

        if (
                type == Material.BOWL ||
                type == Material.STONE_BUTTON ||
                type == Material.WOODEN_SWORD ||
                type == Material.GLASS_BOTTLE ||
                type == Material.IRON_SHOVEL ||
                type == Material.BUCKET ||
                item.getEnchantments().containsKey(Enchantment.VANISHING_CURSE) ||
                (meta != null &&
                        (meta.getDisplayName().contains("Prisoner Uniform") ||
                         meta.getDisplayName().contains("Prisoner's Pickaxe") ||
                         meta.getDisplayName().contains("Lumber's Axe") ||
                         meta.getDisplayName().contains("Shovel") ||
                         meta.getDisplayName().contains("Plumber"))))
            pseudoCancel(player, drop);

        var profile = ProfileKt.getProfile(player);
        if (profile.getRole() != Role.PRISONER) {
            if (!type.equals(Material.TRIPWIRE_HOOK)) {
                pseudoCancel(player, drop);
            } else {
                event.setCancelled(true);
            }
        }
    }

    private void pseudoCancel(Player player, Item drop) {
        player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
        drop.setItemStack(new ItemStack(Material.AIR));
    }
}
