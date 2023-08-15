package org.plugin.quarryplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class QuarryPlugin extends JavaPlugin {

    private static QuarryPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        // Create or load configuration
        setupConfig();

        // Create the Quarry Chest Recipe
        createQuarryChestRecipe();

        // Register the BlockPlaceEvent listener
        Bukkit.getPluginManager().registerEvents(new QuarryChestPlaceListener(), this);
    }

    public static QuarryPlugin getPlugin() {
        return instance;
    }

    private void setupConfig() {
        // Creates config file if it doesn't exist, or loads it if it does
        saveDefaultConfig();
    }

    private void createQuarryChestRecipe() {
        ItemStack quarryChest = new ItemStack(Material.CHEST);

        // Set a custom name and lore to distinguish this from a regular chest
        ItemMeta meta = quarryChest.getItemMeta();
        meta.setDisplayName("Quarry Chest");

        List<String> lore = new ArrayList<>();
        lore.add("Special chest that extracts ores");
        meta.setLore(lore);

        quarryChest.setItemMeta(meta);

        // Define the recipe key and create the recipe object
        NamespacedKey key = new NamespacedKey(this, "quarry_chest");
        ShapedRecipe recipe = new ShapedRecipe(key, quarryChest);

        // Define the recipe shape and ingredients
        recipe.shape("DDD", "DCD", "DDD");
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setIngredient('C', Material.CHEST);

        // Add the recipe to the server
        Bukkit.getServer().addRecipe(recipe);
    }
}