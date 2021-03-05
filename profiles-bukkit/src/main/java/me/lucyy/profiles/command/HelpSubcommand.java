package me.lucyy.profiles.command;

import me.lucyy.profiles.ConfigHandler;
import me.lucyy.profiles.ProFiles;
import org.bukkit.command.CommandSender;

public class HelpSubcommand implements Subcommand {

	private final ProfileCommand cmd;

	public HelpSubcommand(ProfileCommand cmd) {
		this.cmd = cmd;
	}


	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "List all subcommands for the plugin.";
	}

	@Override
	public String getUsage() {
		return "/profile help";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public boolean execute(CommandSender sender, CommandSender target, String[] args) {
		ConfigHandler cfg = cmd.getPlugin().getConfigHandler();
		sender.sendMessage(cfg.getAccentColour() + "ProFiles v" + cmd.getPlugin().getDescription().getVersion() +
				cfg.getMainColour() + " by " + cfg.getAccentColour() + "__lucyy");
		sender.sendMessage(cfg.getMainColour() + "Commands:");
		cmd.getUserSubcommands(sender).forEach(cmd -> {
					if (cmd.getPermission() == null ||  sender.hasPermission(cmd.getPermission()))
						sender.sendMessage(cfg.getMainColour() + "/profile "
								+ cfg.getAccentColour() + cmd.getName()
								+ cfg.getMainColour() + " - " + cmd.getDescription());
				}
		);
		return true;
	}
}
