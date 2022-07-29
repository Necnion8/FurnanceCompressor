package com.gmail.necnionch.myplugin.furnacecompressor.bukkit.listeners;

import com.gmail.necnionch.myplugin.furnacecompressor.bukkit.CompressedFurnace;
import com.gmail.necnionch.myplugin.furnacecompressor.bukkit.FurnaceCompressorPlugin;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemListener implements Listener {
    private final FurnaceCompressorPlugin plugin;

    public ItemListener(FurnaceCompressorPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler(ignoreCancelled = true)
    public void onBlockDrop(BlockDropItemEvent event) {
        BlockState state = event.getBlockState();
        if (!(state instanceof Furnace))
            return;

        Furnace furnace = (Furnace) state;
        CompressedFurnace compressed = CompressedFurnace.from(furnace);
        if (compressed == null)
            return;

        List<Item> items = event.getItems();
        for (int idx = 0; idx < items.size(); idx++) {
            ItemStack item = items.get(items.size() - (idx + 1)).getItemStack();
            if (!Material.FURNACE.equals(item.getType()))
                return;

            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta instanceof BlockStateMeta) {
                BlockState blockState = ((BlockStateMeta) itemMeta).getBlockState();
                if (!(blockState instanceof Furnace))
                    continue;
                furnace = (Furnace) blockState;

                if (CompressedFurnace.from(furnace) != null) {
                    // 中身のドロップに圧縮かまどが含まれていた
                    continue;

                } else {
                    compressed.setTierFurnace(furnace);
                    ((BlockStateMeta) itemMeta).setBlockState(furnace);
                    item.setItemMeta(itemMeta);
                    CompressedFurnace.setItemId(item, compressed.getItemId());
                    break;
                }

            }
            plugin.getLogger().warning("no hit compressed furnace item");
        }
    }
}
