package me.lucyy.profiles.bukkit.field;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileFieldParameter;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;

public class PlaceholderProfileField extends ProfileField {

    private final boolean placeholderApiPresent;

    @ProfileFieldParameter(required = true)
    private String format;

    public PlaceholderProfileField(String key) {
        super(key);
        this.placeholderApiPresent = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    @Override
    public String getValue(UUID player) {
        if (!placeholderApiPresent) return "PlaceholderAPI is not installed!";
        return PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(player), format);
    }
}