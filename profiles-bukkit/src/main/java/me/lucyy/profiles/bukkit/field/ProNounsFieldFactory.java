package me.lucyy.profiles.bukkit.field;

import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.pronouns.api.PronounHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Map;

public class ProNounsFieldFactory implements FieldFactory {

    private final PronounHandler handler;

    public ProNounsFieldFactory() {
        if (Bukkit.getServer().getPluginManager().getPlugin("ProNouns") == null) {
            handler = null;
        } else {
            RegisteredServiceProvider<PronounHandler> provider = Bukkit.getServicesManager().getRegistration(PronounHandler.class);
            handler = provider == null ? null : provider.getProvider();
        }
    }

    @Override
    public ProfileField create(String key, Map<String, Object> cfg) throws IllegalArgumentException {
        try {
            return new ProNounsProfileField(key,
                    (String) cfg.get("displayName"),
                    (Integer) cfg.get("order"),
                    handler);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
