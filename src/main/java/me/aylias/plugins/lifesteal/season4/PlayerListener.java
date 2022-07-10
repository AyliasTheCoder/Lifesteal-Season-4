package me.aylias.plugins.lifesteal.season4;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class PlayerListener implements Listener {

    @EventHandler
    public void playerKilled(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager && e.getEntity() instanceof Player player) {
            if (player.getHealth() - e.getDamage() < 0) {

                var pManager = new PlayerAttrManager(player);
                if (pManager.getMaxHealth() - 2 <= 0) {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "You are banned, because you ran out of hearts!", null, "you ran out of hearts!");
                    player.kickPlayer("You are banned, because you ran out of hearts!");
                }
                else
                    pManager.setMaxHealth(pManager.getMaxHealth() - 2);

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
    public void potionApplied(ProjectileHitEvent e) {
        if (e.getEntity() instanceof TippedArrow arrow) {
            arrow.setBasePotionData(new PotionData(PotionType.WATER));
            arrow.clearCustomEffects();
        }
    }
}
