package me.aylias.plugins.lifesteal.old;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftTippedArrow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Events implements Listener {

    public static List<String> deadPlayers;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        List<String> revivedList = LifeSteal.getInstance().getConfig().getStringList("Revived");
        String reviver = null;
        String wholeString = null;

        for (String uuid : revivedList) {
            String[] uuids = uuid.split("%");
            if (UUID.fromString(uuids[0]).equals(player.getUniqueId())) {
                reviver = Bukkit.getOfflinePlayer(UUID.fromString(uuids[1])).getName();
                wholeString = uuid;
                break;
            }
        }

        if (reviver != null) {
            player.setMaxHealth(8.0D);
            player.sendMessage(ChatColor.GREEN + "You were revived by " + reviver + "!");
            revivedList.remove(wholeString);
            player.getInventory().clear();
            LifeSteal.getInstance().getConfig().set("Revived", revivedList);
        }

        LifeSteal.getInstance().saveConfig();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Action act = e.getAction();
        Player player = e.getPlayer();
        Inventory playerInv = player.getInventory();
        if (e.getItem() != null && (act.equals(Action.RIGHT_CLICK_AIR) || act.equals(Action.RIGHT_CLICK_BLOCK))) {
            if (e.getItem().isSimilar(LifeSteal.getInstance().beaconOfLife) && e.getItem().getItemMeta().getDisplayName().equals(LifeSteal.getInstance().beaconOfLife.getItemMeta().getDisplayName())) {
                new GUI(player, 1);
                e.setCancelled(true);
            } else if (e.getItem().isSimilar(LifeSteal.getInstance().heart) && player.getMaxHealth() <= 38.0D) {
                for (int i = 0; i < playerInv.getSize(); ++i) {
                    if (playerInv.getItem(i) != null && playerInv.getItem(i).isSimilar(LifeSteal.getInstance().heart)) {
                        playerInv.getItem(i).setAmount(playerInv.getItem(i).getAmount() - 1);
                        break;
                    }
                }

            }
        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Material blockType = e.getBlock().getType();
        String displayName = e.getItemInHand().getItemMeta().getDisplayName();
        if (blockType.equals(Material.BEACON) && e.getItemInHand().getItemMeta() != null && displayName.equals(ChatColor.LIGHT_PURPLE + "Beacon Of Life")) {
            e.setCancelled(true);
        }

    }

//    @EventHandler
//    public void playerCraft(CraftItemEvent e) {
//        ItemStack[] inv = e.getInventory().getContents();
//        int size = inv.length;
//
//        for(int index = 0; index < size; index++) {
//            ItemStack content = inv[index];
//            if (content != null) {
//                if (content.getType().equals(Material.CARROT_ON_A_STICK)) {
//                    ItemMeta meta = content.getItemMeta();
//                    if (!meta.hasCustomModelData()) {
//                        e.setCancelled(true);
//                        break;
//                    }
//                }
//            }
//        }
//
//    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (LifeSteal.getInstance().gui != null) {
            HumanEntity var3 = e.getWhoClicked();
            if (var3 instanceof Player) {
                Player player = (Player) var3;
                Inventory playerInv = player.getInventory();
                ItemStack item = e.getCurrentItem();
                Inventory inv = e.getInventory();
                int slot = e.getRawSlot();
                if (item != null) {
                    item.getType();
                    if (inv.equals(LifeSteal.getInstance().gui)) {
                        e.setCancelled(true);
                        int page = Integer.parseInt(inv.getItem(36).getItemMeta().getLocalizedName());
                        if (slot == 36 && item.getType().equals(Material.LIME_WOOL)) {
                            new GUI(player, page - 1);
                        } else if (slot == 44 && item.getType().equals(Material.LIME_WOOL)) {
                            new GUI(player, page + 1);
                        } else if (item.getType().equals(Material.PLAYER_HEAD) || item.getType().equals(Material.LEGACY_SKULL_ITEM)) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                            List<String> deadList = LifeSteal.getInstance().getConfig().getStringList("Dead");
                            deadList.remove(offlinePlayer.getUniqueId().toString());
                            LifeSteal.getInstance().getConfig().set("Dead", deadList);
                            List<String> revivedList = LifeSteal.getInstance().getConfig().getStringList("Revived");
                            String var10001 = offlinePlayer.getUniqueId().toString();
                            revivedList.add(var10001 + "%" + player.getUniqueId().toString());
                            LifeSteal.getInstance().getConfig().set("Revived", revivedList);
                            LifeSteal.getInstance().saveConfig();
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pardon " + offlinePlayer.getName());

                            for (int i = 0; i < playerInv.getSize(); ++i) {
                                if (playerInv.getItem(i) != null && playerInv.getItem(i).isSimilar(LifeSteal.getInstance().beaconOfLife)) {
                                    playerInv.getItem(i).setAmount(playerInv.getItem(i).getAmount() - 1);
                                    break;
                                }
                            }

                            player.closeInventory();
                            ChatColor var13 = ChatColor.GREEN;
                            player.sendMessage(var13 + "Successfully revived " + offlinePlayer.getName() + "!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void entityCreated(EntitySpawnEvent e) {
        if (e.getEntity() instanceof CraftTippedArrow) {
            CraftTippedArrow arrow = (CraftTippedArrow) e.getEntity();

            arrow.setBasePotionData(new PotionData(PotionType.WATER));
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
                            return;
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
}