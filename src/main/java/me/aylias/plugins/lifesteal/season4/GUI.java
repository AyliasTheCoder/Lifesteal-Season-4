/* Decompiler 30ms, total 1372ms, lines 77 */
package me.aylias.plugins.lifesteal.season4;

import me.aylias.plugins.lifesteal.old.LifeSteal;
import me.aylias.plugins.lifesteal.old.PageUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class GUI {

   public GUI(Player p, int page) {
      Main.instance.gui = Bukkit.createInventory(null, 45, ChatColor.BLUE + "Choose Who To Revive");
      List<ItemStack> allItems = new ArrayList();

      for (var entry : Bukkit.getBanList(BanList.Type.NAME).getBanEntries()) {
         allItems.add(this.getPlayerHead(Bukkit.getOfflinePlayer(entry.getTarget())));
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
      Main.getInstance().gui.setItem(36, left);
      Main.getInstance().gui.setItem(44, right);

      for (ItemStack item : PageUtil.getPageItems(allItems, page, 43)) {
         Main.instance.gui.setItem(Main.instance.gui.firstEmpty(), item);
      }

      p.openInventory(Main.getInstance().gui);
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
