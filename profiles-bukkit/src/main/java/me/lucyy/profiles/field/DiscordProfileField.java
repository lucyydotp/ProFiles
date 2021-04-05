package me.lucyy.profiles.field;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import me.lucyy.profiles.api.ProfileField;
import org.bukkit.Bukkit;

import java.util.UUID;

public class DiscordProfileField extends ProfileField {

    private final boolean discordSrvPresent;

    public DiscordProfileField(String key, String displayName, int order) {
        super(key, displayName, order);
        this.discordSrvPresent = Bukkit.getPluginManager().getPlugin("DiscordSRV") != null;
    }

    @Override
    public String getValue(UUID player) {
        if (!discordSrvPresent) return "DiscordSRV is not installed!";
        String id = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player);
        if (id == null) return "Not linked";
        User user = DiscordSRV.getPlugin().getJda().getUserById(id);
        return user == null ? "Not linked" : user.getAsTag();
    }
}