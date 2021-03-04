package me.lucyy.profiles.field.factory;

import me.lucyy.profiles.OptionalDependencyHandler;
import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.field.ProNounsProfileField;
import org.bukkit.configuration.ConfigurationSection;

public class ProNounsFieldFactory implements FieldFactory {

    private OptionalDependencyHandler handler;

    public ProNounsFieldFactory(OptionalDependencyHandler handler) {
        this.handler = handler;
    }

    @Override
    public ProfileField create(ConfigurationSection cfg) {
        return new ProNounsProfileField(cfg.getString("key"), cfg.getString("displayName"), handler.getProNouns());
    }
}
