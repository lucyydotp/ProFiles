package me.lucyy.profiles.bukkit.field;

import me.lucyy.profiles.api.FieldFactory;

import java.util.Map;

public class PlaceholderFieldFactory implements FieldFactory {
	@Override
	public PlaceholderProfileField create(String key, Map<String, Object> cfg) throws IllegalArgumentException {
		try {
			return new PlaceholderProfileField(key,
					(String) cfg.get("displayName"),
					(Integer) cfg.get("order"),
					(String) cfg.get("format"));
		} catch (ClassCastException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}
}
