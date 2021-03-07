package me.lucyy.profiles.field.factory;

import me.lucyy.profiles.OptionalDependencyHandler;
import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.field.ProNounsProfileField;
import org.bukkit.configuration.ConfigurationSection;

public class ProNounsFieldFactory implements FieldFactory {

    private final OptionalDependencyHandler handler;

    public ProNounsFieldFactory(OptionalDependencyHandler handler) {
        this.handler = handler;
    }

    @Override
    public ProNounsProfileField create(String key, ConfigurationSection cfg) {
        return new ProNounsProfileField(key, cfg.getString("displayName"), handler.getProNouns());
    }
}
