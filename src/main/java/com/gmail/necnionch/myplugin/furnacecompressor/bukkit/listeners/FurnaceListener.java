package com.gmail.necnionch.myplugin.furnacecompressor.bukkit.listeners;

import com.gmail.necnionch.myplugin.furnacecompressor.bukkit.CompressedFurnace;
import com.gmail.necnionch.myplugin.furnacecompressor.bukkit.FurnaceCompressorPlugin;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FurnaceListener implements Listener {
    private final FurnaceCompressorPlugin plugin;
    private static final Random RAND = new Random();

    public FurnaceListener(FurnaceCompressorPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler(ignoreCancelled = true)
    public void onStartSmelt(FurnaceStartSmeltEvent event) {
        if (!(event.getBlock().getState() instanceof Furnace))
            return;

        Furnace furnace = (Furnace) event.getBlock().getState();
        CompressedFurnace compressed = CompressedFurnace.from(furnace);
        if (compressed == null)
            return;

        int cookTimeTotal = event.getTotalCookTime();
        float speed = compressed.getConfig().getSpeed();

        int newCookTime = Math.round(cookTimeTotal / speed);
        event.setTotalCookTime(newCookTime);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSmelt(FurnaceSmeltEvent event) {
        if (!(event.getBlock().getState() instanceof Furnace))
            return;

        Furnace furnace = (Furnace) event.getBlock().getState();
        CompressedFurnace compressed = CompressedFurnace.from(furnace);
        if (compressed == null)
            return;

        float doubleChance = compressed.getConfig().getDoubleBonus();

        if (doubleChance <= 0 || doubleChance < RAND.nextFloat())
            return;

        ItemStack result = event.getResult();
        result.setAmount(result.getAmount() + 1);
    }

}
