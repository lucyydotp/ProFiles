package me.lucyy.profiles.api;

import java.util.UUID;

/**
 * Represents a field as part of a profile.
 * @author lucy
 */
public interface ProfileField {
    /**
     * Gets the player this field belongs to
     * @return the player's UUID
     */
    UUID getPlayer();

    /**
     * Gets the key of this field.
     * @return this field's key
     */
    String getKey();

    /**
     * Gets the field's display name.
     */
    String getDisplayName();

    /**
     * Gets the string value of this field.
     * @return the value, or null if it's unset
     */
    String getValue();
}
