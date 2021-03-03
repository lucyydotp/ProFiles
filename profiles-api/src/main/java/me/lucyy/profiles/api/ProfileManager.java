package me.lucyy.profiles.api;

import java.util.Set;
import java.util.UUID;

/**
 * Manages fields assigned to users.
 */
public interface ProfileManager {
    /**
     * Get all of a user's fields.
     * @return a set of all the fields that have values
     */
    Set<ProfileField> getFields(UUID uuid);

    /**
     * Get a field for a user.
     * @param key the key ({@link ProfileField#getKey()}) for this field
     * @param uuid the player to get the field for
     * @return the field, or null if it hasn't been set
     */
    ProfileField getField(String key, UUID uuid);
}
