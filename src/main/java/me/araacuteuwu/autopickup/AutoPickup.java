package me.araacuteuwu.autopickup;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;

public class AutoPickup extends PluginBase implements Listener {

    @Override
    public void onEnable(){
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Item[] drops = event.getDrops();
        boolean inventoryFull = false;
        Item[] itemsToAdd = new Item[drops.length];
        int itemCount = 0;

        for (int i = 0; i < drops.length; i++) {
            if (player.getInventory().canAddItem(drops[i])) {
                itemsToAdd[itemCount] = drops[i];
                itemCount++;
            } else {
                inventoryFull = true;
            }
        }

        if (itemCount > 0) {
            for (int i = 0; i < itemCount; i++) {
                player.getInventory().addItem(itemsToAdd[i]);
                player.addExperience(event.getDropExp());
                event.setDropExp(0);
            }
        }

        event.setDrops(new Item[0]);

        if (inventoryFull) {
            player.sendTitle(this.getConfig().getString("full-inventory-title").replace("&", "§"));
            event.setCancelled();
        }
    }
}
