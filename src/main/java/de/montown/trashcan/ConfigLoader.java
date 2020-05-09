package de.montown.trashcan;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.*;
import java.util.Objects;

public class ConfigLoader {

    private YamlConfiguration config;
    private Trashcan main;
    private File configFile;
    private String version = "1.0.0";

    public ConfigLoader(Trashcan plugin) {
        main = plugin;
        main.getDataFolder().mkdir();
        configFile = new File(main.getDataFolder(), "config.yml");
        if (!configFile.exists()) create();
        read();
        if (!Objects.requireNonNull(config.getString("Config.ConfigVersion")).equalsIgnoreCase(version))
            changeVersion();
    }

    /**
     * Reads config.yml
     */
    private void read() {
        try {
            InputStreamReader custom = new InputStreamReader(new FileInputStream(configFile), Charsets.UTF_8);

            config = YamlConfiguration.loadConfiguration(custom);

            if (config.getKeys(false).isEmpty()) {
                System.out.println("[Warnung] Max hat verkackt");
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates version - safes old version
     */
    private void changeVersion() {
        try {
            InputStream inputStream = new FileInputStream(configFile);
            OutputStream outputStream = new FileOutputStream(new File(main.getDataFolder(), "config-old.yml"));
            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        create();
    }

    /**
     * Creates or updates config.yml
     */
    private void create() {
        try {
            InputStream inputStream = main.getResource("config.yml");
            OutputStream outputStream = new FileOutputStream(configFile);
            int read;
            byte[] bytes = new byte[1024];

            assert inputStream != null;
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the Config.
     */
    public YamlConfiguration getConfig() {
        return config;
    }


}

