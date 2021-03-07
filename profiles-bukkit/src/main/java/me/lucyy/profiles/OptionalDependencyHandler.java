package me.lucyy.profiles;

import me.lucyy.pronouns.api.PronounHandler;
import org.bukkit.plugin.RegisteredServiceProvider;

public class OptionalDependencyHandler {
    private final ProFiles plugin;

    public OptionalDependencyHandler(ProFiles plugin) {
        this.plugin = plugin;
    }

    public PronounHandler getProNouns() {
        if (plugin.getServer().getPluginManager().getPlugin("ProNouns") == null) return null;
        RegisteredServiceProvider<PronounHandler> handler = plugin.getServer().getServicesManager().getRegistration(PronounHandler.class);
        if (handler != null) return handler.getProvider();
        return null;
    }
}
