package prisongame.prisongame;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Door;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import prisongame.prisongame.commands.staff.VanishCommand;
import prisongame.prisongame.keys.Keys;
import prisongame.prisongame.lib.Role;
import prisongame.prisongame.listeners.InventoryClickListener;

import java.text.DecimalFormat;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class MyTask extends BukkitRunnable {

    public static Integer jobm = 1;
    static Boolean hasAlerted = true;

    static Integer timer1;
    static Integer timer2;

    public static BossBar bossbar = Bukkit.createBossBar(
            ChatColor.WHITE + "Morning",
            BarColor.WHITE,
            BarStyle.SOLID);

    @Override
    public void run() {
        PrisonGame.solitcooldown = PrisonGame.solitcooldown - 1;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Keys.TRUST.get(p, 0.0) > 0) {
                Keys.TRUST.set(p, 0.0);
            }
            DisguiseAPI.setActionBarShown(p, false);
            if (p.isSleeping()) {
                p.setNoDamageTicks(10);
            }
            if (!PrisonGame.wealthcycle.containsKey(p)) {
                PrisonGame.wealthcycle.put(p, Keys.MONEY.get(p));
            }
            if (!PrisonGame.wardentime.containsKey(p) || p != PrisonGame.warden) {
                PrisonGame.wardentime.put(p, 0);
            }
            if (!PrisonGame.worryachieve.containsKey(p)) {
                PrisonGame.worryachieve.put(p, -1);
            }
            if (!PrisonGame.axekills.containsKey(p)) {
                PrisonGame.axekills.put(p, 0);
            }
            if (!PrisonGame.prisonerlevel.containsKey(p)) {
                PrisonGame.prisonerlevel.put(p, 0);
            }
            if (Keys.HEAD_GUARD.has(p))
                Keys.HEAD_GUARD.remove(p);
            PrisonGame.handcuff.put(p, null);
            if (PrisonGame.worryachieve.get(p) >= 0) {
                PrisonGame.worryachieve.put(p, PrisonGame.worryachieve.get(p) + 1);
                if (PrisonGame.worryachieve.get(p) / 20 / 60 >= 15) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:whatuworried");
                    PrisonGame.worryachieve.put(p, -1);
                }
            }
            if (PrisonGame.warden == p) {
                PrisonGame.wardentime.put(p, PrisonGame.wardentime.get(p) + 1);
                var advancement = Bukkit.getAdvancement(new NamespacedKey("prison", "dictatorship"));
                if (PrisonGame.wardentime.get(p) / 20 / 60 >= 120 && !p.getAdvancementProgress(advancement).isDone()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:dictatorship");
//                    PrisonGame.wardentime.put(p, Integer.MIN_VALUE);
                }
            }
            if (!PrisonGame.saidcycle.containsKey(p)) {
                PrisonGame.saidcycle.put(p, 0);
            }
            if (!PrisonGame.roles.containsKey(p)) {
                PrisonGame.roles.put(p, Role.PRISONER);
                MyListener.playerJoin(p, false);
            }
            if (!PrisonGame.st.containsKey(p)) {
                PrisonGame.st.put(p, 0.0);
            }
            if (!PrisonGame.hardmode.containsKey(p)) {
                PrisonGame.hardmode.put(p, false);
            }
            if (!PrisonGame.sp.containsKey(p)) {
                PrisonGame.sp.put(p, 0.0);
            }
            if (!PrisonGame.lastward.containsKey(p)) {
                PrisonGame.lastward.put(p, 20 * 10);
            }
            PrisonGame.lastward.put(p, PrisonGame.lastward.get(p) + 1);
            if (!PrisonGame.lastward2.containsKey(p)) {
                PrisonGame.lastward2.put(p, 0);
            }
            if (!PrisonGame.wardenban.containsKey(p)) {
                PrisonGame.wardenban.put(p, 0);
            }
            if (!PrisonGame.trustlevel.containsKey(p)) {
                PrisonGame.trustlevel.put(p, 0);
            }
            if (!PrisonGame.solittime.containsKey(p)) {
                PrisonGame.solittime.put(p, 0);
            }
            PrisonGame.solittime.put(p, PrisonGame.solittime.get(p) - 1);
            if (PrisonGame.solittime.get(p) <= 0 && p.getDisplayName().contains("SOLITARY")) {
                PrisonGame.tptoBed(p);
                MyListener.playerJoin(p, false);
                p.removePotionEffect(PotionEffectType.WATER_BREATHING);
                p.sendMessage( "You were released from solitary.");
            }
            if (!PrisonGame.word.containsKey(p)) {
                PrisonGame.word.put(p, "Amoger Susser");
            }
            if (PrisonGame.lastward.get(p) > 20 * 5) {
                PrisonGame.lastward2.put(p, 0);
            }

            if (!PrisonGame.escaped.containsKey(p)) {
                PrisonGame.escaped.put(p, false);
            }
            if (!PrisonGame.killior.containsKey(p) || p.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE) || PrisonGame.roles.get(p) != Role.PRISONER) {
                PrisonGame.killior.put(p, null);
            }
            if (!PrisonGame.prisonnumber.containsKey(p)) {
                PrisonGame.prisonnumber.put(p, "690");
            }
            if (!PrisonGame.respect.containsKey(p)) {
                PrisonGame.respect.put(p, 0);
            }
            if (PrisonGame.givepig && PrisonGame.warden != null) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + PrisonGame.warden.getName() + " only prison:piggo");
                PrisonGame.givepig = false;
            }
            if (Keys.MUTED.has(p)) {
                if (Keys.MUTED.get(p) <= 0) {
                    Keys.MUTED.remove(p);
                } else {
                    Keys.MUTED.set(p, Keys.MUTED.get(p) - 1);
                }

            }
            if (PrisonGame.isInside(p, PrisonGame.nl("world", -50D, -53D, -109D, 0f, 0f), PrisonGame.nl("world", -48D, -59D, -107D, 0f, 0f))) {
                p.teleport(PrisonGame.nl("world", 70D, -59D, -69D, 180f, 0f));
            }
            if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                p.getInventory().remove(Material.NETHERITE_SWORD);
                p.getInventory().remove(Material.SUGAR);
                if (PrisonGame.hardmode.get(p)) {
                    p.getInventory().remove(Material.GOLDEN_APPLE);
                    p.getInventory().remove(Material.NETHERITE_HELMET);
                }
                p.getInventory().remove(Material.IRON_BARS);
            }
            if (PrisonGame.roles.get(p) != Role.PRISONER && PrisonGame.roles.get(p) != Role.WARDEN) {
                if (p.hasPotionEffect(PotionEffectType.UNLUCK)) {
                    p.sendMessage(ChatColor.RED + "You can't be in here!");
                    p.playSound(p, Sound.ENTITY_PILLAGER_AMBIENT, 1.5f, 0.75f);
                    p.damage(6);
                    p.removePotionEffect(PotionEffectType.UNLUCK);
                    p.teleport(PrisonGame.active.getBlackMarketOut().getLocation());
                }
            }
        }
        PrisonGame.swapcool -= 1;
        PrisonGame.lockdowncool -= 1;
        PrisonGame.wardenCooldown -= 1;
        if (Bukkit.getWorld("world").getTime() > 0 && Bukkit.getWorld("world").getTime() < 2000) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.removePotionEffect(PotionEffectType.BLINDNESS);
            }
            timer1 = 0;
            timer2 = 2500;
            bossbar.setTitle("ROLL CALL");
            hasAlerted = false;
        }
        if (MyTask.bossbar.getTitle().contains("ROLL CALL")) {
            Boolean allat = true;
            PrisonGame.gotcafefood.clear();
            for (Player p : Bukkit.getOnlinePlayers()) {

                p.removePotionEffect(PotionEffectType.BAD_OMEN);
                p.getWorld().getWorldBorder().setWarningDistance(5);
                if (PrisonGame.roles.get(p) != Role.PRISONER && PrisonGame.hardmode.get(p)) {
                    if (!new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ()).getBlock().getType().equals(Material.LIGHT_BLUE_CONCRETE_POWDER)) {
                        p.sendTitle("", ChatColor.BLUE + "GET ONTO LIGHT BLUE POWDER OR GET FIRED!", 0, 5, 0);
                        p.addPotionEffect(PotionEffectType.HUNGER.createEffect(200, 0));
                        p.setCollidable(true);
                        p.addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0));
                        p.removePotionEffect(PotionEffectType.JUMP);
                    } else {
                        p.setFoodLevel(6);
                        if (p.hasPotionEffect(PotionEffectType.GLOWING) && p.getGameMode() != GameMode.SPECTATOR) {
                            p.sendMessage(ChatColor.GREEN + "You came to roll call!");
                            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                        }
                        p.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20, 255));
                        p.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(20, 255));
                        p.removePotionEffect(PotionEffectType.HUNGER);
                        p.removePotionEffect(PotionEffectType.GLOWING);

                    }
                }
                if (PrisonGame.roles.get(p) == Role.PRISONER && !PrisonGame.escaped.get(p)) {
                    if (!new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ()).getBlock().getType().equals(Material.RED_SAND)) {
                        p.sendTitle("", ChatColor.RED + "GET ONTO THE RED SAND OR YOU'LL BE KILLED!", 0, 5, 0);
                        p.addPotionEffect(PotionEffectType.HUNGER.createEffect(200, 0));
                        p.setCollidable(true);
                        allat = false;
                        p.addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0));
                        p.removePotionEffect(PotionEffectType.JUMP);
                        Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Criminals").addPlayer(p);
                    } else {
                        if (PrisonGame.roles.get(p) == Role.PRISONER && Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(p) == Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Criminals")) {
                            Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Prisoners").addPlayer(p);
                        }
                        p.setHealth(p.getMaxHealth());
                        p.setFoodLevel(6);
                        if (p.hasPotionEffect(PotionEffectType.GLOWING) && p.getGameMode() != GameMode.SPECTATOR) {
                            p.sendMessage(ChatColor.GREEN + "You came to roll call!");
                            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                        }
                        p.addPotionEffect(PotionEffectType.SLOW.createEffect(10, 255));
                        p.addPotionEffect(PotionEffectType.JUMP.createEffect(20, -25));
                        p.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20, 255));
                        p.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(20, 255));
                        p.setCollidable(false);
                        p.removePotionEffect(PotionEffectType.HUNGER);
                        p.removePotionEffect(PotionEffectType.GLOWING);

                    }
                }
            }
            if (allat && PrisonGame.warden != null) {
                PrisonGame.warden.sendMessage(ChatColor.GREEN + "All prisoners at roll call! +1k$!");
                Keys.MONEY.set(PrisonGame.warden, Keys.MONEY.get(PrisonGame.warden) + 1000.0);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                        p.sendTitle("", ChatColor.BOLD + bossbar.getTitle(), 20, 40, 20);
                    }, 4);
                    p.playSound(p, Sound.BLOCK_BELL_USE, 1, 1);
                    Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                        p.playSound(p, Sound.BLOCK_BELL_USE, 1, 1);
                    }, 4);
                    Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                        p.playSound(p, Sound.BLOCK_BELL_USE, 1, 1);
                    }, 8);
                }
                Bukkit.getWorld("world").setTime(timer2 + 1);
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setCollidable(true);
                p.removePotionEffect(PotionEffectType.JUMP);
            }
        }
        if (Bukkit.getWorld("world").getTime() == 2000) {
            if (!hasAlerted) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (Keys.MONEY.has(p)) {
                        try {
                            if (Keys.MONEY.get(p, 0.0) - PrisonGame.wealthcycle.get(p) >= 3000) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:wealthy");
                            }
                            PrisonGame.wealthcycle.put(p, Keys.MONEY.get(p));
                        } catch (NullPointerException ignored) {
                            Bukkit.getLogger().info(p.getName() + " seems to not have a money container?");
                        }
                    }
                }
                hasAlerted = true;
                Double taxcount = 0.0;
                DecimalFormat numberFormat = new DecimalFormat("#0.0");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPotionEffect(PotionEffectType.GLOWING) && !PrisonGame.escaped.get(p) && PrisonGame.roles.get(p) == Role.PRISONER) {
                        Bukkit.broadcastMessage(ChatColor.RED + p.getName() + ChatColor.GOLD + " didn't come to roll call! " + ChatColor.RED + "Kill them for 100 dollars!");
                        p.sendTitle("", ChatColor.RED + "COME TO ROLL CALL NEXT TIME!", 0, 60, 0);
                        if (PrisonGame.hardmode.get(p)) {
                            p.damage(99999);
                            if (PrisonGame.roles.get(p) != Role.PRISONER) {
                                if (PrisonGame.warden != null) {
                                    if (PrisonGame.warden.equals(p)) {
                                        PrisonGame.warden = null;
                                    }
                                }
                                PrisonGame.roles.put((Player) p, Role.PRISONER);
                                MyListener.playerJoin((Player) p, false);
                            }
                        }
                        p.playSound(p, Sound.ENTITY_SILVERFISH_AMBIENT, 1, 0.25f);
                    }
                    if (!p.hasPotionEffect(PotionEffectType.GLOWING) && !PrisonGame.escaped.get(p) && PrisonGame.respect.get(p) == 1) {
                        PrisonGame.respect.put(p, 0);
                        PrisonGame.calls.put(p, PrisonGame.calls.getOrDefault(p, 0) + 1);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:respect");
                    }
                }
            }
        }
        if (Bukkit.getWorld("world").getTime() > 2000 && Bukkit.getWorld("world").getTime() < 4000) {
            timer1 = 2000;
            timer2 = 4000;
            bossbar.setTitle("Breakfast");
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!PrisonGame.hardmode.get(p) && !PrisonGame.escaped.get(p) && PrisonGame.roles.get(p) == Role.PRISONER)
                    p.addPotionEffect(PotionEffectType.SATURATION.createEffect(120, 0));
            }
        }
        if (Bukkit.getWorld("world").getTime() > 4000 && Bukkit.getWorld("world").getTime() < 7000) {
            timer1 = 4000;
            timer2 = 7000;
            bossbar.setTitle("Free Time");
            jobm = 1;
        }
        if (Bukkit.getWorld("world").getTime() > 7000 && Bukkit.getWorld("world").getTime() < 10000) {
            timer1 = 7000;
            timer2 = 10000;
            bossbar.setTitle("Job Time");
            jobm = 2;
        }
        if (Bukkit.getWorld("world").getTime() > 10000 && Bukkit.getWorld("world").getTime() < 13000) {
            timer1 = 10000;
            timer2 = 13000;
            bossbar.setTitle("Lunch");
            jobm = 1;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!PrisonGame.hardmode.get(p) && !PrisonGame.escaped.get(p) && PrisonGame.roles.get(p) == Role.PRISONER)
                    p.addPotionEffect(PotionEffectType.SATURATION.createEffect(120, 0));
            }
        }
        if (Bukkit.getWorld("world").getTime() > 13000 && Bukkit.getWorld("world").getTime() < 15000) {
            hasAlerted = false;
            timer1 = 13000;
            timer2 = 15000;
            bossbar.setTitle("EVENING ROLL CALL");
            jobm = 1;
        }
        if (Bukkit.getWorld("world").getTime() == 15000) {
            if (!hasAlerted) {
                hasAlerted = true;
                Double taxcount = 0.0;
                DecimalFormat numberFormat = new DecimalFormat("#0.0");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPotionEffect(PotionEffectType.GLOWING) && !PrisonGame.escaped.get(p) && PrisonGame.roles.get(p) == Role.PRISONER) {
                        Bukkit.broadcastMessage(ChatColor.RED + p.getName() + ChatColor.GOLD + " didn't come to roll call! " + ChatColor.RED + "Kill them for 100 dollars!");
                        p.sendTitle("", ChatColor.RED + "COME TO ROLL CALL NEXT TIME!", 0, 60, 0);
                        if (PrisonGame.hardmode.get(p)) {
                            p.damage(99999);
                            if (PrisonGame.roles.get(p) != Role.PRISONER) {
                                if (PrisonGame.warden != null) {
                                    if (PrisonGame.warden.equals(p)) {
                                        PrisonGame.warden = null;
                                    }
                                }
                                PrisonGame.roles.put((Player) p, Role.PRISONER);
                                MyListener.playerJoin((Player) p, false);
                            }
                        }
                        p.playSound(p, Sound.ENTITY_SILVERFISH_AMBIENT, 1, 0.25f);
                    }
                    if (!p.hasPotionEffect(PotionEffectType.GLOWING) && !PrisonGame.escaped.get(p) && PrisonGame.respect.get(p) == 1) {
                        PrisonGame.respect.put(p, 0);
                        PrisonGame.calls.put(p, PrisonGame.calls.getOrDefault(p, 0) + 1);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:respect");
                    }
                }
            }
        }
        if (Bukkit.getWorld("world").getTime() > 15000 && Bukkit.getWorld("world").getTime() < 16000) {
            timer1 = 15000;
            timer2 = 16000;
            bossbar.setTitle("Cell Time");
            jobm = 1;
        }
        if (Bukkit.getWorld("world").getTime() > 23300) {
            Bukkit.getWorld("world").setTime(0);
        }
        if (Bukkit.getWorld("world").getTime() == 16000) {
            Bukkit.broadcastMessage(ChatColor.RED + "All cells have been automatically closed! " + ChatColor.DARK_RED + "GET TO SLEEP!");
            LivingEntity le = (LivingEntity) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), -40, -58, -973), EntityType.ZOMBIE);
            le.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(999999, 255));
            le.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, 2));
            le.setPersistent(true);
            le.setInvulnerable(true);
            le.setSilent(true);
            PlayerDisguise playerDisguise = new PlayerDisguise("FreddyFazbearXXX");
            le.setCustomName("fredy");
            playerDisguise.setName("fredy");
            DisguiseAPI.disguiseEntity(le, playerDisguise);

            LivingEntity le2 = (LivingEntity) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), -40, -58, -973), EntityType.ZOMBIE);
            le2.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(999999, 255));
            le2.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, 2));
            le2.setPersistent(true);
            le2.setInvulnerable(true);
            le2.setSilent(true);
            PlayerDisguise playerDisguise2 = new PlayerDisguise("Freakedoutt");
            le2.setCustomName("boner");
            playerDisguise2.setName("boner");
            DisguiseAPI.disguiseEntity(le2, playerDisguise2);

            LivingEntity le3 = (LivingEntity) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), -40, -58, -973), EntityType.ZOMBIE);
            le3.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(999999, 255));
            le3.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, 2));
            le3.setPersistent(true);
            le3.setInvulnerable(true);
            le3.setSilent(true);
            PlayerDisguise playerDisguise3 = new PlayerDisguise("minekaufcraft");
            le3.setCustomName("chicker");
            playerDisguise3.setName("chicker");
            DisguiseAPI.disguiseEntity(le3, playerDisguise3);

            LivingEntity le4 = (LivingEntity) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), -40, -58, -973), EntityType.ZOMBIE);
            le4.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(999999, 255));
            le4.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, 2));
            le4.setPersistent(true);
            le4.setInvulnerable(true);
            le4.setSilent(true);
            PlayerDisguise playerDisguise4 = new PlayerDisguise("Paignton");
            le4.setCustomName("foxer");
            playerDisguise4.setName("foxer");
            DisguiseAPI.disguiseEntity(le4, playerDisguise4);

            switch (PrisonGame.active.getName()) {
                case "Gaeae Fort":
                    Location[] doors = {
                            new Location(Bukkit.getWorld("world"), 48, -59, -135),
                            new Location(Bukkit.getWorld("world"), 47, -59, -134),
                            new Location(Bukkit.getWorld("world"), 29, -59, -107),
                            new Location(Bukkit.getWorld("world"),  21, -60, -102),
                            new Location(Bukkit.getWorld("world"), 14, -60, -108),
                            new Location(Bukkit.getWorld("world"),  12, -60, -117),
                            new Location(Bukkit.getWorld("world"),23, -59, -120)
                    };
                    for (Location l : doors) {
                        BlockState state = l.getBlock().getState();
                        Door openable = (Door) state.getBlockData();
                        openable.setOpen(false);
                        state.setBlockData(openable);
                        state.update();
                        l.getWorld().playSound(l, Sound.BLOCK_IRON_DOOR_CLOSE, 0.75f, 0.75f);
                    }
                    break;
                case "Hypertech":
                    Location[] doors2 = {
                            new Location(Bukkit.getWorld("world"), -18, -59, -996),
                            new Location(Bukkit.getWorld("world"), -14, -59, -996),
                            new Location(Bukkit.getWorld("world"), -14, -59, -991),
                            new Location(Bukkit.getWorld("world"), -14, -59, -1006),
                            new Location(Bukkit.getWorld("world"), -18, -59, -1006),
                            new Location(Bukkit.getWorld("world"), -14, -59, -1011),
                            new Location(Bukkit.getWorld("world"), -18, -59, -1011),
                            new Location(Bukkit.getWorld("world"), -18, -52, -996),
                            new Location(Bukkit.getWorld("world"), -14, -52, -996),
                            new Location(Bukkit.getWorld("world"), -14, -52, -991)
                    };
                    for (Location l : doors2) {
                        BlockState state = l.getBlock().getState();
                        Door openable = (Door) state.getBlockData();
                        openable.setOpen(false);
                        state.setBlockData(openable);
                        state.update();
                        l.getWorld().playSound(l, Sound.BLOCK_IRON_DOOR_CLOSE, 0.75f, 0.75f);
                    }
                    break;
                case "Train":
                    Location[] doors3 = {
                            new Location(Bukkit.getWorld("world"), 92, -58, 961),
                            new Location(Bukkit.getWorld("world"), 91, -58, 961),
                            new Location(Bukkit.getWorld("world"), 92, -58, 964),
                            new Location(Bukkit.getWorld("world"), 91, -58, 964),
                            new Location(Bukkit.getWorld("world"), 92, -58, 969),
                            new Location(Bukkit.getWorld("world"), 91, -58, 969),
                            new Location(Bukkit.getWorld("world"), 92, -58, 972),
                            new Location(Bukkit.getWorld("world"), 91, -58, 972),
                            new Location(Bukkit.getWorld("world"), 92, -58, 980),
                            new Location(Bukkit.getWorld("world"), 91, -58, 980)
                    };
                    for (Location l : doors3) {
                        BlockState state = l.getBlock().getState();
                        Door openable = (Door) state.getBlockData();
                        openable.setOpen(false);
                        state.setBlockData(openable);
                        state.update();
                        l.getWorld().playSound(l, Sound.BLOCK_IRON_DOOR_CLOSE, 0.75f, 0.75f);
                    }
                    break;
                case "Gladiator":
                    Location[] doors4 = {
                            new Location(Bukkit.getWorld("world"), -2049, -60, 1950),
                            new Location(Bukkit.getWorld("world"), -2043, -60, 1950),
                            new Location(Bukkit.getWorld("world"), -2037, -60, 1950),
                            new Location(Bukkit.getWorld("world"), -2021, -60, 1950),
                            new Location(Bukkit.getWorld("world"), -2015, -60, 1950),
                            new Location(Bukkit.getWorld("world"), -2009, -60, 1950),
                            new Location(Bukkit.getWorld("world"), -2049, -60, 2002),
                            new Location(Bukkit.getWorld("world"), -2043, -60, 2002),
                            new Location(Bukkit.getWorld("world"), -2037, -60, 2002),
                            new Location(Bukkit.getWorld("world"), -2021, -60, 2002),
                            new Location(Bukkit.getWorld("world"), -2015, -60, 2002),
                            new Location(Bukkit.getWorld("world"), -2009, -60, 2002)
                    };
                    for (Location l : doors4) {
                        BlockState state = l.getBlock().getState();
                        Door openable = (Door) state.getBlockData();
                        openable.setOpen(false);
                        state.setBlockData(openable);
                        state.update();
                        l.getWorld().playSound(l, Sound.BLOCK_IRON_DOOR_CLOSE, 0.75f, 0.75f);
                    }
                    break;
                case "Island":
                    Location[] doors5 = {
                            new Location(Bukkit.getWorld("world"), 1955, -56, -1987),
                            new Location(Bukkit.getWorld("world"), 1962, -56, -1987),
                            new Location(Bukkit.getWorld("world"), 1955, -56, -1983),
                            new Location(Bukkit.getWorld("world"), 1962, -56, -1983),
                            new Location(Bukkit.getWorld("world"), 1955, -56, -1979),
                            new Location(Bukkit.getWorld("world"), 1962, -56, -1979),

                            new Location(Bukkit.getWorld("world"), 1955, -60, -1987),
                            new Location(Bukkit.getWorld("world"), 1962, -60, -1987),
                            new Location(Bukkit.getWorld("world"), 1955, -60, -1983),
                            new Location(Bukkit.getWorld("world"), 1962, -60, -1983),
                            new Location(Bukkit.getWorld("world"), 1955, -60, -1979),
                            new Location(Bukkit.getWorld("world"), 1962, -60, -1979)
                    };
                    for (Location l : doors5) {
                        BlockState state = l.getBlock().getState();
                        Door openable = (Door) state.getBlockData();
                        openable.setOpen(false);
                        state.setBlockData(openable);
                        state.update();
                        l.getWorld().playSound(l, Sound.BLOCK_IRON_DOOR_CLOSE, 0.75f, 0.75f);
                    }
                    break;
                case "Santa's Workshop":
                    Location[] doors6 = {
                            new Location(Bukkit.getWorld("world"), 1964, -60, 1931),
                            new Location(Bukkit.getWorld("world"), 1958, -60, 1931),
                            new Location(Bukkit.getWorld("world"), 1964, -60, 1926),
                            new Location(Bukkit.getWorld("world"), 1958, -60, 1926),
                            new Location(Bukkit.getWorld("world"), 1964, -60, 1921),
                            new Location(Bukkit.getWorld("world"), 1958, -60, 1921),
                            new Location(Bukkit.getWorld("world"), 1964, -60, 1916),
                            new Location(Bukkit.getWorld("world"), 1958, -60, 1916),
                            new Location(Bukkit.getWorld("world"), 1964, -60, 1911),
                            new Location(Bukkit.getWorld("world"), 1958, -60, 1911),
                            new Location(Bukkit.getWorld("world"), 1964, -60, 1906),
                            new Location(Bukkit.getWorld("world"), 1958, -60, 1906),
                            new Location(Bukkit.getWorld("world"), 1964, -60, 1901),
                            new Location(Bukkit.getWorld("world"), 1958, -60, 1901)

                    };
                    for (Location l : doors6) {
                        BlockState state = l.getBlock().getState();
                        Door openable = (Door) state.getBlockData();
                        openable.setOpen(false);
                        state.setBlockData(openable);
                        state.update();
                        l.getWorld().playSound(l, Sound.BLOCK_IRON_DOOR_CLOSE, 0.75f, 0.75f);
                    }
                    break;
                case "Volcano":
                    Location[] doors7 = {
                            new Location(Bukkit.getWorld("world"), -2019, -60, -1976),
                            new Location(Bukkit.getWorld("world"), -2020, -60, -1976)

                    };
                    for (Location l : doors7) {
                        BlockState state = l.getBlock().getState();
                        Door openable = (Door) state.getBlockData();
                        openable.setOpen(false);
                        state.setBlockData(openable);
                        state.update();
                        l.getWorld().playSound(l, Sound.BLOCK_IRON_DOOR_CLOSE, 0.75f, 0.75f);
                    }
                    break;
            }
        }
        if (Bukkit.getWorld("world").getTime() < 16000) {
            for (Entity e : Bukkit.getWorld("world").getEntities()) {
                if (e.getCustomName() != null) {
                    if (e.getCustomName().equals("fredy") || e.getCustomName().equals("boner") || e.getCustomName().equals("foxer") || e.getCustomName().equals("chicker")) {
                        e.remove();
                    }
                }
            }
        }
        if (Bukkit.getWorld("world").getTime() > 0 && Bukkit.getWorld("world").getTime() < 20) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (PrisonGame.isInside(p, new Location(Bukkit.getWorld("world"), -35, -55, -991), new Location(Bukkit.getWorld("world"), -59, -61, -968))) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:freder");
                }
            }
        }
        if (Bukkit.getWorld("world").getTime() > 16000 && Bukkit.getWorld("world").getTime() < 24000) {
            timer1 = 16000;
            timer2 = 24000;
            bossbar.setTitle("LIGHTS OUT");
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (PrisonGame.hardmode.get(p)) {
                    if (p.hasPotionEffect(PotionEffectType.LUCK)) {
                        p.addPotionEffect(PotionEffectType.LUCK.createEffect(20 * 15, 0));
                        p.addPotionEffect(PotionEffectType.BAD_OMEN.createEffect(999999, 0));
                        p.sendTitle("", ChatColor.RED + "You will be respawned at roll call!", 0, 20 * 3, 0);
                    }
                }
                if (PrisonGame.roles.get(p) == Role.PRISONER && !PrisonGame.escaped.get(p)) {
                    p.addPotionEffect(PotionEffectType.SATURATION.createEffect(20, 3));
                    p.getWorld().getWorldBorder().setWarningDistance(Integer.MAX_VALUE);
                    if (p.getWorld().getName().equals("endprison")) {
                        if (!new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1, p.getLocation().getZ()).getBlock().getType().equals(Material.JIGSAW)) {
                            if (!p.hasPotionEffect(PotionEffectType.LUCK)) {
                                p.sendTitle("", ChatColor.RED + "GET TO A CELL OR YOU'LL BE KILLED!", 0, 20 * 3, 0);
                                p.addPotionEffect(PotionEffectType.HUNGER.createEffect(200, 0));
                                p.addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0));
                                if (PrisonGame.hardmode.get(p)) {
                                    if (p.getHealth() > 1) {
                                        p.setHealth(1);
                                    }
                                }
                                Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Criminals").addPlayer(p);
                            } else {
                                Bukkit.getWorld("world").setTime(Bukkit.getWorld("world").getTime() + 2);
                                if (PrisonGame.roles.get(p) == Role.PRISONER && Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(p) == Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Criminals")) {
                                    Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Prisoners").addPlayer(p);
                                }
                                p.removePotionEffect(PotionEffectType.HUNGER);
                                p.removePotionEffect(PotionEffectType.GLOWING);
                            }
                        }
                    } else {
                        if (!p.isSleeping()) {
                            if (!p.hasPotionEffect(PotionEffectType.LUCK)) {
                                p.sendTitle("", ChatColor.RED + "GET TO SLEEP IN A BED OR YOU'LL BE KILLED!", 0, 20 * 3, 0);
                                p.addPotionEffect(PotionEffectType.HUNGER.createEffect(200, 0));
                                p.addPotionEffect(PotionEffectType.GLOWING.createEffect(20 * 30, 0));
                                if (PrisonGame.hardmode.get(p)) {
                                    if (p.getHealth() > 1) {
                                        p.setHealth(1);
                                    }
                                }
                                Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Criminals").addPlayer(p);
                                PrisonGame.respect.put(p, 0);
                            } else {
                                PrisonGame.respect.put(p, 1);
                                Bukkit.getWorld("world").setTime(Bukkit.getWorld("world").getTime() + 2);
                                if (PrisonGame.roles.get(p) == Role.PRISONER && Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(p) == Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Criminals")) {
                                    Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Prisoners").addPlayer(p);
                                }
                                p.removePotionEffect(PotionEffectType.HUNGER);
                                p.removePotionEffect(PotionEffectType.GLOWING);

                            }
                        }
                    }
                }
            }
            Bukkit.getWorld("world").setTime(Bukkit.getWorld("world").getTime() + 2);
            bossbar.setColor(BarColor.RED);
            bossbar.addFlag(BarFlag.DARKEN_SKY);
            bossbar.addFlag(BarFlag.CREATE_FOG);
        } else {
            bossbar.setColor(BarColor.WHITE);
            bossbar.removeFlag(BarFlag.DARKEN_SKY);
            if (!PrisonGame.active.getName().equals("Island"))
                bossbar.removeFlag(BarFlag.CREATE_FOG);
        }
        if (PrisonGame.active.getName().equals("Island")) {
            bossbar.addFlag(BarFlag.CREATE_FOG);
        }
        bossbar.setTitle(ChatColor.BOLD + bossbar.getTitle());
        bossbar.setProgress(Math.max(0, Math.min(1, ((float) Bukkit.getWorld("world").getTime() - (float) timer1) / ((float) timer2 - (float) timer1))));
        if (PrisonGame.active.getName().equals("Barreled")) {
            bossbar.setTitle(bossbar.getTitle() + ChatColor.GREEN + " Power: " + PrisonGame.BBpower);
            if (Bukkit.getWorld("world").getBlockAt(new Location(Bukkit.getWorld("world"),-1023,-57,-994)).getType().equals(Material.AIR)) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (PrisonGame.roles.get(p).equals(Role.PRISONER)) {
                        p.addPotionEffect(PotionEffectType.SPEED.createEffect(20*5,0));
                    }
                }
            } else {

            }
        }
        if (Bukkit.getWorld("world").getTime() == timer2) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                    p.sendTitle("", ChatColor.BOLD + bossbar.getTitle(), 20, 40, 20);
                }, 4);
                p.playSound(p, Sound.BLOCK_BELL_USE, 1, 1);
                Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                    p.playSound(p, Sound.BLOCK_BELL_USE, 1, 1);
                }, 4);
                Bukkit.getScheduler().runTaskLater(PrisonGame.getPlugin(PrisonGame.class), () -> {
                    p.playSound(p, Sound.BLOCK_BELL_USE, 1, 1);
                }, 8);
            }
        }

        var format = new DecimalFormat("#0.0");
        var timer = PrisonGame.mm.deserialize(PrisonGame.warden == null
                ? "(No Warden!)"
                : PrisonGame.wardentime.get(PrisonGame.warden) / (20 * 60) + "m");

        var wardenDisplay = PrisonGame.warden == null
                ? PrisonGame.mm.deserialize("No warden!")
                : PrisonGame.mm.deserialize(
                        "<name> <ping>\n<red>(<health> HP <aqua><damage></aqua>) for <time>",
                        Placeholder.component("damage", PrisonGame.mm.deserialize(String.format(
                                "[%ss of No-Damage]",
                                PrisonGame.warden.getNoDamageTicks() / 20
                        ))),
                        Placeholder.component("name", PrisonGame.warden.displayName()),
                        Placeholder.component("ping", PrisonGame.getPingDisplay(PrisonGame.warden)),
                        Placeholder.component("health", PrisonGame.mm.deserialize("" + Math.round(PrisonGame.warden.getHealth()))),
                        Placeholder.component("time", timer)
                );

        var guardCount = 0;
        var prisonerCount = 0;

        var guards = Component.empty();
        var prisoners = Component.empty();

        for (var player : Bukkit.getOnlinePlayers()) {
            var role = PrisonGame.roles.get(player);

            switch (role) {
                case NURSE, GUARD, SWAT -> {
                    if (!player.getPersistentDataContainer().has(VanishCommand.VANISHED)) {
                        guards = guards.append(player
                                .displayName()
                                .append(Component.space())
                                .append(PrisonGame.getPingDisplay(player))
                                .append(Component.newline()));
                    }
                    guardCount++;
                }
                case PRISONER -> {
                    if (player.isDead() || player.hasPotionEffect(PotionEffectType.LUCK)) {
                        prisoners = prisoners.append(PrisonGame.mm.deserialize(
                                "<dark_red>☠ <gray><player>\n",
                                Placeholder.component("player", player.name())
                        ));
                        prisonerCount++;
                        break;
                    }
                    if (!player.getPersistentDataContainer().has(VanishCommand.VANISHED)) {
                        prisoners = prisoners.append(player
                                .displayName()
                                .append(Component.space())
                                .append(PrisonGame.getPingDisplay(player))
                                .append(Component.newline()));
                    }
                    prisonerCount++;
                }
            }
        }

        var tab = PrisonGame.mm.deserialize("""
                <gray>---
                <brand> - <white>made by agmass!</white>
                <green>Players: <player-count></green>
                <red><warden></red>
                ---
                
                <aqua>Guards (<guard-count>):</aqua>
                
                <guards>
                
                ---
                
                <gold>Prisoners (<prisoner-count>):</gold>
                
                <prisoners>
                <padding>
                """,
                Placeholder.component("brand", Component.text(PrisonGame.brand).color(NamedTextColor.YELLOW)),
                Placeholder.component("player-count", PrisonGame.mm.deserialize("" + Bukkit.getOnlinePlayers().size())),
                Placeholder.component("warden", wardenDisplay),
                Placeholder.component("guard-count", PrisonGame.mm.deserialize("" + guardCount)),
                Placeholder.component("guards", guards),
                Placeholder.component("prisoner-count", PrisonGame.mm.deserialize("" + prisonerCount)),
                Placeholder.component("prisoners", prisoners),
                Placeholder.component("padding", PrisonGame.mm.deserialize("\n".repeat(50)))
        );
        String civs = "";

        Integer pris = 0;
        Integer guard = 0;


        for (Player p : Bukkit.getOnlinePlayers()) {
            if (PrisonGame.isInside(p, new Location(Bukkit.getWorld("world"), 1453, -41, 1533), new Location(Bukkit.getWorld("world"), 1455, -41, 1535))) {
                if (PrisonGame.warden.equals(p)) {
                    if (PrisonGame.swapcool <= 0) {
                        InventoryClickListener.switchMap(getConfig().getPrisons().get("polus"));
                    } else {
                        p.sendTitle(ChatColor.YELLOW + ("" + PrisonGame.swapcool / 20) + " seconds left.", ChatColor.RED + "That's on cooldown! ", 0, 50, 0);
                    }
                } else {
                    p.sendTitle("", ChatColor.RED + "YOU MUST BE WARDEN!", 0, 50, 0);
                }
            }

            if (PrisonGame.isInside(p, new Location(Bukkit.getWorld("world"), -2011, -53, -1929), new Location(Bukkit.getWorld("world"), -2009, -57, -1931))) {
                if (PrisonGame.warden.equals(p)) {
                    if (PrisonGame.swapcool <= 0)
                        InventoryClickListener.switchMap(getConfig().getPrisons().get("nether"));
                    else {
                        p.sendTitle(ChatColor.YELLOW + ("" + PrisonGame.swapcool / 20) + " seconds left.", ChatColor.RED + "That's on cooldown! ", 0, 50, 0);
                    }
                } else {
                    p.sendTitle("", ChatColor.RED + "YOU MUST BE WARDEN!", 0, 50, 0);
                }
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (PrisonGame.calls.getOrDefault(p, 0) >= 10) {
                PrisonGame.calls.put(p, Integer.MIN_VALUE);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:npc");
            }
            if (p.getName().contains("RennArpent")) {
                p.setDisplayName(p.getDisplayName().replace("FUNDER", "fund deez nuts"));
                p.setDisplayName(p.getDisplayName().replace("WARDEN", "nah fuck this dude"));
                p.setDisplayName(p.getDisplayName().replace("GUARD", "worst guard"));
                p.setDisplayName(p.getDisplayName().replace("NURSE", "L nurse"));
                p.setDisplayName(p.getDisplayName().replace("SWAT", "who gave him swat"));
                p.setDisplayName(p.getDisplayName().replace("PRISONER", "idiot -->"));
                p.setDisplayName(p.getDisplayName().replace("SOLITARY", "deserved"));
                p.setDisplayName(p.getDisplayName().replace("VISITOR", "i wish he was a prisoner"));
                p.setDisplayName(p.getDisplayName().replace("CRIMINAL", "*snore* mimimimimi"));
            }
            if (p.getLocation().getBlockY() == -60 && p.getLocation().clone().subtract(0.0, 1.0, 0.0).getBlock().getType() == Material.SAND && PrisonGame.active.getName().equals("Train") && PrisonGame.isInside(p, PrisonGame.nl("world", 27D, -61D, 920D, 0f, 0f), PrisonGame.nl("world", 129D, 8D, 1041D, 0f, 0f))) {
                p.damage(999);
            }
            if (p.getLocation().getBlockY() == -61 && PrisonGame.active.getName().equals("Island") && p.isInWater()) {
                p.damage(4);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:dewater");
            }
            if (p.getLocation().getBlockY() <= -53 && PrisonGame.active.getName().equals("Boat") && p.isInWater()) {
                p.damage(1);
                p.setNoDamageTicks(1);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only prison:dewater");
            }
            if (p.getLocation().getY() < 118 && p.getWorld().getName().equals("endprison")) {
                p.damage(999);
            }
            if (Keys.NIGHT_VISION.has(p)) {
                p.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(99999, 255));
            } else {
                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }
            if (p.getGameMode().equals(GameMode.SURVIVAL))
                p.setGameMode(GameMode.ADVENTURE);
            if (p.hasPotionEffect(PotionEffectType.LUCK) || p.hasPotionEffect(PotionEffectType.BAD_OMEN)) {
                if (p.getGameMode().equals(GameMode.SPECTATOR)) {
                    if (PrisonGame.warden != null) {
                        p.setSpectatorTarget(PrisonGame.warden);
                        p.sendActionBar(new TextComponent(ChatColor.GRAY + "CURRENTLY SPECTATING THE WARDEN"));
                    } else {
                        PrisonGame.tptoBed(p);
                        p.sendActionBar(new TextComponent(ChatColor.GRAY + "NO WARDEN TO SPECTATE!"));
                    }
                }
            } else {
                if (p.getGameMode().equals(GameMode.SPECTATOR)) {
                    p.setGameMode(GameMode.ADVENTURE);
                }
            }
            /*if (p.getPersistentDataContainer().getOrDefault(PrisonGame.rank, PersistentDataType.INTEGER, 0) == 1) {
                p.setCustomName(ChatColor.GRAY + "[" + ChatColor.GOLD + "SUPPORTER" + ChatColor.GRAY + "] " + p.getDisplayName());
                p.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.GOLD + "SUPPORTER" + ChatColor.GRAY + "] " + p.getDisplayName());
                p.setDisplayName(ChatColor.GRAY + "[" + ChatColor.GOLD + "SUPPORTER" + ChatColor.GRAY + "] " + p.getDisplayName());
            }*/
            if (PrisonGame.hardmode.get(p)) {
                if (p.isSprinting() && !PrisonGame.escaped.get(p) && PrisonGame.roles.get(p) == Role.PRISONER) {
                    p.setFoodLevel(p.getFoodLevel() - 1);
                    p.sendTitle("", ChatColor.RED + "You can only sprint when you've escaped! [HARD MODE]", 0, 5, 0);
                }
            }

            if (p.getInventory().getHelmet() != null) {
                if (!p.getInventory().getHelmet().getType().equals(Material.YELLOW_WOOL)) {
                    p.setMaxHealth(20);
                } else {
                    p.addPotionEffect(PotionEffectType.SPEED.createEffect(20, 2));
                }
            } else {
                p.setMaxHealth(20);
            }


            if (Keys.SEMICLOAK.has(p)) {
                Keys.MONEY.set(p, Keys.MONEY.get(p, 0.0) + 1000);
                p.sendMessage(ChatColor.RED + "Your semicloak ascension has been refunded and removed as ascensions have been removed!");
                Keys.SEMICLOAK.remove(p);
            }
            if (Keys.REINFORCEMENT.has(p)) {
                Keys.MONEY.set(p, Keys.MONEY.get(p, 0.0) + 1000);
                p.sendMessage(ChatColor.RED + "Your Reinforcement ascension has been refunded and removed as ascensions have been removed!");
                Keys.REINFORCEMENT.remove(p);
            }
            if (Keys.SPAWN_PROTECTION.has(p)) {
                Keys.MONEY.set(p, Keys.MONEY.get(p, 0.0) + 1000);
                p.sendMessage(ChatColor.RED + "Your protspawn ascension has been refunded and removed as ascensions have been removed!");
                Keys.SPAWN_PROTECTION.remove(p);
            }
            if (Keys.RANDOM_ITEMS.has(p)) {
                p.sendMessage(ChatColor.RED + "Your Random item ascension has been refunded and removed as ascensions have been removed!");
                Keys.MONEY.set(p,Keys.MONEY.get(p, 0.0) + 1000);
                Keys.RANDOM_ITEMS.remove(p);
            }
            if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
                var mainHand = p.getInventory().getItemInMainHand();
                var meta = mainHand.getItemMeta();
                if (meta != null && !meta.getDisplayName().equals(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]")) {
                    p.setWalkSpeed(0.2f);
                    p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.4);
                } else {
                    p.getInventory().getItemInMainHand().setDurability((short) 3);
                    p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(9999);
                }
            }

            if (PrisonGame.prisonerlevel.getOrDefault(p, 0) == 1) {
                if (p.isSprinting() && !PrisonGame.escaped.get(p) && PrisonGame.roles.get(p) == Role.PRISONER) {
                    p.setFoodLevel(p.getFoodLevel() - 1);
                    p.sendTitle("", ChatColor.RED + "You can only sprint when you've escaped! [F-CLASS]", 0, 5, 0);
                }
            }

            if (PrisonGame.prisonerlevel.get(p) == 1) {
                if (!p.getPlayerListName().contains("F-CLASS")) {
                    p.setCustomName(ChatColor.GRAY + "[" + ChatColor.RED + "F-CLASS" + ChatColor.GRAY + "] " + p.getDisplayName());
                    p.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.RED + "F-CLASS" + ChatColor.GRAY + "] " + p.getDisplayName());
                    p.setDisplayName(ChatColor.GRAY + "[" + ChatColor.RED + "F-CLASS" + ChatColor.GRAY + "] " + p.getDisplayName());
                }
            }

            for (Player pe : Bukkit.getOnlinePlayers()) {
                if (!p.canSee(pe) && p.isOnline()) {
                    if (!pe.isInsideVehicle() && pe.isOnline() && !pe.getPersistentDataContainer().has(VanishCommand.VANISHED)) {
                        p.showPlayer(PrisonGame.getPlugin(PrisonGame.class), pe);
                    }
                }
            }

            if (p.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
                if (p.isInsideVehicle() && p.isOnline()) {
                    if (p.getVehicle() instanceof Player) {
                        ((Player) p.getVehicle()).setCooldown(Material.IRON_SHOVEL, 20 * 5);
                        ((Player) p.getVehicle()).hidePlayer(PrisonGame.getPlugin(PrisonGame.class), p);
                    }
                }
                p.setNoDamageTicks(20);
                p.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20, 255));
            }
            if (p.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
                p.removePotionEffect(PotionEffectType.GLOWING);
            }

            if (!PrisonGame.hardmode.get(p)) {
                if (PrisonGame.api.getPlayerAdapter(Player.class).getUser(p).getCachedData().getMetaData().getPrefix() != null) {
                    if (!p.getDisplayName().contains(PrisonGame.api.getPlayerAdapter(Player.class).getUser(p).getCachedData().getMetaData().getPrefix())) {
                        p.setCustomName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(p).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] " + p.getDisplayName());
                        p.setPlayerListName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(p).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] " + p.getDisplayName());
                        p.setDisplayName(ChatColor.GRAY + "[" + PrisonGame.api.getPlayerAdapter(Player.class).getUser(p).getCachedData().getMetaData().getPrefix() + ChatColor.GRAY + "] " + p.getDisplayName());
                    }
                }
            } else {
                p.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.RED + "HARD MODE" + ChatColor.GRAY + "] " + ChatColor.GRAY + p.getName());
            }
            if (!Keys.OLD_TAB.has(p)) {
                p.sendPlayerListHeader(tab);
            } else {
                p.sendPlayerListHeaderAndFooter(Component.empty(), PrisonGame.mm.deserialize("Imagine using old tab. Actual pussy move ngl"));
            }
            /*if (p.hasPotionEffect(PotionEffectType.GLOWING)) {
                if (PrisonGame.type.get(p) == 0 && Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(p) == Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Prisoners")) {
                    Bukkit.getScoreboardManager().getMainScoreboard().getTeam("Criminals").addPlayer(p);
                }
            }*/
            bossbar.addPlayer(p);
            if (p.getInventory().getItemInMainHand().getType().equals(Material.COOKED_CHICKEN)) {
                p.kickPlayer("");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + p.getName() + " 5m Abuse of Illegals [AUTO]");
            }
            if (p.getInventory().getItemInMainHand().hasItemMeta()) {
                if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Handcuffs " + ChatColor.RED + "[CONTRABAND]")) {
                    if (PrisonGame.roles.get(p) == Role.PRISONER) {
                        p.getInventory().remove(p.getInventory().getItemInMainHand());
                    }
                }
            }
            if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.DAMAGE_ALL)) {
                if (p.getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_SWORD)) {
                    if (PrisonGame.roles.get(p) != Role.WARDEN) {
                        p.sendMessage("no illegal 4 u!");
                        p.getInventory().getItemInMainHand().removeEnchantment(Enchantment.DAMAGE_ALL);
                        p.kickPlayer("");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + p.getName() + " 5m Abuse of Illegals [AUTO]");
                    }
                }
            }
            if (p.getLocation().getBlock().getType().equals(Material.VOID_AIR)) {
                if (PrisonGame.roles.get(p).equals(0)) {
                    if (!p.hasPotionEffect(PotionEffectType.GLOWING)) {
                        Boolean yesdothat = true;
                        if (p.getInventory().getChestplate() != null) {
                            if (p.getInventory().getChestplate().getItemMeta() != null) {
                                if (p.getInventory().getChestplate().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "Cloak")) {
                                    yesdothat = false;
                                }
                            }
                        }
                        if (yesdothat) {
                            for (ItemStack i : p.getInventory()) {
                                if (i != null) {
                                    if (i.getItemMeta().getDisplayName().contains("[CONTRABAND]") || i.getType().equals(Material.STONE_SWORD) || i.getType().equals(Material.IRON_SWORD) || i.getType().equals(Material.IRON_HELMET) || i.getType().equals(Material.IRON_CHESTPLATE) || i.getType().equals(Material.IRON_LEGGINGS) || i.getType().equals(Material.IRON_BOOTS)) {
                                        if (!p.isInsideVehicle() && p.isOnline()) {
                                            p.addPotionEffect(PotionEffectType.GLOWING.createEffect(1200, 0));
                                            p.sendMessage(ChatColor.RED + "You were caught with contraband!");
                                            if (PrisonGame.prisonerlevel.getOrDefault(p, 0) == 1) {
                                                p.addPotionEffect(PotionEffectType.CONFUSION.createEffect(20 * 6, 0));
                                                p.addPotionEffect(PotionEffectType.SLOW.createEffect(20 * 6, 0));
                                                p.addPotionEffect(PotionEffectType.DARKNESS.createEffect(20 * 6, 0));
                                            }
                                            for (Player g : Bukkit.getOnlinePlayers()) {
                                                if (PrisonGame.roles.get(g) != Role.PRISONER) {
                                                    g.playSound(g, Sound.ENTITY_SILVERFISH_DEATH, 1, 0.5f);
                                                    g.sendMessage(ChatColor.RED + p.getName() + ChatColor.DARK_RED + " was caught with contraband!");
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (PrisonGame.roles.get(p) == Role.NURSE) {
                if (!p.getInventory().contains(Material.SPLASH_POTION)) {
                    ItemStack pot = new ItemStack(Material.SPLASH_POTION);
                    PotionMeta potionMeta = (PotionMeta) pot.getItemMeta();
                    potionMeta.addCustomEffect(PotionEffectType.HEAL.createEffect(10, 0), true);
                    pot.setItemMeta(potionMeta);

                    p.getInventory().addItem(pot);
                    p.setCooldown(Material.SPLASH_POTION, 40);
                }
            }
        }
        DecimalFormat numberFormat = new DecimalFormat("#0.0");
        DecimalFormat numberFormat2 = new DecimalFormat("#0.000");
        DecimalFormat whole = new DecimalFormat("#");
        if (PrisonGame.warden == null) {
            String wardentime = ChatColor.RED + "None! Use '/warden' to become the prison warden!";

            if (PrisonGame.wardenCooldown > 0) {
                wardentime = ChatColor.RED + "None! Warden command is on cooldown! " + net.md_5.bungee.api.ChatColor.GRAY + "[" + Math.round((float) PrisonGame.wardenCooldown / 20f) + "]";
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.getDisplayName().contains("ASCENDING")) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + PrisonGame.formatBalance(Keys.MONEY.get(p, 0.0)) + "$" + ChatColor.GRAY + " || " + ChatColor.GRAY + "Current Warden: " + wardentime));
                } else {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent((ChatColor.AQUA + whole.format(Keys.ASCENSION_COINS.get(p, 0)) + " ascension coins.")));
                }
            }
        } else {
            if (!PrisonGame.warden.isOnline()) {
                PrisonGame.warden = null;
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.getDisplayName().contains("ASCENDING") || !p.getGameMode().equals(GameMode.SPECTATOR)) {
                    if (p != PrisonGame.warden) {
                        Role role = PrisonGame.roles.get(p);
                        String acbar = "";
                        if (p.getDisplayName().contains("SOLITARY"))
                            acbar = "||" + ChatColor.DARK_GRAY + " SOLITARY [ " + numberFormat.format(PrisonGame.solittime.get(p) / 20) + " seconds left. ]";
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + numberFormat.format(Keys.MONEY.get(p, 0.0)) + "$" + ChatColor.GRAY + acbar + ChatColor.GRAY + " || Current Warden: " + ChatColor.DARK_RED + PrisonGame.warden.getName()));
                    } else {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + numberFormat.format(Keys.MONEY.get(p, 0.0)) + "$" + ChatColor.GRAY + " || " + ChatColor.GRAY + "Current Warden: " + ChatColor.GREEN + "You! Use \"/warden help\"!"));
                    }
                }
                if (p.getDisplayName().contains("ASCENDING")) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent((ChatColor.AQUA + "you have " + whole.format(Keys.ASCENSION_COINS.get(p, 0)) + " ascension coins.")));
                }
            }
        }
    }
}
