package me.aylias.plugins.lifesteal.season4;

import me.aylias.plugins.lifesteal.old.LifeSteal;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerListener implements Listener {

    @EventHandler
    public void playerKilled(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager && e.getEntity() instanceof Player player) {
            if (player.getHealth() - e.getDamage() < 0) {

                var pManager = new PlayerAttrManager(player);
                if (pManager.getMaxHealth() - 2 <= 0) {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "You are banned, because you ran out of hearts!", null, "you ran out of hearts!");
                    player.kickPlayer("You are banned, because you ran out of hearts!");
                } else pManager.setMaxHealth(pManager.getMaxHealth() - 2);

                var dManager = new PlayerAttrManager(damager);
                if (dManager.getMaxHealth() + 2 <= 40) {
                    dManager.setMaxHealth(dManager.getMaxHealth() + 2);
                } else {
                    damager.getInventory().addItem(SpecialItems.HEART).forEach((index, item) -> damager.getWorld().dropItem(damager.getLocation(), item));
                }
            }
        }
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        if (e.getItem() != null) {
            if (e.getItem().isSimilar(SpecialItems.HEART)) {
                if (e.getItem().getItemMeta().getCustomModelData() == SpecialItems.HEART.getItemMeta().getCustomModelData()) {
                    var pManager = new PlayerAttrManager(e.getPlayer());
                    if (pManager.getMaxHealth() + 2 <= 40) {
                        pManager.setMaxHealth(pManager.getMaxHealth() + 2);
                        e.getItem().setAmount(e.getItem().getAmount() - 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        if (getOverMax(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryMove(InventoryMoveItemEvent e) {
        if (e.getSource().getHolder() instanceof Player player) {
            if (getOverMax(player)) {
                e.setCancelled(true);
            }
        }
    }

    private boolean getOverMax(Player player) {
        AtomicInteger goldenApples = new AtomicInteger();
        AtomicInteger enderPearls = new AtomicInteger();

        Arrays.asList(player.getInventory().getContents()).forEach(item -> {
            if (item != null) {
                if (item.getType().equals(Material.GOLDEN_APPLE)) {
                    goldenApples.addAndGet(item.getAmount());
                }

                if (item.getType().equals(Material.ENDER_PEARL)) {
                    enderPearls.addAndGet(item.getAmount());
                }
            }
        });

        return goldenApples.get() > 16 || enderPearls.get() > 5;
    }

    @EventHandler
    public void playerEnchangt(EnchantItemEvent e) {
        if (e.getItem().getType().equals(Material.BOW)) {
            e.getEnchantsToAdd().forEach((enchantment, level) -> {
                if (enchantment.equals(Enchantment.ARROW_DAMAGE)) {
                    if (level > 3) {
                        e.setCancelled(true);
                    }
                }
            });
        }

        if (e.getItem().getType().equals(Material.NETHERITE_AXE)) {
            e.getEnchantsToAdd().forEach((enchantment, level) -> {
                if (enchantment.equals(Enchantment.DAMAGE_ALL)) {
                    if (level > 3) {
                        e.setCancelled(true);
                    }
                }
            });
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (Main.getInstance().getConfig().contains("unbanned-players." + e.getPlayer().getName())) {
            Main.getInstance().getConfig().set("unbanned-players." + e.getPlayer().getName(), null);
            Main.getInstance().saveConfig();
            e.getPlayer().setMaxHealth(8);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (Main.getInstance().gui != null) {
            HumanEntity var3 = e.getWhoClicked();
            if (var3 instanceof Player) {
                Player player = (Player) var3;
                Inventory playerInv = player.getInventory();
                ItemStack item = e.getCurrentItem();
                Inventory inv = e.getInventory();
                int slot = e.getRawSlot();
                if (item != null) {
                    if (inv.equals(Main.getInstance().gui)) {
                        if (item.getType().equals(Material.PLAYER_HEAD)) {
                            var head = (SkullMeta) item.getItemMeta();
                            Bukkit.getBanList(BanList.Type.NAME).getBanEntries().removeIf(banEntry -> banEntry.getTarget().equals(head.getOwner()));
                            Main.getInstance().getConfig().set("unbanned-players." + head.getOwningPlayer().getName(), 1);
                            Main.getInstance().saveConfig();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void playerCraft(CraftItemEvent e) {
        if (e.getRecipe().getResult().isSimilar(LifeSteal.getInstance().heart)) {
            Arrays.asList(e.getInventory().getMatrix()).forEach(item -> {
                if (item != null) {
                    if (item.getType().equals(Material.DIAMOND)) {
                        ItemMeta meta = item.getItemMeta();
                        if (!meta.getDisplayName().equalsIgnoreCase(LifeSteal.getInstance().heartFragment.getItemMeta().getDisplayName())) {
                            e.setCancelled(true);
                        }
                    }
                }
            });

            return;
        }

        if (e.getRecipe().getResult().isSimilar(LifeSteal.getInstance().beaconOfLife)) {
            Arrays.asList(e.getInventory().getMatrix()).forEach(item -> {
                if (item != null) {
                    if (item.getType().equals(Material.NETHER_STAR)) {
                        ItemMeta meta = item.getItemMeta();
                        if (!meta.getDisplayName().equalsIgnoreCase(LifeSteal.getInstance().heart.getItemMeta().getDisplayName())) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            });
        }
    }
}
