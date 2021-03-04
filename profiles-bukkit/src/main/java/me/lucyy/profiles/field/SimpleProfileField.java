package me.lucyy.profiles.field;

import me.lucyy.profiles.ProfileManagerImpl;
import me.lucyy.profiles.api.SettableProfileField;

import java.util.UUID;

public class SimpleProfileField extends SettableProfileField {
    private final ProfileManagerImpl manager;

    public SimpleProfileField(ProfileManagerImpl manager, String key, String displayName) {
        super(key, displayName);
        this.manager = manager;
    }

    @Override
    public String getValue(UUID uuid) {
        return manager.getStorage().getField(uuid, getKey());
    }

    @Override
    public void setValue(UUID player, String value) {
        manager.getStorage().setField(player, getKey(), value);
    }
}
