package me.lucyy.profiles.bukkit;

import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.ProFilesPlatform;
import org.bukkit.plugin.java.JavaPlugin;

public class ProFilesBukkit extends JavaPlugin {

    private ProFilesPlatform platform;
    private ProFiles plugin;

    @Override
    public void onEnable() {
        platform = new ProFilesBukkitPlatform(this);
        plugin = new ProFiles(platform);
        plugin.onEnable();
    }

    public ProFiles getPlugin() {
        return plugin;
    }
}
