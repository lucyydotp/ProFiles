package me.lucyy.profiles.field.factory;

import me.lucyy.profiles.ProfileManagerImpl;
import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.field.SimpleProfileField;
import org.bukkit.configuration.ConfigurationSection;

public class SimpleFieldFactory implements FieldFactory {
    private final ProfileManagerImpl manager;

    public SimpleFieldFactory(ProfileManagerImpl manager) {
        this.manager = manager;
    }

    @Override
    public SimpleProfileField create(String key, ConfigurationSection cfg) {
        return new SimpleProfileField(manager, key, cfg.getString("displayName"), cfg.getInt("order"));
    }
}