package me.lucyy.profiles.api;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

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
	 * @return An optional message to send the player. If null, a message will be generated.
	 */
	public abstract @Nullable Component setValue(UUID player, String value);

	/**
	 * Clears the value of this field.
	 *
	 * @param player the player to clear this field for
	 * @return An optional message to send the player. If null, a message will be generated.
	 */
	public abstract @Nullable Component clearValue(UUID player);

	protected SettableProfileField(String key) {
		super(key);
	}
}
