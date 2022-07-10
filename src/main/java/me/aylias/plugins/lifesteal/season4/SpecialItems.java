package me.aylias.plugins.lifesteal.season4;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpecialItems {
    public static final ItemStack HEART;
    public static final ItemStack FRAGMENT;

    static {
        HEART = new ItemStack(Material.NETHER_STAR, 1);

        var meta = HEART.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Heart");
        meta.setCustomModelData(1);

        HEART.setItemMeta(meta);
    }

    static {
        FRAGMENT = new ItemStack(Material.DIAMOND, 1);

        var meta = FRAGMENT.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Fragment");
        meta.setCustomModelData(2);

        FRAGMENT.setItemMeta(meta);
    }
}
