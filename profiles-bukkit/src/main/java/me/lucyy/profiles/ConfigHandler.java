package me.lucyy.profiles;

import me.lucyy.common.command.FormatProvider;
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
		cfg.addDefault("format.prefix", "&f[&3Profile&f] ");
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

	@SuppressWarnings("ConstantConditions")
	public String getAccentColour() {
		return ChatColor.translateAlternateColorCodes('&',
				getString("format.accent", "&3"));
	}

	@SuppressWarnings("ConstantConditions")
	public String getMainColour() {
		return ChatColor.translateAlternateColorCodes('&',
				getString("format.main", "&f"));
	}

	public String getPrefix() {
		return ChatColor.translateAlternateColorCodes('&',
				plugin.getConfig().getString("format.prefix") + getMainColour());
	}

	public boolean checkForUpdates() {
		return !Objects.equals(plugin.getConfig().getString("checkForUpdates"), "false");
	}
}
