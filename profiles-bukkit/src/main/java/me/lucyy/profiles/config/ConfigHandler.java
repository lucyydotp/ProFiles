/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of ProFiles.
 *
 * ProFiles is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProFiles is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProFiles.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucyy.profiles.config;

import me.lucyy.common.command.FormatProvider;
import me.lucyy.common.format.TextFormatter;
import me.lucyy.profiles.ProFiles;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Locale;

public class ConfigHandler implements FormatProvider {
	private final ProFiles plugin;

	public ConfigHandler(ProFiles plugin) {
		this.plugin = plugin;
		plugin.saveDefaultConfig();

		FileConfiguration cfg = plugin.getConfig();

		cfg.addDefault("checkForUpdates", "true");
		cfg.addDefault("format.accent", "&3");
		cfg.addDefault("format.main", "&f");
		cfg.addDefault("subtitle.enabled", "true");

		cfg.addDefault("storage", "yml");

		cfg.addDefault( "mysql.host", "127.0.0.1");
		cfg.addDefault("mysql.port", 3306);
		cfg.addDefault("mysql.database", "profiles");
		cfg.addDefault("mysql.username", "profiles");
		cfg.addDefault("mysql.password", "password");

		plugin.saveConfig();
	}

	private String getString(String key) {
		return getString(key, null);
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
		if (formatter.contains("%s")) return TextFormatter.format(String.format(formatter, content), overrides, true);

		return TextFormatter.format(formatter + content, overrides, true);
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

	@SuppressWarnings("ConstantConditions")
	public String getPrefix() {
		String prefix = getString("format.prefix", "");
		if (prefix.equals("")) return formatAccent("Profile") + ChatColor.GRAY + " >> ";
		return TextFormatter.format(getString("format.prefix"));
	}

	public boolean subtitleEnabled() {
		return !"false".equals(getString("subtitle.enabled", "true"));
	}

	public boolean checkForUpdates() {
		return !"false".equals(getString("checkForUpdates", "true"));
	}

	public SqlInfoContainer getSqlConnectionData() {
		SqlInfoContainer info = new SqlInfoContainer();
		info.host = getString("mysql.host");
		info.port = plugin.getConfig().getInt("mysql.port", 3306);
		info.database = getString("mysql.database");
		info.username = getString("mysql.username");
		info.password = getString("mysql.password");
		return info;
	}

	@SuppressWarnings("ConstantConditions")
	public String getStorage() {
		return getString("storage", "YML").toUpperCase();
	}
}
