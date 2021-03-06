package me.aylias.plugins.lifesteal.old;

import me.aylias.plugins.lifesteal.season4.SpecialItems;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Date;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player dead = e.getEntity();
        Player killer = e.getEntity().getKiller();

        AttributeInstance attrDead = dead.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double baseDead = attrDead.getBaseValue();

        if (killer == null || killer.equals(dead)) {
            diedNoKiller(dead);
            return;
        }

        AttributeInstance attrKiller = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double baseKiller = attrKiller.getBaseValue();
        if (baseDead > 2.0D) {
            attrDead.setBaseValue(baseDead - 2.0D);
            if (killer != null && baseKiller <= 38.0D) {
                attrKiller.setBaseValue(baseKiller + 2.0D);
            }
        } else {
            Events.deadPlayers = LifeSteal.instance.getConfig().getStringList("Dead");
            Events.deadPlayers.add(dead.getUniqueId().toString());
            LifeSteal.getInstance().getConfig().set("Dead", Events.deadPlayers);
            LifeSteal.getInstance().saveConfig();
            if (killer != null) {
                dead.getInventory().clear();
                Bukkit.getBanList(BanList.Type.NAME).addBan(dead.getName(), ChatColor.RED + "You died because of " + ChatColor.GOLD + killer.getName() + ChatColor.RED + ".", null, dead.getName());
                ChatColor var10001 = ChatColor.RED;
                dead.kickPlayer(var10001 + "You died because of " + ChatColor.GOLD + killer.getName() + ChatColor.RED + ".");
                if (killer.getMaxHealth() <= 38.0D) {
                    killer.setMaxHealth(killer.getMaxHealth() + 2.0D);
                } else {
                    killer.getInventory().addItem(SpecialItems.HEART).forEach((index, item) -> killer.getWorld().dropItem(killer.getLocation(), item));
                }
            } else {
                Bukkit.getBanList(BanList.Type.NAME).addBan(dead.getName(), ChatColor.RED + "You died.", null, dead.getName());
                dead.kickPlayer(ChatColor.RED + "You died.");
            }
        }
    }

    public void diedNoKiller(Player dead) {
        AttributeInstance attrDead = dead.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double baseDead = attrDead.getBaseValue();
        if (baseDead > 2.0D) {
            attrDead.setBaseValue(baseDead - 2.0D);
        } else {
            Events.deadPlayers = LifeSteal.instance.getConfig().getStringList("Dead");
            Events.deadPlayers.add(dead.getUniqueId().toString());
            Bukkit.getBanList(BanList.Type.NAME).addBan(dead.getName(), ChatColor.RED + "You died.", null, dead.getName());
            dead.kickPlayer(ChatColor.RED + "You died.");
            LifeSteal.getInstance().getConfig().set("Dead", Events.deadPlayers);
        }
    }
}
