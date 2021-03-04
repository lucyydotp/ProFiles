package me.lucyy.profiles;

import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.command.ProfileCommand;
import me.lucyy.profiles.command.SetFieldCommand;
import me.lucyy.profiles.storage.Storage;
import me.lucyy.profiles.storage.YamlStorage;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProFiles extends JavaPlugin {

    private final OptionalDependencyHandler depHandler = new OptionalDependencyHandler(this);
    private ProfileManagerImpl profileManager;
    private Storage storage;

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onEnable() {
        storage = new YamlStorage(this);
        profileManager = new ProfileManagerImpl(this, storage);

        getServer().getServicesManager().register(ProfileManager.class, profileManager, this, ServicePriority.Normal);
        getCommand("profile").setExecutor(new ProfileCommand(profileManager));
        getCommand("setfield").setExecutor(new SetFieldCommand(profileManager));
    }

    @Override
    public void onDisable() {
        storage.close();
    }

    public OptionalDependencyHandler getDepHandler() {
        return depHandler;
    }
}
