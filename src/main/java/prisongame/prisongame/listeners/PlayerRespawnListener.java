package prisongame.prisongame.listeners;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.lib.Role;
import prisongame.prisongame.profile.ProfileKt;

import java.util.Random;

import static prisongame.prisongame.MyListener.playerJoin;

public class PlayerRespawnListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerRespawnEvent event) {
        var profile = ProfileKt.getProfile(event.getPlayer());
        event.getPlayer().playSound(event.getPlayer(), Sound.MUSIC_DISC_CHIRP, 1, 2);
        PrisonGame.worryachieve.put(event.getPlayer(), -1);
        Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
            if (profile.getRole() == Role.WARDEN) {
                Player nw = event.getPlayer();
                nw.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                nw.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                nw.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                nw.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

                ItemStack wardenSword = new ItemStack(Material.DIAMOND_SWORD);
                wardenSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                wardenSword.addEnchantment(Enchantment.DURABILITY, 2);

                nw.getInventory().addItem(wardenSword);
                nw.getInventory().addItem(new ItemStack(Material.BOW));
                nw.getInventory().addItem(new ItemStack(Material.ARROW, 64));

                ItemStack card = new ItemStack(Material.TRIPWIRE_HOOK);
                ItemMeta cardm = card.getItemMeta();
                cardm.setDisplayName(ChatColor.BLUE + "Keycard " + ChatColor.RED + "[CONTRABAND]");
                card.setItemMeta(cardm);
                nw.getInventory().addItem(card);
            }
            if (profile.getRole() == Role.SWAT) {
                Player g = event.getPlayer();
                ItemStack orangechest = new ItemStack(Material.NETHERITE_CHESTPLATE);


                ItemStack orangeleg = new ItemStack(Material.NETHERITE_LEGGINGS);
                orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);

                ItemStack orangeboot = new ItemStack(Material.LEATHER_BOOTS);
                orangeboot.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                LeatherArmorMeta orangelegItemMeta = (LeatherArmorMeta) orangeboot.getItemMeta();
                orangelegItemMeta.setColor(Color.GRAY);
                orangeboot.setItemMeta(orangelegItemMeta);

                g.getInventory().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
                g.getInventory().setChestplate(orangechest);
                g.getInventory().setLeggings(orangeleg);
                g.getInventory().setBoots(orangeboot);

                if (Keys.HEAD_GUARD.has(g)) {
                    g.getInventory().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
                }

                ItemStack wardenSword = new ItemStack(Material.DIAMOND_SWORD);

                g.getInventory().addItem(wardenSword);

