package com.gmail.necnionch.myplugin.furnacecompressor.bukkit.config;

import com.gmail.necnionch.myplugin.furnacecompressor.bukkit.CompressedFurnace;
import com.gmail.necnionch.myplugin.furnacecompressor.common.BukkitConfigDriver;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginConfig extends BukkitConfigDriver {
    public PluginConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onLoaded(FileConfiguration config) {
        if (super.onLoaded(config)) {
            loadCompressedItems();
            return true;
        }
        return false;
    }


    private void loadCompressedItems() {
        for (CompressedFurnace compressed : CompressedFurnace.ITEMS) {
            ConfigurationSection section = config.getConfigurationSection(String.format("compressed.tier%d", compressed.getTierLevel()));
            if (section != null) {
                compressed.getConfig().speed = (float) (section.getDouble("speed", 100) / 100);
                compressed.getConfig().doubleBonus = (float) (section.getDouble("double-bonus", 0) / 100);

            } else {
                compressed.getConfig().speed = 1.0f;
                compressed.getConfig().doubleBonus = 0f;
            }
        }
    }

    public static class Compressed {
        private float speed;
        private float doubleBonus;

        public Compressed() {}

        public Compressed(float speed, float doubleBonus) {
            this.speed = speed;
            this.doubleBonus = doubleBonus;
        }

        public float getSpeed() {
            return speed;
        }

        public float getDoubleBonus() {
            return doubleBonus;
        }

    }

}
