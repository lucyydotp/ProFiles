package me.lucyy.profiles.config;

import me.lucyy.squirtgun.format.FormatProvider;

import java.util.Map;

/**
 * A configuration file.
 */
public interface Config {

    /**
     * Reloads the config.
     */
    void reload();

    /**
     * Whether to check for updates.
     */
    boolean checkForUpdates();

    /**
     * Whether the profile subtitle is enabled.
     */
    boolean subtitleEnabled();

    /**
     * The format provider.
     */
    FormatProvider format();

    /**
     * The string name of the storage platform in use.
     */
    String storage();

    /**
     * The MySQL connection info.
     */
    SqlInfoContainer mysqlInfo();

    /**
     * Fields, given as a map of a name to a set of parameters.
     */
    Map<String, Map<String, Object>> fields();
}
