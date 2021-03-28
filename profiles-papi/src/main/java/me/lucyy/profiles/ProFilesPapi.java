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

package me.lucyy.profiles;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lucyy.common.format.TextFormatter;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;


public class ProFilesPapi extends PlaceholderExpansion {
    private final String VERSION = getClass().getPackage().getImplementationVersion();
    private ProfileManager manager;

    @Override
    public boolean canRegister() {
        try {
            Class.forName("me.lucyy.profiles.api.ProfileManager");
        } catch (ClassNotFoundException ignored) {
            return false;
        }

        RegisteredServiceProvider<ProfileManager> rsp = Bukkit.getServer().getServicesManager().getRegistration(ProfileManager.class);
        if (rsp == null) return false;
        manager = rsp.getProvider();
        return true;
    }

    @Override
    public String getRequiredPlugin() {
        return "ProFiles";
    }

    @Override
    public String getAuthor() {
        return "__lucyy";
    }

    @Override
    public String getIdentifier() {
        return "profiles";
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) return "";

        String[] split = identifier.split("_");

        ProfileField field = manager.getField(split[0]);
        if (field == null) return null;

        String mod;
        try {
            mod = split[1];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            mod = "";
        }

        String feedback = TextFormatter.format(field.getValue(player.getUniqueId()));

        return mod.equals("col") ? feedback : ChatColor.stripColor(feedback);
    }
}
