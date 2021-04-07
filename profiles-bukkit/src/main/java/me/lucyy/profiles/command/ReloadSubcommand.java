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

package me.lucyy.profiles.command;

import me.lucyy.common.command.Subcommand;
import me.lucyy.profiles.config.ConfigHandler;
import me.lucyy.profiles.ProFiles;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class ReloadSubcommand implements Subcommand {

    private final ProFiles pl;

    public ReloadSubcommand(ProFiles plugin) {
        pl = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "ADMIN - reloads config";
    }

    @Override
    public String getUsage() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "profiles.admin";
    }

    @Override
    public boolean execute(@NotNull final CommandSender sender, @NotNull final CommandSender target, @NotNull final String[] args) {
        ConfigHandler handler = pl.getConfigHandler();
        pl.reloadConfig();
        try {
            pl.getProfileManager().loadFields();
        } catch (InvalidConfigurationException e) {
            sender.sendMessage(handler.getPrefix()
                    .append(handler.formatMain("Failed to reload ProFiles!\n" + e.getMessage()))
            );
        }
        sender.sendMessage(pl.getConfigHandler().getPrefix()
                .append(pl.getConfigHandler().formatMain("Reloaded"))
        );
        return true;
    }
}
