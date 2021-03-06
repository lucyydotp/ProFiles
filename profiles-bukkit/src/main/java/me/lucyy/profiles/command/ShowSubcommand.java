package me.lucyy.profiles.command;

import me.lucyy.common.command.Subcommand;
import me.lucyy.common.format.TextFormatter;
import me.lucyy.profiles.ConfigHandler;
import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.api.ProfileField;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShowSubcommand implements Subcommand {

	private final ProFiles plugin;

	public ShowSubcommand(ProFiles plugin) {
		this.plugin = plugin;
	}


	@Override
	public String getName() {
		return "show";
	}

	@Override
	public String getDescription() {
		return "Shows your, or another player's, profile.";
	}

	@Override
	public String getUsage() {
		return "set";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public boolean execute(CommandSender sender, CommandSender target, String[] args) {
		ConfigHandler cfg = plugin.getConfigHandler();
		Player player;
		if (!(sender instanceof Player) && args.length == 0) {
			sender.sendMessage(cfg.getPrefix() + "Please specify a username.");
			return true;
		}

		if (args.length > 0) {
			player = Bukkit.getPlayer(args[0]);
		} else player = (Player)sender;

		if (player == null) {
			sender.sendMessage(cfg.getPrefix() + "Player '" + args[0] + cfg.getMainColour() + "' could not be found.");
			return true;
		}

		StringBuilder output = new StringBuilder().append("\n")
				.append(TextFormatter.formatTitle(target.getName() + "'s profile", cfg))
				.append("\n");
		for (ProfileField field : plugin.getProfileManager().getFields()) {
			output.append(cfg.formatMain(field.getDisplayName() + ": "))
					.append(cfg.formatAccent(field.getValue(player.getUniqueId())))
					.append("\n");
		}
		output.append(TextFormatter.formatTitle("*", cfg)).append("\n");
		sender.sendMessage(output.toString());
		return true;
	}

	@Override
	public List<String> tabComplete(String[] args) {
		if (args.length != 2) return new ArrayList<>();
		List<String> output = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) output.add(player.getName());
		return output;
	}
}
