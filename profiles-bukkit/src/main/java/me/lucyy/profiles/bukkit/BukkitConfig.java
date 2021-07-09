package me.lucyy.profiles.bukkit;

import me.lucyy.profiles.config.Config;
import me.lucyy.profiles.config.SqlInfoContainer;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class BukkitConfig implements Config {

    private final ProFilesBukkit plugin;
    private final HashMap<TextDecoration, Character> decoStrings = new HashMap<>();

    public BukkitConfig(ProFilesBukkit plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();

        FileConfiguration cfg = plugin.getConfig();

        cfg.addDefault("checkForUpdates", "true");
        cfg.addDefault("format.accent", "&3");
        cfg.addDefault("format.main", "&f");
        cfg.addDefault("subtitle.enabled", "true");

        cfg.addDefault("storage", "yml");

        cfg.addDefault("mysql.host", "127.0.0.1");
        cfg.addDefault("mysql.port", 3306);
        cfg.addDefault("mysql.database", "profiles");
        cfg.addDefault("mysql.username", "profiles");
        cfg.addDefault("mysql.password", "password");

        plugin.saveConfig();

        decoStrings.put(TextDecoration.OBFUSCATED, 'k');
        decoStrings.put(TextDecoration.BOLD, 'l');
        decoStrings.put(TextDecoration.STRIKETHROUGH, 'm');
        decoStrings.put(TextDecoration.UNDERLINED, 'n');
        decoStrings.put(TextDecoration.ITALIC, 'o');
    }

    private String string(String key) {
        return string(key, null);
    }

    private String string(String key, String defaultVal) {
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

    private boolean bool(String key) {
        return !plugin.getConfig().getString(key, "true").equalsIgnoreCase("false");
    }

    @Override
    public void reload() {
        plugin.reloadConfig();
    }

    @Override
    public boolean checkForUpdates() {
        return bool("checkForUpdates");
    }

    @Override
    public boolean subtitleEnabled() {
        return bool("subtitle.enabled");
    }

    @Override
    public FormatProvider format() {
        // TODO
        return new FormatProvider() {
            @Override
            public Component formatMain(@NotNull String input, @NotNull TextDecoration[] formatters) {
                Component out = Component.text(input).color(NamedTextColor.WHITE);
                for (TextDecoration deco : formatters) out = out.decorate(deco);
                return out;
            }

            @Override
            public Component formatAccent(@NotNull String input, @NotNull TextDecoration[] formatters) {
                Component out = Component.text(input).color(TextColor.fromCSSHexString("#99ffcc"));
                for (TextDecoration deco : formatters) out = out.decorate(deco);
                return out;
            }

            @Override
            public Component getPrefix() {
                return Component.text("[ProFiles] ");
            }
        };
    }

    @Override
    public String storage() {
        return Objects.requireNonNull(string("storage", "YML")).toUpperCase(Locale.ROOT);
    }

    @Override
    public SqlInfoContainer mysqlInfo() {
        SqlInfoContainer info = new SqlInfoContainer();
        info.host = string("mysql.host");
        info.port = plugin.getConfig().getInt("mysql.port", 3306);
        info.database = string("mysql.database");
        info.username = string("mysql.username");
        info.password = string("mysql.password");
        return info;
    }

    @Override
    public Map<String, Map<String, Object>> fields() {
        ConfigurationSection cfg = plugin.getConfig().getConfigurationSection("fields");
        if (cfg == null) return null;

        Map<String, Map<String, Object>> out = new HashMap<>();
        for (String sub : cfg.getKeys(false)) {

            Map<String, Object> params = new HashMap<>();
            ConfigurationSection subSection = cfg.getConfigurationSection(sub);
            Objects.requireNonNull(subSection); // this is safe, keys are obtained from getKeys()
            for (String key : subSection.getKeys(false)) {
                params.put(key, subSection.get(key));
            }

            out.put(sub, params);
        }
        return out;
    }
}
