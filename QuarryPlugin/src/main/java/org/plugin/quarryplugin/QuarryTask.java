package org.plugin.quarryplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class QuarryTask extends BukkitRunnable {

    private final Location chestLocation;
    private long nextOreTime;

    public QuarryTask(Location chestLocation) {
        this.chestLocation = chestLocation;
        this.nextOreTime = 0;
    }

    public void run() {
        long currentTime = System.currentTimeMillis();

        if (currentTime >= nextOreTime) {
            quarryArea();
        }
    }

    private void quarryArea() {
        int startX = chestLocation.getBlockX() - 4;
        int startY = chestLocation.getBlockY() - 1;
        int startZ = chestLocation.getBlockZ() - 4;

        int orePerTransfer = QuarryPlugin.getPlugin().getConfig().getInt("ore_per_transfer", 1);
        int oresProcessed = 0;

        for (int x = 0; x < 8 && oresProcessed < orePerTransfer; x++) {
            for (int y = startY; y >= -64 && oresProcessed < orePerTransfer; y--) {
                for (int z = 0; z < 8 && oresProcessed < orePerTransfer; z++) {
                    Block block = chestLocation.getWorld().getBlockAt(startX + x, y, startZ + z);

                    if (isOre(block.getType())) {
                        quarryOre(block);
                        oresProcessed++;
                    }
                }
            }
        }

        int transferTime = QuarryPlugin.getPlugin().getConfig().getInt("ore_transfer_time", 2);
        nextOreTime = System.currentTimeMillis() + (transferTime * 1000);
    }

    private void quarryOre(Block block) {
        Material material = block.getType();

        new BukkitRunnable() {
            @Override
            public void run() {
                Block chestBlock = chestLocation.getBlock();

                if (chestBlock.getState() instanceof Chest) {
                    Chest chest = (Chest) chestBlock.getState();
                    ItemStack item = new ItemStack(material);

                    org.bukkit.inventory.Inventory inventory = chest.getInventory();

                    HashMap<Integer, ItemStack> remaining = inventory.addItem(item);

                    for (ItemStack remainingItem : remaining.values()) {
                        chestLocation.getWorld().dropItemNaturally(chestLocation, remainingItem);
                    }

                    block.setType(Material.AIR);
                }
            }
        }.runTaskLater(QuarryPlugin.getPlugin(QuarryPlugin.class), 40L); // 40 ticks (2 seconds)
    }

    private boolean isOre(Material material) {
        return material == Material.COAL_ORE
                || material == Material.IRON_ORE
                || material == Material.COPPER_ORE
                || material == Material.GOLD_ORE
                || material == Material.REDSTONE_ORE
                || material == Material.DIAMOND_ORE
                || material == Material.LAPIS_ORE
                || material == Material.NETHER_QUARTZ_ORE
                || material == Material.NETHER_GOLD_ORE
                || material == Material.ANCIENT_DEBRIS
                || material == Material.DEEPSLATE_COAL_ORE
                || material == Material.DEEPSLATE_IRON_ORE
                || material == Material.DEEPSLATE_COPPER_ORE
                || material == Material.DEEPSLATE_GOLD_ORE
                || material == Material.DEEPSLATE_REDSTONE_ORE
                || material == Material.DEEPSLATE_DIAMOND_ORE
                || material == Material.DEEPSLATE_LAPIS_ORE;
    }
}