package me.lucyy.profiles.field;

import me.lucyy.profiles.ProfileManagerImpl;
import me.lucyy.profiles.api.SettableProfileField;

import java.util.UUID;

public class SimpleProfileField extends ProfileFieldBase implements SettableProfileField {

    public SimpleProfileField(ProfileManagerImpl manager, String key, String displayName) {
        super(manager, key, displayName);
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
