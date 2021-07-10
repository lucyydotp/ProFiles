package me.lucyy.profiles.field;

import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.ProFilesPlatform;
import me.lucyy.profiles.ProfileManagerImpl;
import me.lucyy.profiles.api.ProfileFieldParameter;
import me.lucyy.profiles.api.SettableProfileField;

import java.util.Map;
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
	public String setValue(UUID player, String value) {
		ProFiles.getInstance().getPlatform().getStorage().setField(player, key(), value);
		return "";
	}

	@Override
	public String clearValue(UUID player) {
		ProFiles.getInstance().getPlatform().getStorage().clearField(player, key());
		return "";
	}

	public boolean allowsColour() {
		return allowColour;
	}
}