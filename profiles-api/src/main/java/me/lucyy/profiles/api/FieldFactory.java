package me.lucyy.profiles.api;

import java.util.Map;

/*
 * A factory to create fields from configuration sections.
 */
public interface FieldFactory {
    /**
     * Create a field from a configuration entry.
     *
     * @param key the field's key
     * @param cfg the map of objects to get values from
     * @return the field
     * @throws IllegalArgumentException if a required parameter is missing or malformed
     */
    ProfileField create(String key, Map<String, Object> cfg) throws IllegalArgumentException;
}
