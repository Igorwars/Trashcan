package de.montown.trashcan;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class Listener implements org.bukkit.event.Listener {

    private Trashcan main;

    Listener(Trashcan trashcan) {
        main = trashcan;
        main.getServer().getPluginManager().registerEvents(this,main);
    }

    @EventHandler
    public void onChange(InventoryMoveItemEvent event){
        Location location = event.getDestination().getLocation();
        if(location.getBlock().getType().toString().equalsIgnoreCase(main.getConfigLoader().getConfig().getString("Config.Blocks.low"))){
            location.setY(location.getY()+1);
            if(location.getBlock().getType().toString().equalsIgnoreCase(main.getConfigLoader().getConfig().getString("Config.Blocks.top"))){
                Bukkit.getScheduler().runTaskLater(main, () -> event.getDestination().clear(),1L);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(InventoryCloseEvent event){
       Location location = event.getInventory().getLocation();
       if(location == null){
           return;
       }
        if(location.getBlock().getType().toString().equalsIgnoreCase(main.getConfigLoader().getConfig().getString("Config.Blocks.low"))){
            location.setY(location.getY()+1);
            if(location.getBlock().getType().toString().equalsIgnoreCase(main.getConfigLoader().getConfig().getString("Config.Blocks.top"))){
                event.getInventory().clear();
            }
        }
    }
}
