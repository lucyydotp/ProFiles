package me.lucyy.profiles.api;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/*
 * A factory to create fields from configuration sections.
 */
public interface FieldFactory {
    /**
     * Create a field from a configuration entry.
     *
     * @param cfg the configuration section to get the values from
     * @return the field
     * @throws IllegalArgumentException if a required parameter is missing
     */
    ProfileField create(ConfigurationSection cfg);
}
