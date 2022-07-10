/* Decompiler 36ms, total 266ms, lines 93 */
package me.aylias.plugins.lifesteal.old;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class LifeSteal extends JavaPlugin implements Listener {
    public static LifeSteal instance;
    public ItemStack beaconOfLife;
    public ItemStack heart;
    public ItemStack heartFragment;
    Inventory gui;

    public static long filesCompareByByte(Path path1, Path path2) throws IOException {
        try (BufferedInputStream fis1 = new BufferedInputStream(new FileInputStream(path1.toFile()));
             BufferedInputStream fis2 = new BufferedInputStream(new FileInputStream(path2.toFile()))) {

            int ch = 0;
            long pos = 1;
            while ((ch = fis1.read()) != -1) {
                if (ch != fis2.read()) {
                    return pos;
                }
                pos += 1;
            }
            if (fis2.read() == -1) {
                return -1;
            } else {
                return pos;
            }
        }
    }

    public static LifeSteal getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();
        this.beaconOfLife = new ItemStack(Material.BEACON);
        ItemMeta beaconOfLifeMeta = this.beaconOfLife.getItemMeta();
        beaconOfLifeMeta.setDisplayName(ChatColor.GOLD + "Beacon of Life");

        this.beaconOfLife.setItemMeta(beaconOfLifeMeta);
        this.heart = new ItemStack(Material.NETHER_STAR);
        ItemMeta heartMeta = this.heart.getItemMeta();
        heartMeta.setDisplayName(ChatColor.DARK_RED + "Heart");
        this.heart.setItemMeta(heartMeta);

        this.heartFragment = new ItemStack(Material.DIAMOND);
        ItemMeta heartFragmentMeta = this.heartFragment.getItemMeta();
        heartFragmentMeta.setDisplayName(ChatColor.RED + "Heart Fragment");

        this.heartFragment.setItemMeta(heartFragmentMeta);
        new Recipes(this);
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);

        getCommand("withdraw").setExecutor(this);
        getCommand("heartadd").setExecutor(this);


        Update();
    }

    private void Update() {
        try {
            InputStream in = new URL("https://raw.githubusercontent.com/AyliasTheCoder/Lifesteal-Season-4/master/out/artifacts/Lifesteal_jar/Lifesteal.jar").openStream();
            Files.copy(in, Paths.get("plugins/aLifeSteal.jar"), StandardCopyOption.REPLACE_EXISTING);

            if (filesCompareByByte(Path.of("plugins/aLifeSteal.jar"), Path.of("plugins/LifeSteal.jar")) != -1) {
                getLogger().log(Level.INFO, "They do be different tho");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        getServer().getPluginManager().disablePlugin(instance);
                        try {
                            Files.copy(Path.of("plugins/aLifeSteal.jar"), Path.of("plugins/LifeSteal.jar"),
                                    StandardCopyOption.REPLACE_EXISTING);
                            new File("plugins/aLifeSteal.jar").delete();
                        } catch (IOException e) {
                        }
                        Bukkit.dispatchCommand(getServer().getConsoleSender(), "restart");
                    }
                }.runTaskLater(this, 200);
            } else {
                new File("plugins/aLifeSteal.jar").delete();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("withdraw") && sender instanceof Player player && args.length == 1) {
            boolean var6 = false;

            int hearts;
            try {
                hearts = Integer.parseInt(args[0]);
            } catch (NumberFormatException var10) {
                player.sendMessage(ChatColor.RED + "(!) Please enter a valid number");
                return false;
            }

            if (hearts > 0) {
                int playerHearts = (int) player.getMaxHealth() - (hearts * 2);
                if (playerHearts > 0) {
                    player.setMaxHealth(playerHearts);
                    int i;
                    if (player.getInventory().firstEmpty() != -1) {
                        for (i = 0; i < hearts; ++i) {
                            player.getInventory().addItem(getInstance().heart);
                        }
                    } else {
                        for (i = 0; i < hearts; ++i) {
                            player.getWorld().dropItemNaturally(player.getLocation(), getInstance().heart);
                        }
                    }

                    player.sendMessage(ChatColor.GOLD + "(!) Successfully withdrew hearts!");
                } else {
                    player.sendMessage(ChatColor.RED + "(!) You don't have that many hearts!");
                }
            } else if (hearts == 0) {
                player.sendMessage(ChatColor.GOLD + "(!) You can't withdraw 0 hearts!");
            } else {
                player.sendMessage(ChatColor.RED + "(!) You can't withdraw a negative number of hearts!");
            }
        }

        if (label.equalsIgnoreCase("heartadd")) {
            if (!sender.equals(Bukkit.getConsoleSender())) {
                return true;
            }
            if (sender instanceof Player player) {
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(getInstance().heart);
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(), getInstance().heart);
                }

                player.sendMessage(ChatColor.GOLD + "(!) Successfully added a heart!");
            }


        }

        return true;
    }
}
