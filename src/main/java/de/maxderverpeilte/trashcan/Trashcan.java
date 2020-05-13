package de.maxderverpeilte.trashcan;

import org.bukkit.plugin.java.JavaPlugin;
/*
 * Copyleft MaxDerVerpeilte
 * MaxDerVerpeilte = MaxWarsNicht, DarkFusion
 */

public final class Trashcan extends JavaPlugin {

    private ConfigLoader configLoader;

    @Override
    public void onEnable() {
        configLoader = new ConfigLoader(this);
        Listener listener = new Listener(this);
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }
}
