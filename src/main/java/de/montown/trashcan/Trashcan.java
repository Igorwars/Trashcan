package de.montown.trashcan;

import org.bukkit.plugin.java.JavaPlugin;

public final class Trashcan extends JavaPlugin {

    private ConfigLoader configLoader;

    @Override
    public void onEnable() {
        Listener listener = new Listener(this);
        configLoader = new ConfigLoader(this);
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }
}
