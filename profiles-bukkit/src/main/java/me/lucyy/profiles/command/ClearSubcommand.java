package me.lucyy.profiles.command;

import me.lucyy.common.command.Subcommand;
import me.lucyy.profiles.ConfigHandler;
import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.api.SettableProfileField;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClearSubcommand implements Subcommand {

	private final ProFiles plugin;

	public ClearSubcommand(ProFiles plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "clear";
	}

	@Override
	public String getDescription() {
		return "Clear a field.";
	}

	@Override
	public String getUsage() {
		return "/profile clear <field> ";
	}
	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public boolean execute(CommandSender sender, CommandSender target, String[] args) {

		ProfileManager manager = plugin.getProfileManager();
		ConfigHandler cfg = plugin.getConfigHandler();

		if (args.length < 1) return false;

		ProfileField field = manager.getField(args[0]);
		if (field == null) {
			sender.sendMessage(cfg.getPrefix() + "The field '" + cfg.getAccentColour() + args[0]
					+ cfg.getMainColour() + "' doesn't exist.");
			return true;
		}

		if (!(field instanceof SettableProfileField)) {
			sender.sendMessage(cfg.getPrefix() + "This field can't be set manually.");
			return true;
		}

		SettableProfileField settable = (SettableProfileField) field;
		Player player = (Player) sender;

		String result = settable.clearValue(player.getUniqueId());
		if (result.equals(""))
			sender.sendMessage(cfg.getPrefix() + "Cleared " + field.getDisplayName() + ".");
		else sender.sendMessage(result);


		return true;
	}

	@Override
	public List<String> tabComplete(String[] args) {
		List<String> output = new ArrayList<>();
		if (args.length == 2) {
			for (ProfileField field : plugin.getProfileManager().getFields()) {
				if (field instanceof SettableProfileField) output.add(field.getKey());
			}
		}
		return output;
	}
}
