package me.lucyy.profiles.field.factory;

import me.lucyy.profiles.ProfileManagerImpl;
import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.field.SimpleProfileField;
import org.bukkit.configuration.ConfigurationSection;

public class SimpleFieldFactory implements FieldFactory {
    private final ProfileManagerImpl manager;

    public SimpleFieldFactory(ProfileManagerImpl manager) {
        this.manager = manager;
    }

    @Override
    public ProfileField create(ConfigurationSection cfg) {
        return new SimpleProfileField(manager, cfg.getString("key"), cfg.getString("displayName"));
    }
}
