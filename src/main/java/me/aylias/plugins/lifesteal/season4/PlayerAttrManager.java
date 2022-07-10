package me.aylias.plugins.lifesteal.season4;

import org.bukkit.entity.Player;

public class PlayerAttrManager {

    private final Player player;

    public PlayerAttrManager(Player player) {
        this.player = player;
    }

    public void setHealth(double health) {
        player.setHealth(health);
    }

    public void setMaxHealth(double maxHealth) {
        player.setMaxHealth(maxHealth);
    }

    public void setFoodLevel(int foodLevel) {
        player.setFoodLevel(foodLevel);
    }

    public void setSaturation(float saturation) {
        player.setSaturation(saturation);
    }

    public void setExhaustion(float exhaustion) {
        player.setExhaustion(exhaustion);
    }

    public void setExp(float exp) {
        player.setExp(exp);
    }

    public void setLevel(int level) {
        player.setLevel(level);
    }

    public double getMaxHealth() {
        return player.getMaxHealth();
    }
}
