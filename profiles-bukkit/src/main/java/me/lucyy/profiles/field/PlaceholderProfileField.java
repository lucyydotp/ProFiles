package me.lucyy.profiles.field;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lucyy.profiles.api.ProfileField;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PlaceholderProfileField extends ProfileField {

	private final boolean placeholderApiPresent;
	private final String format;

	public PlaceholderProfileField(String key, String displayName, String format) {
		super(key, displayName);
		this.placeholderApiPresent = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
		this.format = format;

	}

	@Override
	public String getValue(UUID player) {
		if (!placeholderApiPresent) return "PlaceholderAPI is not installed!";
		return PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(player), format);
	}
}