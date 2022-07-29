package com.gmail.necnionch.myplugin.furnacecompressor.bukkit;

import com.gmail.necnionch.myplugin.furnacecompressor.bukkit.config.PluginConfig;
import com.gmail.necnionch.myplugin.furnacecompressor.bukkit.listeners.FurnaceListener;
import com.gmail.necnionch.myplugin.furnacecompressor.bukkit.listeners.ItemListener;
import com.gmail.necnionch.myplugin.metacraftingapi.bukkit.MetaCraftingAPI;
import com.gmail.necnionch.myplugin.metacraftingapi.bukkit.recipe.CustomRecipe;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class FurnaceCompressorPlugin extends JavaPlugin {
    private final PluginConfig pluginConfig = new PluginConfig(this);
    private static FurnaceCompressorPlugin instance;
    private final NamespacedKey tierKey = new NamespacedKey(this, "tier");

    @Override
    public void onEnable() {
        instance = this;
        pluginConfig.load();
        getServer().getPluginManager().registerEvents(new FurnaceListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemListener(this), this);
        registerItems();
    }

    @Override
    public void onDisable() {
        try {
            MetaCraftingAPI.unregisterBy(this);
        } catch (Throwable e) {
            getLogger().warning("Failed to unregisters items");
        }
    }

    private void registerItems() {
        MetaCraftingAPI.registerCustomItem(this, CompressedFurnace.TIER1);
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("furn_tier1")
                .shape("iii", "ifi", "iii")
                .setIngredient('i', Material.IRON_BLOCK)
                .setIngredient('f', Material.FURNACE)
                .setResult(CompressedFurnace.TIER1, 1)
                .create());
        MetaCraftingAPI.registerCustomItem(this, CompressedFurnace.TIER2);
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("furn_tier2")
                .shape("ggg", "gfg", "ggg")
                .setIngredient('g', Material.GOLD_BLOCK)
                .setIngredient('f', CompressedFurnace.TIER1)
                .setResult(CompressedFurnace.TIER2, 1)
                .create());
        MetaCraftingAPI.registerCustomItem(this, CompressedFurnace.TIER3);
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("furn_tier3")
                .shape("ddd", "dfd", "ddd")
                .setIngredient('d', Material.DIAMOND_BLOCK)
                .setIngredient('f', CompressedFurnace.TIER2)
                .setResult(CompressedFurnace.TIER3, 1)
                .create());
        MetaCraftingAPI.registerCustomItem(this, CompressedFurnace.TIER4);
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("furn_tier4")
                .shape("nnn", "nfn", "nnn")
                .setIngredient('n', Material.NETHERITE_BLOCK)
                .setIngredient('f', CompressedFurnace.TIER3)
                .setResult(CompressedFurnace.TIER4, 1)
                .create());
    }

    private CustomRecipe.Builder makeRecipe(String key) {
        return CustomRecipe.builder(new NamespacedKey(this, key));
    }

    public static Logger getLog() {
        return instance.getLogger();
    }

    public static NamespacedKey tierKey() {
        return instance.tierKey;
    }

}
