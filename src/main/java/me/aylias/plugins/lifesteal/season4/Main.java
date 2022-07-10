package me.aylias.plugins.lifesteal.season4;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        makeHeartRecipe();
        makeFragmentRecipe();

        getLogger().info("Lifesteal Season 4 has been enabled!");
    }

    public void makeHeartRecipe() {
        // create a NamespacedKey for your recipe
        NamespacedKey key = new NamespacedKey(this, "heart");

        // Create our custom recipe variable
        ShapedRecipe recipe = new ShapedRecipe(key, SpecialItems.HEART);

        // Here we will set the places. E and S can represent anything, and the letters can be anything. Beware; this is case sensitive.
        recipe.shape(
                "DBD",
                "BNB",
                "DBD"
        );

        // Set what the letters represent.
        // E = Emerald, S = Stick
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setIngredient('B', Material.DIAMOND_BLOCK);
        recipe.setIngredient('N', Material.NETHERITE_INGOT);

        // Finally, add the recipe to the bukkit recipes
        Bukkit.addRecipe(recipe);
    }

    public void makeFragmentRecipe() {
        // create a NamespacedKey for your recipe
        NamespacedKey key = new NamespacedKey(this, "fragment");

        // Create our custom recipe variable
        ShapedRecipe recipe = new ShapedRecipe(key, SpecialItems.FRAGMENT);

        // Here we will set the places. E and S can represent anything, and the letters can be anything. Beware; this is case sensitive.
        recipe.shape(
                "RRR",
                "RTR",
                "RRR"
        );

        // Set what the letters represent.
        // E = Emerald, S = Stick
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);

        // Finally, add the recipe to the bukkit recipes
        Bukkit.addRecipe(recipe);
    }

    @Override
    public void onDisable() {
        getLogger().info("Lifesteal Season 4 has been disabled!");
    }
}
