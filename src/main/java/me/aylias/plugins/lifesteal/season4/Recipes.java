/* Decompiler 3ms, total 153ms, lines 28 */
package me.aylias.plugins.lifesteal.season4;

import me.aylias.plugins.lifesteal.old.LifeSteal;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class Recipes {

    public static void makeRecipes(Main main) {
        ShapedRecipe heartFragment = new ShapedRecipe(new NamespacedKey(main, "heartfragment"), main.heartFragment);
        heartFragment.shape(
                "RRR",
                "RTR",
                "RRR");
        heartFragment.setIngredient('R', Material.REDSTONE_BLOCK);
        heartFragment.setIngredient('T', Material.TOTEM_OF_UNDYING);
        ShapedRecipe heart = new ShapedRecipe(new NamespacedKey(main, "heart"), main.heart);
        heart.shape(
                "DBD",
                "BNB",
                "DBD");
        heart.setIngredient('D', Material.DIAMOND);
        heart.setIngredient('B', Material.DIAMOND_BLOCK);
        heart.setIngredient('N', Material.NETHERITE_INGOT);
        ShapedRecipe beaconOfLife = new ShapedRecipe(new NamespacedKey(main, "beaconoflife"), main.beaconOfLife);
        beaconOfLife.shape(
                "HHH",
                "HCH",
                "OOO");
        beaconOfLife.setIngredient('H', Material.NETHER_STAR);
        beaconOfLife.setIngredient('C', Material.RECOVERY_COMPASS);
        beaconOfLife.setIngredient('O', Material.OBSIDIAN);
        main.getServer().addRecipe(heartFragment);
        main.getServer().addRecipe(heart);
        main.getServer().addRecipe(beaconOfLife);
    }
}
