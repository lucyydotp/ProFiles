package me.lucyy.profiles.api;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * Manages fields assigned to users.
 */
public interface ProfileManager {
    /**
     * Get all the active fields.
     */
    Collection<ProfileField> getFields();

    /**
     * Get a field.
     * @param key the key ({@link ProfileField#getKey()}) for this field
     * @return the field, or null if it hasn't been set
     */
    ProfileField getField(String key);
}
