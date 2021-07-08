package me.lucyy.profiles.field;

import me.lucyy.profiles.ProfileManagerImpl;
import me.lucyy.profiles.api.FieldFactory;

import java.util.Map;

public class SimpleFieldFactory implements FieldFactory {
    private final ProfileManagerImpl manager;

    public SimpleFieldFactory(ProfileManagerImpl manager) {
        this.manager = manager;
    }

    @Override
    public SimpleProfileField create(String key, Map<String, Object> cfg) {
        boolean allowColour = cfg.get("allowColour").equals(true);
        return new SimpleProfileField(manager, key,
                (String) cfg.get("displayName"),
                (Integer) cfg.get("order"),
                allowColour);
    }
}