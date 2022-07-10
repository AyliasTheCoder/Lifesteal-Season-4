/* Decompiler 30ms, total 1372ms, lines 77 */
package me.aylias.plugins.lifesteal.old;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GUI {

   public GUI(Player p, int page) {
      LifeSteal.instance.gui = Bukkit.createInventory((InventoryHolder)null, 45, ChatColor.BLUE + "Choose Who To Revive");
      List<ItemStack> allItems = new ArrayList();

      for (String uuid : LifeSteal.getInstance().getConfig().getStringList("Dead")) {
         allItems.add(this.getPlayerHead(Bukkit.getOfflinePlayer(UUID.fromString(uuid))));
      }

      ItemStack left;
      ItemMeta leftMeta;
      if (PageUtil.isPageValid(allItems, page - 1, 43)) {
         left = new ItemStack(Material.LIME_WOOL);
         leftMeta = left.getItemMeta();
         leftMeta.setDisplayName(ChatColor.GREEN + "Next Page");
      } else {
         left = new ItemStack(Material.RED_WOOL);
         leftMeta = left.getItemMeta();
         leftMeta.setDisplayName(ChatColor.RED + "No More Pages");
      }

      leftMeta.setLocalizedName(page + "");
      left.setItemMeta(leftMeta);
      ItemStack right;
      ItemMeta rightMeta;
      if (PageUtil.isPageValid(allItems, page + 1, 43)) {
         right = new ItemStack(Material.LIME_WOOL);
         rightMeta = right.getItemMeta();
         rightMeta.setDisplayName(ChatColor.GREEN + "Previous Page");
      } else {
         right = new ItemStack(Material.RED_WOOL);
         rightMeta = right.getItemMeta();
         rightMeta.setDisplayName(ChatColor.RED + "No More Pages");
      }

      right.setItemMeta(rightMeta);
      LifeSteal.getInstance().gui.setItem(36, left);
      LifeSteal.getInstance().gui.setItem(44, right);
      Iterator var8 = PageUtil.getPageItems(allItems, page, 43).iterator();

      while(var8.hasNext()) {
         ItemStack item = (ItemStack)var8.next();
         LifeSteal.instance.gui.setItem(LifeSteal.instance.gui.firstEmpty(), item);
      }

      p.openInventory(LifeSteal.getInstance().gui);
   }

   public ItemStack getPlayerHead(OfflinePlayer player) {
      ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
      SkullMeta meta = (SkullMeta)item.getItemMeta();
      meta.setOwningPlayer(player);
      ChatColor var10001 = ChatColor.GOLD;
      meta.setDisplayName(var10001 + player.getName());
      item.setItemMeta(meta);
      return item;
   }
}
