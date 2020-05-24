package de.maxderverpeilte.trashcan;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import java.util.List;

public class Listener implements org.bukkit.event.Listener {

    private Trashcan main;
    private List<String> deniedworlds;
    private YamlConfiguration config;

    Listener(Trashcan trashcan) {
        main = trashcan;
        config = main.getConfigLoader().getConfig();
        main.getServer().getPluginManager().registerEvents(this, main);
        deniedworlds = config.getStringList("Config.denied-worlds");
    }

    @EventHandler
    public void onChange(InventoryMoveItemEvent event) {
        Location location = event.getDestination().getLocation();
        if (location.getBlock().getType().toString().equalsIgnoreCase(config.getString("Config.Blocks.low"))) {
            if (location.getBlock().getState() instanceof Container) {
                Container container = (Container) location.getBlock().getState();
                if (container.getCustomName() == null) return;
                if (!container.getCustomName().equalsIgnoreCase("Trashcan")) return;
                location.setY(location.getY() + 1);
                if (location.getBlock().getType().toString().equalsIgnoreCase(config.getString("Config.Blocks.top"))) {
                    Bukkit.getScheduler().runTaskLater(main, () -> event.getDestination().clear(), 1L);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(InventoryCloseEvent event) {
        Location location = event.getInventory().getLocation();
        if (location == null) {
            return;
        }
        if (location.getBlock().getType().toString().equalsIgnoreCase(config.getString("Config.Blocks.low"))) {
            if (location.getBlock().getState() instanceof Container) {
                Container container = (Container) location.getBlock().getState();
                if (container.getCustomName() == null) return;
                if (!container.getCustomName().equalsIgnoreCase("Trashcan")) return;
                location.setY(location.getY() + 1);
                if (location.getBlock().getType().toString().equalsIgnoreCase(config.getString("Config.Blocks.top"))) {
                    event.getInventory().clear();
                }
            }
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType().toString().equalsIgnoreCase(config.getString("Config.Blocks.low"))) {
            Location location = event.getBlock().getLocation();
            location.setY(location.getY() + 1);
            try {
                if (deniedworlds.contains(location.getWorld().getName())) {
                    return;
                }
            }catch (NullPointerException e){

            }
            if (location.getBlock().getType().toString().equalsIgnoreCase(config.getString("Config.Blocks.top"))) {
                if (!event.getPlayer().hasPermission("trashcan.create")) {
                    if (config.getBoolean("Config.Messages.MessagesEnabled")) {
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Config.Messages.MessageNoPermission")));
                    }
                    Container container = (Container) event.getBlock().getState();
                    container.setCustomName(null);
                    container.update();
                    return;
                }
                if (event.getBlock().getState() instanceof Container) {
                    Container inventoryblock = (Container) event.getBlock().getState();
                    inventoryblock.setCustomName("Trashcan");
                    inventoryblock.update(true);
                    if (config.getBoolean("Config.Messages.MessagesEnabled")) {
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Config.Messages.MessageCreate")));
                    }
                }
            }

        }
        if (event.getBlock().getType().toString().equalsIgnoreCase(config.getString("Config.Blocks.top"))) {
            Location location = event.getBlock().getLocation();
            location.setY(location.getY() - 1);
            if (location.getBlock().getType().toString().equalsIgnoreCase(config.getString("Config.Blocks.low"))) {
                if (!event.getPlayer().hasPermission("trashcan.create")) {
                    if (config.getBoolean("Config.Messages.MessagesEnabled")) {
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Config.Messages.MessageNoPermission")));
                    }
                    Container container = (Container) location.getBlock().getState();
                    container.setCustomName(null);
                    container.update();
                    return;
                }
                if (location.getBlock().getState() instanceof Container) {
                    Container inventoryblock = (Container) location.getBlock().getState();
                    inventoryblock.setCustomName("Trashcan");
                    inventoryblock.update(true);
                    if (config.getBoolean("Config.Messages.MessagesEnabled")) {
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Config.Messages.MessageCreate")));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Container) {
            if (event.getBlock().getType().toString().equalsIgnoreCase(config.getString("Config.Blocks.low"))) {
                Container container = (Container) event.getBlock().getState();
                if (container.getCustomName() != null) {
                    if (container.getCustomName().equalsIgnoreCase("Trashcan")) {
                        if(event.getPlayer().getGameMode() == GameMode.SURVIVAL||event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                            container.setCustomName(null);
                            container.update();
                        }
                    }
                }
            }
        }
    }
}
