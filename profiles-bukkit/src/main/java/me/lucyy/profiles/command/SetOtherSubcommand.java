package me.lucyy.profiles.command;

import me.lucyy.common.command.CommandHelper;
import me.lucyy.common.command.Subcommand;
import me.lucyy.profiles.ConfigHandler;
import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.api.SettableProfileField;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class SetOtherSubcommand implements Subcommand {

    private final ProFiles plugin;

    public SetOtherSubcommand(ProFiles plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "setother";
    }

    @Override
    public String getDescription() {
        return "Sets a field for another player.";
    }

    @Override
    public String getUsage() {
        return "setother <player> <field> <value>";
    }

    @Override
    public String getPermission() {
        return "profiles.moderator";
    }

    @Override
    public boolean execute(CommandSender sender, CommandSender ignored, String[] args) {
        ConfigHandler cfg = plugin.getConfigHandler();

        ProfileManager manager = plugin.getProfileManager();

        if (args.length < 3) return false;

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(cfg.getPrefix() + cfg.formatMain("The player '")
                    + cfg.formatAccent(args[0])
                    + cfg.formatMain("' could not be found."));
            return true;
        }

        SettableProfileField field;
        try {
            field = CommandUtils.getSettableField(manager, args[1]);
        } catch (AssertionError e) {
            sender.sendMessage(cfg.getPrefix() + cfg.formatMain(e.getMessage()));
            return true;
        }
        String value = CommandHelper.concatArgs(args, 2);


        String result = field.setValue(target.getUniqueId(), value);
        if (result.equals(""))
            sender.sendMessage(cfg.getPrefix() + cfg.formatMain("Set " + field.getDisplayName() + " to '")
                    + cfg.formatAccent(value) + cfg.formatMain("' for player ")
                    + cfg.formatAccent(target.getName()) + cfg.formatMain("."));
        else sender.sendMessage(result);
        return true;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        List<String> output = new ArrayList<>();
        switch (args.length) {
            case 2:
                return CommandHelper.tabCompleteNames(args[1]);
            case 3:
                return CommandUtils.tabCompleteSettable(plugin.getProfileManager().getFields(), args[2]);
            default:
                ProfileField field = plugin.getProfileManager().getField(args[2]);
                output.add(field instanceof SettableProfileField ? "<"
                        + field.getDisplayName().toLowerCase(Locale.ROOT) + ">" : "You can't set this field!");
        }
        return output;
    }
}
