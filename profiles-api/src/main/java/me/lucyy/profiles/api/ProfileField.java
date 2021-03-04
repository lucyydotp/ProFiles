package me.lucyy.profiles.api;

import java.util.UUID;

/**
 * Represents a field as part of a profile.
 * @author lucy
 */
public abstract class ProfileField {
    private final String key;
    private final String displayName;

    /**
     * Gets the key of this field.
     * @return this field's key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the field's display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the string value of this field.
     * @param player the player to get the value for
     * @return the value, or null if it's unset
     */
    public abstract String getValue(UUID player);

    protected ProfileField(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }
}
