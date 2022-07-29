package com.gmail.necnionch.myplugin.furnacecompressor.bukkit;

import com.gmail.necnionch.myplugin.furnacecompressor.bukkit.config.PluginConfig;
import com.gmail.necnionch.myplugin.metacraftingapi.bukkit.MetaCraftingAPI;
import com.gmail.necnionch.myplugin.metacraftingapi.bukkit.item.CustomItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompressedFurnace extends CustomItem {
    private final String itemId;
    private final int tierLevel;
    private final String name;
    private final PluginConfig.Compressed config = new PluginConfig.Compressed();

    public static final CompressedFurnace TIER1 = new CompressedFurnace("compressed_furnace_tier1", 1, "かまど - Tier1");
    public static final CompressedFurnace TIER2 = new CompressedFurnace("compressed_furnace_tier2", 2, "かまど - Tier2");
    public static final CompressedFurnace TIER3 = new CompressedFurnace("compressed_furnace_tier3", 3, "かまど - Tier3");
    public static final CompressedFurnace TIER4 = new CompressedFurnace("compressed_furnace_tier4", 4, "かまど - Tier4");
    public static final CompressedFurnace[] ITEMS = { TIER1, TIER2, TIER3, TIER4 };

    public static CompressedFurnace from(@Nullable ItemStack itemStack) {
        CustomItem customItem = MetaCraftingAPI.getCustomItemByItemStack(itemStack);
        return (customItem instanceof CompressedFurnace) ? ((CompressedFurnace) customItem) : null;
    }

    public static CompressedFurnace from(String itemId) {
        if (TIER1.itemId.equalsIgnoreCase(itemId))
            return TIER1;
        if (TIER2.itemId.equalsIgnoreCase(itemId))
            return TIER2;
        if (TIER3.itemId.equalsIgnoreCase(itemId))
            return TIER3;
        if (TIER4.itemId.equalsIgnoreCase(itemId))
            return TIER4;
        return null;
    }

    public static CompressedFurnace from(Furnace furnace) {
        PersistentDataContainer data = furnace.getPersistentDataContainer();
        Integer tierLevel = data.get(FurnaceCompressorPlugin.tierKey(), PersistentDataType.INTEGER);

        if (tierLevel == null)
            return null;

        if (tierLevel == 1)
            return TIER1;
        if (tierLevel == 2)
            return TIER2;
        if (tierLevel == 3)
            return TIER3;
        if (tierLevel == 4)
            return TIER4;
        return null;
    }

    public CompressedFurnace(String itemId, int tierLevel, String name) {
        this.itemId = itemId;
        this.tierLevel = tierLevel;
        this.name = name;
    }


    @NotNull
    @Override
    public String getItemId() {
        return itemId;
    }

    public int getTierLevel() {
        return tierLevel;
    }

    public String getName() {
        return name;
    }

    @Override
    public @NotNull Material getRecipeMaterial() {
        return Material.FURNACE;
    }

    @Override
    public @NotNull ItemStack create() {
        ItemStack itemStack = new ItemStack(Material.FURNACE);
        ItemMeta meta = itemStack.getItemMeta();

        boolean apply = false;
        if (meta != null) {
//        meta.setCustomModelData(customModel);
            meta.setDisplayName(ChatColor.AQUA + "かまど " + ChatColor.GOLD + "(Tier" + tierLevel + ")");

            if (meta instanceof BlockStateMeta && ((BlockStateMeta) meta).getBlockState() instanceof Furnace) {
                BlockState furnace = ((BlockStateMeta) meta).getBlockState();
                setTierFurnace((Furnace) furnace);
                ((BlockStateMeta) meta).setBlockState(furnace);
                apply = true;
            }

            itemStack.setItemMeta(meta);
        }

        if (!apply)
            FurnaceCompressorPlugin.getLog().severe("Failed to apply furnace data");

        return itemStack;
    }


    public PluginConfig.Compressed getConfig() {
        return config;
    }


    public void setTierFurnace(Furnace furnace) {
        PersistentDataContainer data = furnace.getPersistentDataContainer();
        data.set(FurnaceCompressorPlugin.tierKey(), PersistentDataType.INTEGER, tierLevel);
    }

}
