package me.lucyy.profiles.field.factory;

import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.field.PlaceholderProfileField;
import org.bukkit.configuration.ConfigurationSection;

public class PlaceholderFieldFactory implements FieldFactory {
	@Override
	public ProfileField create(String key, ConfigurationSection cfg) throws IllegalArgumentException {
		return new PlaceholderProfileField(key, cfg.getString("displayName"), cfg.getString("format"));
	}
}
