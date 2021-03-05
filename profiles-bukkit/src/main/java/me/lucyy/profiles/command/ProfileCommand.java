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
 *
 * this file is quite literally copypasted from pronouns - TODO migrate to lucycommonlib
 */

package me.lucyy.profiles.command;

import me.lucyy.profiles.ConfigHandler;
import me.lucyy.profiles.ProFiles;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class ProfileCommand implements CommandExecutor, TabCompleter {
	private final ProFiles plugin;
	private final HashMap<String, Subcommand> subcommands = new HashMap<>();

	private void register(Subcommand cmd) {
		subcommands.put(cmd.getName(), cmd);
	}

	@SuppressWarnings("ConstantConditions")
	public ProfileCommand(ProFiles plugin) {
		this.plugin = plugin;

		register(new HelpSubcommand(this));
		register(new SetSubcommand(plugin));
		register(new ShowSubcommand(plugin));
		register(new ClearSubcommand(plugin));

		this.plugin.getCommand("profile").setTabCompleter(this);
	}

	public ProFiles getPlugin() {
		return plugin;
	}

	public List<Subcommand> getUserSubcommands(CommandSender sender) {
		List<Subcommand> cmds = new ArrayList<>();
		subcommands.forEach((String label, Subcommand cmd) -> {
					if (cmd.getPermission() == null ||  sender.hasPermission(cmd.getPermission()))
						cmds.add(cmd);
				}
		);
		return cmds;
	}
	public Map<String, Subcommand> getUserSubcommandsMap(CommandSender sender) {
		Map<String, Subcommand> cmds = new HashMap<>();
		subcommands.forEach((String label, Subcommand cmd) -> {
					if (cmd.getPermission() == null ||  sender.hasPermission(cmd.getPermission()))
						cmds.put(label, cmd);
				}
		);
		return cmds;
	}

	public List<String> getSubcommands() {
		return new ArrayList<>(subcommands.keySet());
	}

	private void showDefault(CommandSender sender) {
		subcommands.get("show").execute(sender, sender, new String[]{});
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return onCommand(sender, sender, args);
	}

	public boolean onCommand(CommandSender sender, CommandSender target, String[] args) {
		if (args.length < 1) {
			showDefault(sender);
			return true;
		}

		Subcommand subcommand = subcommands.get(args[0]);

		if (subcommand == null) {
			showDefault(sender);
			return true;
		}

		if (subcommand.getPermission() == null || sender.hasPermission(subcommand.getPermission())) {
			if (!subcommand.execute(sender, target, Arrays.copyOfRange(args, 1, args.length))) {
				sender.sendMessage(plugin.getConfigHandler().getPrefix() + "Usage: " + subcommand.getUsage());
			}
		} else {
			sender.sendMessage(plugin.getConfigHandler().getPrefix() + "No permission!");
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			ArrayList<String> results = new ArrayList<>();
			getUserSubcommands(sender).forEach(x -> results.add(x.getName()));
			return results;
		}

		Subcommand subcmd = getUserSubcommandsMap(sender).get(args[0]);
		if (subcmd == null) return null;
		return subcmd.tabComplete(args);
	}
}

