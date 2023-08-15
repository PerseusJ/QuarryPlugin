    package org.plugin.quarryplugin;

    import org.bukkit.Material;
    import org.bukkit.block.Block;
    import org.bukkit.block.Chest;
    import org.bukkit.event.EventHandler;
    import org.bukkit.event.Listener;
    import org.bukkit.event.block.BlockPlaceEvent;

    public class QuarryChestPlaceListener implements Listener {

        @EventHandler
        public void onBlockPlace(BlockPlaceEvent event) {
            Block blockPlaced = event.getBlockPlaced();

            if (blockPlaced.getType() == Material.CHEST) {

                // Check if the chest is a Quarry Chest by checking its name or lore
                Chest chest = (Chest) blockPlaced.getState();

                if (chest.getCustomName() != null && chest.getCustomName().equals("Quarry Chest")) {

                    // Start the quarry operation
                    QuarryTask quarryTask = new QuarryTask(blockPlaced.getLocation());
                    quarryTask.runTaskTimer(event.getPlayer().getServer().getPluginManager().getPlugin("QuarryPlugin"), 0L, 20L);
                }
            }
        }
    }