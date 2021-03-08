package me.lucyy.profiles;

import me.lucyy.common.command.FormatProvider;
import me.lucyy.common.format.TextFormatter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ConfigHandler implements FormatProvider {
	private final ProFiles plugin;

	public ConfigHandler(ProFiles plugin) {
		this.plugin = plugin;
		plugin.saveDefaultConfig();

		FileConfiguration cfg = plugin.getConfig();

		plugin.getConfig().addDefault("checkForUpdates", "true");
		cfg.addDefault("format.accent", "&3");
		cfg.addDefault("format.main", "&f");

		plugin.saveConfig();
	}

	private String getString(String key, String defaultVal) {
		String value = plugin.getConfig().getString(key);
		if (value == null) {
			if (defaultVal == null) {
				plugin.getLogger().severe("Your config file is broken! Unable to read key '" + key);
				return null;
			}
			return defaultVal;
		}
		return value;
	}

	private String applyFormatter(String formatter, String content, String overrides) {
		if (formatter.contains("%s")) return TextFormatter.format(String.format(formatter, content), overrides);
		StringBuilder formatters = new StringBuilder();
		if (overrides != null) {
			for (char character : overrides.toCharArray()) formatters.append("&").append(character);
		}
		return TextFormatter.format(formatter + formatters.toString() + content);
	}

	@SuppressWarnings("ConstantConditions")
	public String getAccentColour() {
		return ChatColor.translateAlternateColorCodes('&',
				getString("format.accent", "&3"));
	}

	@Override
	public String formatAccent(String s, String formatters) {
		return applyFormatter(getAccentColour(), s, formatters);
	}

	@SuppressWarnings("ConstantConditions")
	public String getMainColour() {
		return ChatColor.translateAlternateColorCodes('&',
				getString("format.main", "&f"));
	}

	@Override
	public String formatMain(String s, String formatters) {
		return applyFormatter(getMainColour(), s, formatters);
	}

	public String getPrefix() {
		return TextFormatter.format(getString("format.prefix", formatAccent("Profile") + ChatColor.GRAY + " >> "));
	}

	public boolean checkForUpdates() {
		return "false".equals(plugin.getConfig().getString("checkForUpdates"));
	}
}
