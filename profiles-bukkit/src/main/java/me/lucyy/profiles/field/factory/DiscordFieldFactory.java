package me.lucyy.profiles.field.factory;

import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.field.DiscordProfileField;
import org.bukkit.configuration.ConfigurationSection;

public class DiscordFieldFactory implements FieldFactory {

    @Override
    public DiscordProfileField create(String key, ConfigurationSection cfg) {
        return new DiscordProfileField(key, cfg.getString("displayName"), cfg.getInt("order"));
    }
}