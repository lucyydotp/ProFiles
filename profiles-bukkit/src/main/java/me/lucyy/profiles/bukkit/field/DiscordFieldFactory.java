package me.lucyy.profiles.bukkit.field;

import me.lucyy.profiles.api.FieldFactory;

import java.util.Map;

public class DiscordFieldFactory implements FieldFactory {

    @Override
    public DiscordProfileField create(String key, Map<String, Object> cfg) throws IllegalArgumentException {
        try {
            return new DiscordProfileField(key,
                    (String) cfg.get("displayName"),
                    (Integer) cfg.get("order"));
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}