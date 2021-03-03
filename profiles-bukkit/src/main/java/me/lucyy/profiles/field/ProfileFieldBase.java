package me.lucyy.profiles.field;

import me.lucyy.profiles.ProfileManagerImpl;
import me.lucyy.profiles.api.ProfileField;

import java.util.UUID;

public abstract class ProfileFieldBase implements ProfileField {
    private final String key;
    private final String displayName;
    protected final ProfileManagerImpl manager;

    protected ProfileFieldBase(ProfileManagerImpl manager, String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
        this.manager = manager;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
