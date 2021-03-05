package me.lucyy.profiles.api;

import org.bukkit.configuration.ConfigurationSection;

import java.util.UUID;

/**
 * A profile field with a settable value.
 *
 * @author lucy
 */
public abstract class SettableProfileField extends ProfileField {
	/**
	 * Sets the value of this field.
	 *
	 * @param player the player to set this field for
	 * @param value  the value to set
	 * @return A message to send the player indicating success or failure, or an empty string to indicate a success
	 */
	public abstract String setValue(UUID player, String value);

	/**
	 * Clears the value of this field.
	 *
	 * @param player the player to clear this field for
	 * @return A message to send the player indicating success or failure, or an empty string to indicate a success
	 */
	public abstract String clearValue(UUID player);

	protected SettableProfileField(String key, String displayName) {
		super(key, displayName);
	}
}
