package prisongame.prisongame.listeners;

import kotlin.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.MyListener;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.config.Prison;
import prisongame.prisongame.lib.Role;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.profile.ProfileKt;

import static prisongame.prisongame.MyListener.reloadBert;
import static prisongame.prisongame.config.ConfigKt.getConfig;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onShulkerBox(InventoryClickEvent event) {
        var inventory = event.getClickedInventory();

        if (inventory == null)
            return;

        var player = (Player) event.getWhoClicked();
        var type = inventory.getType();
        var action = event.getAction();

        if (type != InventoryType.ENDER_CHEST)
            return;

        var item = event.getCurrentItem();

        if (item == null || item.getType() != Material.SHULKER_BOX)
            return;

        if (action != InventoryAction.PICKUP_ALL && action != InventoryAction.SWAP_WITH_CURSOR) {
            event.setCancelled(true);
            return;
        }

        var meta = (BlockStateMeta) item.getItemMeta();
        var blockState = (ShulkerBox) meta.getBlockState();
        var shulkerInventory = blockState.getInventory();
        event.setCancelled(true);

        if (action == InventoryAction.SWAP_WITH_CURSOR) {
            if (shulkerInventory.firstEmpty() == -1)
                return;

            shulkerInventory.addItem(event.getCursor());
            meta.setBlockState(blockState);
            item.setItemMeta(meta);
            event.setCurrentItem(item);
            player.setItemOnCursor(null);
            return;
        }

        PrisonGame.shulkers.put(player, new Pair<>(event, item));
        player.openInventory(shulkerInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.PLAYER)) {
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getItemMeta() != null) {
                    var name = event.getCurrentItem().getItemMeta().getDisplayName();
                    var player = (Player) event.getWhoClicked();
                    
                    if (name.equals(ChatColor.DARK_AQUA + "2x Income")) {
                        if (Keys.ASCENSION_COINS.get(player) >= 25) {
                            Keys.DOUBLE_INCOME.set(player, 1);
                            Keys.ASCENSION_COINS.set(player, Keys.ASCENSION_COINS.get(player) - 25);
                            player.closeInventory();
                        }
                        event.setCancelled(true);
                    }
                    if (name.equals(ChatColor.DARK_AQUA + "Tax Evasion")) {
                        if (Keys.ASCENSION_COINS.get(player) >= 25) {
                            Keys.TAX_EVASION.set(player, 1);
                            Keys.ASCENSION_COINS.set(player, Keys.ASCENSION_COINS.get(player) - 25);
                            event.setCancelled(true);
                            player.closeInventory();
                        }
                        event.setCancelled(true);
                    }
                    if (name.equals(ChatColor.DARK_AQUA + "Semi Cloak")) {
                        if (Keys.ASCENSION_COINS.get(player) >= 5) {
                            Keys.SEMICLOAK.set(player, 1);
                            Keys.ASCENSION_COINS.set(player, Keys.ASCENSION_COINS.get(player) - 5);
                            event.setCancelled(true);
                            player.closeInventory();
                        }
                        event.setCancelled(true);
                    }
                    if (name.equals(ChatColor.DARK_AQUA + "Reinforcements")) {
                        if (Keys.ASCENSION_COINS.get(player) >= 30) {
                            Keys.REINFORCEMENT.set(player, 1);
                            Keys.ASCENSION_COINS.set(player, Keys.ASCENSION_COINS.get(player) - 30);
                            event.setCancelled(true);
                            player.closeInventory();
                        }
                        event.setCancelled(true);
                    }
                    if (name.equals(ChatColor.DARK_AQUA + "ProtSpawn")) {
                        if (Keys.ASCENSION_COINS.get(player) >= 10) {
                            Keys.SPAWN_PROTECTION.set(player, 1);
                            Keys.ASCENSION_COINS.set(player, Keys.ASCENSION_COINS.get(player) - 10);
                            event.setCancelled(true);
                            player.closeInventory();
                        }
                        event.setCancelled(true);
                    }
                    if (name.equals(ChatColor.DARK_AQUA + "Random Items")) {
                        if (Keys.ASCENSION_COINS.get(player) >= 3) {
                            Keys.RANDOM_ITEMS.set(player, 1);
                            Keys.ASCENSION_COINS.set(player, Keys.ASCENSION_COINS.get(player) - 3);
                            event.setCancelled(true);
                            player.closeInventory();
                        }
                        event.setCancelled(true);
                    }
                    if (name.contains(ChatColor.YELLOW + "Get Coords [CUSTOM]")) {
                        event.setCancelled(true);
                        var location = player.getLocation();
                        player.sendMessage(PrisonGame.mm.deserialize(
                                "<click> <gray>to copy the coordinates to your clipboard.",
                                Placeholder.component("click", Component
                                        .text("Click Here")
                                        .clickEvent(ClickEvent.copyToClipboard(String.format(
                                                "{ world = \"%s\", x = %s, y = %s, z = %s, yaw = %s, pitch = %s }",
                                                location.getWorld().getName(),
                                                location.getX(),
                                                location.getY(),
                                                location.getZ(),
                                                location.getYaw(),
                                                location.getPitch()
                                        ))))
                        ));
                    } else if (name.contains("[CUSTOM]")) {
                        if (player.hasPermission("minecraft.command.gamemode")) {
                            event.setCancelled(true);
                            var id = name.replace(" [CUSTOM]", "").substring(2);
                            var prison = getConfig().getPrisons().get(id);

                            if (prison != null)
                                player.teleport(prison.getPrisoner().getLocation());
                        }
                    }
                    if (name.contains("[CMD]")) {
                        if (player.hasPermission("minecraft.command.gamemode")) {
                            if (event.getCurrentItem().getItemMeta().getLore() != null) {
                                event.setCancelled(true);
                                Bukkit.dispatchCommand(player, event.getCurrentItem().getItemMeta().getLore().get(0));
                            }
                        }
                    }
                    if (name.equals(ChatColor.LIGHT_PURPLE + "epic bertude night vision")) {
                        event.setCancelled(true);
                        if (!Keys.NIGHT_VISION.has(player)) {
                            Keys.NIGHT_VISION.set(player, 1);
                            player.sendMessage("ok i changed that for u lol");
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                        } else {
                            Keys.NIGHT_VISION.remove(player);
                            player.sendMessage("ok i changed that for u lol");
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                        }
                    }
                    if (name.equals(ChatColor.BLUE + "old tab")) {
                        event.setCancelled(true);
                        if (!Keys.OLD_TAB.has(player)) {
                            Keys.OLD_TAB.set(player, 1);
                            player.sendMessage("ok i changed that for u lol");
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                        } else {
                            Keys.OLD_TAB.remove(player);
                            player.sendMessage("ok i changed that for u lol");
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                        }
                    }

                    if (name.equals(ChatColor.LIGHT_PURPLE + "-1 dollar")) {
                        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                            event.setCancelled(true);
                            player.damage(999999);
                            Bukkit.broadcastMessage(player.getName() + " was robbed by bertrude (L)");
                        } else {
                            player.sendMessage("Bertrude was nice today and decided not to rob you :D");
                        }
                    }

                    if (name.equals(ChatColor.LIGHT_PURPLE + "no warden spaces")) {
                        event.setCancelled(true);
                        if (!Keys.NO_WARDEN_SPACES.has(player)) {
                            Keys.NO_WARDEN_SPACES.set(player, 1);
                            player.sendMessage("ok i changed that for u lol");
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                        } else {
                            Keys.NO_WARDEN_SPACES.remove(player);
                            player.sendMessage("ok i changed that for u lol");
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                        }
                    }

                    if (name.equals(ChatColor.GOLD + "ping noises")) {
                        event.setCancelled(true);

                        var value = Keys.PING_NOISES.get(player, 0);
                        Keys.PING_NOISES.get(player, value == 1 ? 0 : 1);

                        player.sendMessage("ok i changed that for u lol");
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                    }

                    if (name.equals(ChatColor.DARK_PURPLE + "buy shulker box")) {
                        event.setCancelled(true);
                        var money = Keys.MONEY.get(player, 0.0);
                        var inventory = player.getEnderChest();
                        var cost = 4000;

                        if (money < cost) {
                            player.sendMessage("you don't got enough money");
                            return;
                        }

                        if (inventory.firstEmpty() == -1) {
                            player.sendMessage("you don't have any room in your ender chest for a shulker box");
                            return;
                        }

                        if (inventory.contains(Material.SHULKER_BOX, 3)) {
                            player.sendMessage("you've hit a limit of 3 shulker boxes");
                            return;
                        }

                        Keys.MONEY.set(player, money - cost);

                        int i = 0;

                        while (isShulker(inventory, i)) {
                            i += 9;
                            i %= 26;
                        }

                        var item = inventory.getItem(i);

                        if (item != null && !item.isEmpty()) {
                            var emptySlot = inventory.firstEmpty();
                            inventory.setItem(emptySlot, item);
                            inventory.clear(i);
                        }

                        inventory.setItem(i, new ItemStack(Material.SHULKER_BOX));

                        player.sendMessage("you now have a shulker box");
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
                    }
                }
            }
        }
    }

    private boolean isShulker(Inventory inventory, int index) {
        var item = inventory.getItem(index);

        if (item == null)
            return false;

        return item.getType() == Material.SHULKER_BOX;
    }

    @EventHandler
    public void onInventoryClick3(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE) || event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
                event.setCancelled(true);
            }

            if (event.getCurrentItem().getItemMeta() != null) {
                var name = event.getCurrentItem().getItemMeta().getDisplayName();
                var player = (Player) event.getWhoClicked();
                if (name.equals(ChatColor.YELLOW + "Not Drugs")) {
                    if (Keys.MONEY.get(player, 0.0) >= 30.0) {
                        Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0)  - 30.0);
                        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
                    }
                }

                if (name.equals(ChatColor.YELLOW + "Scrap Metal")) {
                    if (Keys.MONEY.get(player, 0.0) >= 150.0) {
                        Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0)  - 150.0);
                        player.getInventory().addItem(new ItemStack(Material.RAW_IRON));
                    }
                }

                if (name.equals(ChatColor.YELLOW + "Dagger")) {
                    if (Keys.MONEY.get(player, 0.0) >= 1000.0) {
                        Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0)  - 1000.0);
                        player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
                    }
                }

                if (name.equals(ChatColor.YELLOW + "Chainmail Helmet")) {
                    if (Keys.MONEY.get(player, 0.0) >= 300.0) {
                        Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0)  - 300.0);
                        player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_HELMET));
                    }
                }

                if (name.equals(ChatColor.YELLOW + "Soup")) {
                    if (Keys.MONEY.get(player, 0.0) >= 2.0) {
                        Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0)  - 2.0);
                        player.getInventory().addItem(new ItemStack(Material.BEETROOT_SOUP));
                    }
                }
                if (name.equals(ChatColor.YELLOW + "Supreme Stick")) {
                    if (Keys.MONEY.get(player, 0.0) >= 50.0) {
                        Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0)  - 50.0);
                        player.getInventory().addItem(new ItemStack(Material.STICK));
                    }
                }
                if (name.equals(ChatColor.YELLOW + "Coal")) {
                    if (Keys.MONEY.get(player, 0.0) >= 30.0) {
                        Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0)  - 30.0);
                        player.getInventory().addItem(new ItemStack(Material.COAL));
                    }
                }

                if (name.equals(ChatColor.BLUE + "SWAT Guards")) {
                    if (Keys.MONEY.get(player, 0.0) >= 2500.0) {
                        Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0)  - 2.0);
                        PrisonGame.swat = true;
                        Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has enabled SWAT guards!");
                    }
                }
                if (name.equals(ChatColor.BLUE + "Prot 1")) {
                    if (Keys.MONEY.get(player, 0.0) >= 500.0) {
                        Boolean shouldpay = false;
                        if (player.getInventory().getHelmet() != null) {
                            if (player.getInventory().getHelmet().getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                                player.getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                                shouldpay = true;
                            }
                        }
                        if (player.getInventory().getChestplate() != null) {
                            if (player.getInventory().getChestplate().getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                                player.getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                                shouldpay = true;
                            }
                        }
                        if (player.getInventory().getLeggings() != null) {
                            if (player.getInventory().getLeggings().getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                                player.getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                                shouldpay = true;
                            }
                        }
                        if (player.getInventory().getBoots() != null) {
                            if (player.getInventory().getBoots().getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                                player.getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                                shouldpay = true;
                            }
                        }
                        if (shouldpay)
                            Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0)  - 30.0);
                    }
                }
                if (PrisonGame.warden != null) {
                    if (PrisonGame.swapcool <= 0 && PrisonGame.warden.equals(player)) {
                        var prisonName = event.getCurrentItem().getItemMeta().getDisplayName();

                        for (var prison : getConfig().getPrisons().values()) {
                            if (prison.getDisplayName().equals(prisonName)) {
                                switchMap(prison);
                                event.setCancelled(true);
                                break;
                            }
                        }
                    }
                }
                if (name.equals(ChatColor.LIGHT_PURPLE + "Rock")) {
                    event.setCancelled(true);
                    if (player.getInventory().containsAtLeast(new ItemStack(Material.STONE_BUTTON), 9)) {
                        player.getInventory().removeItem(new ItemStack(Material.STONE_BUTTON, 9));
                        player.getInventory().addItem(new ItemStack(Material.COBBLESTONE));
                    }
                }
                if (name.equals(ChatColor.WHITE + "Paper")) {
                    event.setCancelled(true);
                    if (Keys.MONEY.get(player, 0.0) >= 15.0) {
                        if (player.getInventory().contains(Material.COAL) && player.getInventory().contains(Material.RAW_IRON)) {
                            Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0) - 15.0);
                            player.getInventory().removeItem(new ItemStack(Material.COAL, 1));
                            player.getInventory().removeItem(new ItemStack(Material.RAW_IRON, 1));
                            player.getInventory().addItem(new ItemStack(Material.PAPER));
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Not enough money!");
                    }
                }
                if (name.equals(ChatColor.LIGHT_PURPLE + "Fake Card")) {
                    event.setCancelled(true);
                    if (player.getInventory().containsAtLeast(new ItemStack(Material.PAPER), 3) && player.getInventory().containsAtLeast(new ItemStack(Material.STICK), 2)) {
                        player.getInventory().removeItem(new ItemStack(Material.PAPER, 3));
                        player.getInventory().removeItem(new ItemStack(Material.STICK, 2));
                        ItemStack card = new ItemStack(Material.TRIPWIRE_HOOK);
                        ItemMeta cardm = card.getItemMeta();
                        cardm.setDisplayName(ChatColor.BLUE + "Keycard " + ChatColor.RED + "[CONTRABAND]");
                        card.setItemMeta(cardm);
                        player.getInventory().addItem(card);
                    }
                }
                if (name.equals(ChatColor.GRAY + "WireCutters")) {
                    event.setCancelled(true);
                    if (player.getInventory().containsAtLeast(new ItemStack(Material.COBBLESTONE), 1) &&  player.getInventory().containsAtLeast(new ItemStack(Material.RAW_IRON), 4) && player.getInventory().containsAtLeast(new ItemStack(Material.STICK), 2)) {
                        player.getInventory().removeItem(new ItemStack(Material.RAW_IRON, 4));
                        player.getInventory().removeItem(new ItemStack(Material.STICK, 2));
                        player.getInventory().removeItem(new ItemStack(Material.COBBLESTONE, 1));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " iron_pickaxe{Damage:245,display:{Name:'[{\"text\":\"WireCutters\",\"italic\":false,\"color\":\"gray\"},{\"text\":\" \"},{\"text\":\"[CONTRABAND]\",\"color\":\"red\"}]'},Enchantments:[{id:efficiency,lvl:5}],HideFlags:1,CanDestroy:[iron_bars]} 1");
                    }
                }
                if (name.equals(ChatColor.DARK_GRAY + "Cloak")) {
                    event.setCancelled(true);
                    if (Keys.MONEY.get(player, 0.0) >= 15.0) {
                        if (player.getInventory().contains(Material.COAL)) {
                            Keys.MONEY.set(player, Keys.MONEY.get(player, 0.0) - 15.0);
                            player.getInventory().removeItem(new ItemStack(Material.COAL, 1));
                            ItemStack orangeboot = new ItemStack(Material.LEATHER_CHESTPLATE);

                            LeatherArmorMeta orangelegItemMeta = (LeatherArmorMeta) orangeboot.getItemMeta();
                            orangelegItemMeta.setDisplayName(ChatColor.DARK_GRAY + "Cloak Chestplate");
                            orangelegItemMeta.setColor(Color.BLACK);
                            orangeboot.setItemMeta(orangelegItemMeta);
                            player.getInventory().setChestplate(orangeboot);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Not enough money!");
                    }
                }
                if (name.equals(ChatColor.LIGHT_PURPLE + "Normal Crafting")) {
                    player.openWorkbench(null, true);
                }
            }
        }
    }

    public static void switchMap(Prison prison) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            var profile = ProfileKt.getProfile(player);
            if (player.getDisplayName().contains("ASCENDING") || PrisonGame.builder.getOrDefault(player, false))
                continue;

            player.teleport(prison.getPrisoner().getLocation());
            if (profile.getRole() != Role.PRISONER) {
                player.teleport(prison.getWarden().getLocation());
            }
        }

        PrisonGame.BBpower = 100;
        PrisonGame.active = prison;
        PrisonGame.swapcool = 20 * 60 * 5;

        reloadBert();
        Bukkit.getWorld("world").getBlockAt(new Location(Bukkit.getWorld("world"),-1023,-57,-994)).setType(Material.REDSTONE_BLOCK);

        for (Player player : Bukkit.getOnlinePlayers()) {
            var profile = ProfileKt.getProfile(player);
            if (PrisonGame.builder.get(player))
                continue;

            if (player.isSleeping())
                player.wakeup(false);

            if (player.isInsideVehicle()) {
                player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
                player.removePotionEffect(PotionEffectType.WEAKNESS);
                player.getVehicle().removePassenger(player);
            }

            for (var passenger : player.getPassengers()) {
                if (passenger instanceof Player playerPassenger) {
                    playerPassenger.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
                    playerPassenger.removePotionEffect(PotionEffectType.WEAKNESS);
                }

                player.removePassenger(passenger);
            }

            if (profile.getRole() != Role.WARDEN) {
                MyListener.playerJoin(player, true);
                player.sendTitle(ChatColor.GREEN + "New prison!", ChatColor.BOLD + prison.getName().toUpperCase());
                continue;
            }

            player.teleport(prison.getWarden().getLocation());

            Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                player.teleport(prison.getWarden().getLocation());
            }, 5);

            if (!player.getDisplayName().contains("ASCENDING"))
                player.sendTitle("New prison!", prison.getName().toUpperCase());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick4(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        var profile = ProfileKt.getProfile(player);

        if (!player.getOpenInventory().getTitle().equals("Map Switch")) {
            if (player.getGameMode().equals(GameMode.ADVENTURE) || profile.getRole() != Role.WARDEN || event.isCancelled() || player.getOpenInventory().getType() == InventoryType.CRAFTING) {
                return;
            }
            player.sendMessage(ChatColor.RED + "Wardens cannot interact with containers.");
            event.setCancelled(true);
        }
    }
}
