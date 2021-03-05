package me.lucyy.profiles;

import me.lucyy.pronouns.api.PronounHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class OptionalDependencyHandler {
    private ProFiles plugin;

    public OptionalDependencyHandler(ProFiles plugin) {
        this.plugin = plugin;
    }

    public PronounHandler getProNouns() {
        RegisteredServiceProvider<PronounHandler> handler = plugin.getServer().getServicesManager().getRegistration(PronounHandler.class);
        if (handler != null) return handler.getProvider();
        return null;
    }
}
