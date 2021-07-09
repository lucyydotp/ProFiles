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


import me.lucyy.profiles.InvalidConfigurationException;
import me.lucyy.profiles.ProFiles;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.node.AbstractNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class ReloadNode extends AbstractNode<PermissionHolder> {

    private final ProFiles pl;

    public ReloadNode(ProFiles plugin) {
        super("admin", "Reloads the config.", "profiles.admin");
        pl = plugin;
    }

    @Override
    public @Nullable Component execute(CommandContext<PermissionHolder> context) {
        pl.getPlatform().getConfig().reload();
        final FormatProvider fmt = pl.getPlatform().getConfig().format();
        try {
            pl.getProfileManager().loadFields();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            return fmt.getPrefix()
                    .append(fmt.formatMain(
                            "Failed to reload ProFiles! See console for more info:\n" + e.getMessage()
                    ));
        }
        return fmt.getPrefix().append(fmt.formatMain("Reloaded"));
    }
}
