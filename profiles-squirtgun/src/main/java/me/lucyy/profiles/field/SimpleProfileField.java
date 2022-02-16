package me.lucyy.profiles.field;

import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.api.ProfileFieldParameter;
import me.lucyy.profiles.api.SettableProfileField;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class SimpleProfileField extends SettableProfileField {

	@ProfileFieldParameter
	private boolean allowColour;

	public SimpleProfileField(String name) {
		super(name);
	}

	@Override
	public String getValue(UUID player) {
		return ProFiles.getInstance().getPlatform().getStorage().getField(player, key());
	}

	@Override
	public Component setValue(UUID player, String value) {
		ProFiles.getInstance().getPlatform().getStorage().setField(player, key(), value);
		return null;
	}

	@Override
	public Component clearValue(UUID player) {
		ProFiles.getInstance().getPlatform().getStorage().clearField(player, key());
		return null;
	}

	public boolean allowsColour() {
		return allowColour;
	}
}