                g.getInventory().addItem(new ItemStack(Material.BOW));
                g.getInventory().addItem(new ItemStack(Material.ARROW, 16));
                g.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 32));

                ItemStack card2 = new ItemStack(Material.IRON_SHOVEL);
                ItemMeta cardm2 = card2.getItemMeta();
                cardm2.setDisplayName(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]");
                cardm2.addEnchant(Enchantment.KNOCKBACK, 1, true);
                card2.setItemMeta(cardm2);
                g.getInventory().addItem(card2);

                ItemStack card = new ItemStack(Material.TRIPWIRE_HOOK);
                ItemMeta cardm = card.getItemMeta();
                cardm.setDisplayName(ChatColor.BLUE + "Keycard " + ChatColor.RED + "[CONTRABAND]");
                card.setItemMeta(cardm);
                g.getInventory().addItem(card);
            }
            if (profile.getRole() == Role.NURSE) {
                Player g = event.getPlayer();

                ItemStack orangechest = new ItemStack(Material.LEATHER_CHESTPLATE);
                orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                LeatherArmorMeta chestmeta = (LeatherArmorMeta) orangechest.getItemMeta();
                chestmeta.setColor(Color.PURPLE);
                orangechest.setItemMeta(chestmeta);

                ItemStack orangeleg = new ItemStack(Material.LEATHER_LEGGINGS);
                orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                LeatherArmorMeta orangelegItemMeta = (LeatherArmorMeta) orangeleg.getItemMeta();
                orangelegItemMeta.setColor(Color.PURPLE);
                orangeleg.setItemMeta(orangelegItemMeta);

                ItemStack orangeboot = new ItemStack(Material.LEATHER_BOOTS);
                orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                LeatherArmorMeta orangebootItemMeta = (LeatherArmorMeta) orangeboot.getItemMeta();
                orangebootItemMeta.setColor(Color.PURPLE);
                orangeboot.setItemMeta(orangebootItemMeta);

                g.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                g.getInventory().setChestplate(orangechest);
                g.getInventory().setLeggings(orangeleg);
                g.getInventory().setBoots(orangeboot);

                if (Keys.HEAD_GUARD.has(g)) {
                    g.getInventory().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
                }

                ItemStack wardenSword = new ItemStack(Material.STONE_SWORD);
                wardenSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                wardenSword.addEnchantment(Enchantment.DURABILITY, 1);

                g.getInventory().addItem(wardenSword);

                g.getInventory().addItem(new ItemStack(Material.CROSSBOW));
                g.getInventory().addItem(new ItemStack(Material.ARROW, 16));
                g.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 32));

                ItemStack pot = new ItemStack(Material.SPLASH_POTION);
                PotionMeta potionMeta = (PotionMeta) pot.getItemMeta();
                potionMeta.addCustomEffect(PotionEffectType.HEAL.createEffect(10, 0), true);
                pot.setItemMeta(potionMeta);

                g.getInventory().addItem(pot);

                ItemStack card2 = new ItemStack(Material.IRON_SHOVEL);
                ItemMeta cardm2 = card2.getItemMeta();
                cardm2.setDisplayName(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]");
                cardm2.addEnchant(Enchantment.KNOCKBACK, 1, true);
                card2.setItemMeta(cardm2);
                g.getInventory().addItem(card2);

                ItemStack card = new ItemStack(Material.TRIPWIRE_HOOK);
                ItemMeta cardm = card.getItemMeta();
                cardm.setDisplayName(ChatColor.BLUE + "Keycard " + ChatColor.RED + "[CONTRABAND]");
                card.setItemMeta(cardm);
                g.getInventory().addItem(card);

            }
            if (profile.getRole() == Role.GUARD) {
                Player g = event.getPlayer();



                ItemStack orangechest = new ItemStack(Material.LEATHER_CHESTPLATE);
                orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                LeatherArmorMeta chestmeta = (LeatherArmorMeta) orangechest.getItemMeta();
                chestmeta.setColor(Color.fromRGB(126, 135, 245));
                orangechest.setItemMeta(chestmeta);

                ItemStack orangeleg = new ItemStack(Material.LEATHER_LEGGINGS);
                orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                LeatherArmorMeta orangelegItemMeta = (LeatherArmorMeta) orangeleg.getItemMeta();
                orangelegItemMeta.setColor(Color.fromRGB(126, 135, 245));
                orangeleg.setItemMeta(orangelegItemMeta);

                ItemStack orangeboot = new ItemStack(Material.LEATHER_BOOTS);
                orangechest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                LeatherArmorMeta orangebootItemMeta = (LeatherArmorMeta) orangeboot.getItemMeta();
                orangebootItemMeta.setColor(Color.fromRGB(126, 135, 245));
                orangeboot.setItemMeta(orangebootItemMeta);

                g.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                g.getInventory().setChestplate(orangechest);
                g.getInventory().setLeggings(orangeleg);
                g.getInventory().setBoots(orangeboot);

                if (Keys.HEAD_GUARD.has(g)) {
                    g.getInventory().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
                }

                ItemStack wardenSword = new ItemStack(Material.IRON_SWORD);
                wardenSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                wardenSword.addEnchantment(Enchantment.DURABILITY, 1);

                g.getInventory().addItem(wardenSword);

                g.getInventory().addItem(new ItemStack(Material.CROSSBOW));
                g.getInventory().addItem(new ItemStack(Material.ARROW, 16));
                g.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 32));

                ItemStack card2 = new ItemStack(Material.IRON_SHOVEL);
                ItemMeta cardm2 = card2.getItemMeta();
                cardm2.setDisplayName(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]");
                cardm2.addEnchant(Enchantment.KNOCKBACK, 1, true);
                card2.setItemMeta(cardm2);
                g.getInventory().addItem(card2);

                ItemStack card = new ItemStack(Material.TRIPWIRE_HOOK);
                ItemMeta cardm = card.getItemMeta();
                cardm.setDisplayName(ChatColor.BLUE + "Keycard " + ChatColor.RED + "[CONTRABAND]");
                card.setItemMeta(cardm);
                g.getInventory().addItem(card);


            }
            if (profile.getRole() == Role.PRISONER) {
                playerJoin(event.getPlayer(), false);
                Player p = event.getPlayer();
                event.getPlayer().sendTitle("", ChatColor.GOLD + "you died.");
                p.setCustomName(ChatColor.GRAY + "[" + ChatColor.GOLD + "PRISONER" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY + p.getName());
                p.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.GOLD + "PRISONER" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY + p.getName());
                p.setDisplayName(ChatColor.GRAY + "[" + ChatColor.GOLD + "PRISONER" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY + p.getName());

            }
            if (Keys.SPAWN_PROTECTION.has(event.getPlayer())) {
                if (profile.getRole() != Role.PRISONER) {
                    if (event.getPlayer().getInventory().getHelmet() != null)
                        event.getPlayer().getInventory().getHelmet().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    if (event.getPlayer().getInventory().getChestplate() != null)
                        event.getPlayer().getInventory().getChestplate().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    if (event.getPlayer().getInventory().getLeggings() != null)
                        event.getPlayer().getInventory().getLeggings().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    if (event.getPlayer().getInventory().getBoots() != null)
                        event.getPlayer().getInventory().getBoots().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                }
            }
            if (event.getPlayer().getDisplayName().contains("BOOSTER")) {
                ItemStack orangeboot = new ItemStack(Material.LEATHER_BOOTS);
                if (event.getPlayer().getInventory().getBoots().getEnchantments().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                    orangeboot.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                }
                LeatherArmorMeta orangebootItemMeta = (LeatherArmorMeta) orangeboot.getItemMeta();
                orangebootItemMeta.setColor(Color.PURPLE);
                orangeboot.setItemMeta(orangebootItemMeta);

                event.getPlayer().getInventory().setBoots(orangeboot);

            }
            event.getPlayer().sendTitle( ChatColor.DARK_RED + "RESPAWNING", ChatColor.RED + "Wait 15 seconds.");
            PrisonGame.tptoBed(event.getPlayer());
            event.getPlayer().addPotionEffect(PotionEffectType.LUCK.createEffect(15 * 20, 255));
            event.getPlayer().addPotionEffect(PotionEffectType.BLINDNESS.createEffect(15 * 20, 0));
            LivingEntity bat = (LivingEntity) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.BAT);
            bat.setInvulnerable(true);
            bat.setAI(false);
            bat.setInvisible(true);
            bat.setSilent(true);
            bat.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(99999999, 10));
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            if (Keys.RANDOM_ITEMS.has(event.getPlayer())) {
                Material[] rands = {
                        Material.WOODEN_AXE,
                        Material.GOLDEN_APPLE,
                        Material.BOW,
                        Material.SUSPICIOUS_STEW,
                        Material.GOLDEN_PICKAXE,
                        Material.BAKED_POTATO,
                        Material.STONE_AXE,
                };
                event.getPlayer().getInventory().addItem(new ItemStack(rands[new Random().nextInt(0, rands.length)]));
                event.getPlayer().getInventory().addItem(new ItemStack(rands[new Random().nextInt(0, rands.length)]));
                event.getPlayer().getInventory().addItem(new ItemStack(rands[new Random().nextInt(0, rands.length)]));
            }
            Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                event.getPlayer().stopSound(Sound.MUSIC_DISC_CHIRP);
                if (!event.getPlayer().getDisplayName().contains("SOLITARY")) {
                    event.getPlayer().setGameMode(GameMode.SPECTATOR);
                    PrisonGame.killior.put(event.getPlayer(), null);
                    event.getPlayer().setSpectatorTarget(null);
                    PrisonGame.tptoBed(event.getPlayer());
                    bat.remove();
                    if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) {
                        event.getPlayer().setGameMode(GameMode.ADVENTURE);
                        event.getPlayer().setNoDamageTicks(20 * 15);
                        event.getPlayer().addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(20 * 15, 0));
                        if (event.getPlayer().getGameMode() != GameMode.ADVENTURE) {
                            event.getPlayer().setGameMode(GameMode.ADVENTURE);
                            PrisonGame.tptoBed(event.getPlayer());
                            Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                                PrisonGame.tptoBed(event.getPlayer());
                            }, 7);
                            Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                                PrisonGame.tptoBed(event.getPlayer());
                            }, 10);
                        }
                    }
                }
            }, 20 * 15);
            Player p = event.getPlayer();
            if (PrisonGame.hardmode.get(p)) {
                int prisonerNumber = profile.getPrisonerNumber();
                PlayerDisguise playerDisguise = new PlayerDisguise("pdlCAMERA");
                playerDisguise.setName("Prisoner " + prisonerNumber);
                playerDisguise.setKeepDisguiseOnPlayerDeath(true);
                DisguiseAPI.disguiseToAll(p, playerDisguise);
                p.setCustomName(ChatColor.GRAY + "[" + ChatColor.GOLD + "PRISONER" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY  + "Prisoner " + prisonerNumber);
                p.setDisplayName(ChatColor.GRAY + "[" + ChatColor.GOLD + "PRISONER" + ChatColor.GRAY + "] " + ChatColor.DARK_GRAY  + "Prisoner " + prisonerNumber);
                p.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.RED + "HARD MODE" + ChatColor.DARK_GRAY + "] " + p.getName());
            }
        }, 1);
    }
}
