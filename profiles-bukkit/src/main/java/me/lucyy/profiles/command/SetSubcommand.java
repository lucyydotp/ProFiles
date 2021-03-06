package me.lucyy.profiles.command;

import me.lucyy.common.command.Subcommand;
import me.lucyy.profiles.ConfigHandler;
import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.api.SettableProfileField;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class SetSubcommand implements Subcommand {

	private final ProFiles plugin;

	public SetSubcommand(ProFiles plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "set";
	}

	@Override
	public String getDescription() {
		return "Sets a field.";
	}

	@Override
	public String getUsage() {
		return "set <field> <value>";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public boolean execute(CommandSender sender, CommandSender target, String[] args) {

		ProfileManager manager = plugin.getProfileManager();
		ConfigHandler cfg = plugin.getConfigHandler();

		if (args.length < 2) return false;

		ProfileField field = manager.getField(args[0]);
		if (field == null) {
			sender.sendMessage(cfg.getPrefix() + cfg.formatMain("The field '") + cfg.formatAccent(args[0])
					+ cfg.formatMain("' doesn't exist."));
			return true;
		}

		if (!(field instanceof SettableProfileField)) {
			sender.sendMessage(cfg.getPrefix() + cfg.formatMain("This field can't be set manually."));
			return true;
		}
		StringBuilder value = new StringBuilder();
		for (int x = 1; x < args.length; x++) {
			value.append(args[x]).append(" ");
		}
		value.setLength(value.length() - 1);

		SettableProfileField settable = (SettableProfileField) field;
		Player player = (Player) sender;

		String result = settable.setValue(player.getUniqueId(), value.toString());
		if (result.equals(""))
			sender.sendMessage(cfg.getPrefix() + cfg.formatMain("Set " + field.getDisplayName() + " to '")
					+ cfg.formatAccent(value.toString()) + cfg.formatMain( "'."));
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
		} else {
			ProfileField field = plugin.getProfileManager().getField(args[1]);
			output.add(field instanceof SettableProfileField ? "<"
					+ field.getDisplayName().toLowerCase(Locale.ROOT) + ">" : "You can't set this field!");
		}
		return output;
	}
}
