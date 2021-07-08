package me.lucyy.profiles.bukkit;

import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.ProFilesPlatform;
import org.bukkit.plugin.java.JavaPlugin;

public class ProFilesBukkit extends JavaPlugin {

    private final ProFilesPlatform platform = new ProFilesBukkitPlatform(this);
    private ProFiles plugin;

    @Override
    public void onEnable() {
        plugin = new ProFiles(platform);
    }
}
