package me.lucyy.profiles.storage;

import me.lucyy.profiles.ProFiles;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class YamlStorage implements Storage {

    private final ProFiles pl;
    private File configFile;
    private FileConfiguration config;

    private void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ConfigurationSection getPlayerSection(UUID uuid) {
        ConfigurationSection section = config.getConfigurationSection(uuid.toString());
        if (section == null) section = config.createSection(uuid.toString());
        return section;
    }

    public YamlStorage(ProFiles plugin) {
        this.pl = plugin;

        try {
            if (!pl.getDataFolder().exists()) pl.getDataFolder().mkdirs();
            configFile = new File(pl.getDataFolder(), "storage.yml");
            configFile.createNewFile();
            config = YamlConfiguration.loadConfiguration(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setField(UUID uuid, String key, String value) {
        getPlayerSection(uuid).set(key, value);
        save();
    }

    @Override
    public String getField(UUID uuid, String key) {
        return getPlayerSection(uuid).getString(key, "");
    }

    @Override
    public void close() {
        save();
    }
}
