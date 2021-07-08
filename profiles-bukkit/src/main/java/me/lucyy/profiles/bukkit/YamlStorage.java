package me.lucyy.profiles.bukkit;

import me.lucyy.profiles.storage.Storage;
import me.lucyy.squirtgun.util.UuidUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class YamlStorage implements Storage {

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

    public YamlStorage(ProFilesBukkit plugin) {
        try {
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
            configFile = new File(plugin.getDataFolder(), "storage.yml");
            boolean isNew = configFile.createNewFile();
            config = YamlConfiguration.loadConfiguration(configFile);
            if (isNew) setField(UuidUtils.fromString("7bc5888e-02bd-48fd-a177-0c46632e14c3"),
                    "subtitle", "ProFiles Developer");
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
        return getPlayerSection(uuid).getString(key, "Unset");
    }

	@Override
	public void clearField(UUID uuid, String key) {
		getPlayerSection(uuid).set(key, null);
		save();
	}

	@Override
    public void close() {
        save();
    }
}
