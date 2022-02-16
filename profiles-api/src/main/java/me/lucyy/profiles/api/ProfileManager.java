package me.lucyy.profiles.api;

import java.util.Collection;

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
     * @param key the key ({@link ProfileField#key()}) for this field
     * @return the field, or null if it hasn't been set
     */
    ProfileField getField(String key);

    /**
     * Registers a field factory for use in the config.
     * @param key a globally unique string for use in the config to identify this field. Valid chars are A-Z, a-z, 0-9, underscore, hyphen
     * @param factory the factory to register
     * @throws IllegalArgumentException if the key contains invalid characters
     * @throws IllegalStateException if the key has already been registered
     */
    void register(String key, Class<? extends ProfileField> factory);
}
