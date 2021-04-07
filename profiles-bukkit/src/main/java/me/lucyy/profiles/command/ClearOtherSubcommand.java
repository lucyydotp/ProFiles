package me.lucyy.profiles.command;

import me.lucyy.common.command.CommandHelper;
import me.lucyy.common.command.Subcommand;
import me.lucyy.profiles.config.ConfigHandler;
import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.api.SettableProfileField;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClearOtherSubcommand implements Subcommand {

    private final ProFiles plugin;

    public ClearOtherSubcommand(ProFiles plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "clearother";
    }

    @Override
    public String getDescription() {
        return "Clears a field for another player..";
    }

    @Override
    public String getUsage() {
        return "clearother <player> <field>";
    }

    @Override
    public String getPermission() {
        return "profiles.moderator";
    }

    @Override
    public boolean execute(CommandSender sender, CommandSender ignored, String[] args) {

        ConfigHandler cfg = plugin.getConfigHandler();
        ProfileManager manager = plugin.getProfileManager();

        if (args.length < 2) return false;

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(cfg.getPrefix()
                    .append(cfg.formatMain("The player '"))
                    .append(cfg.formatAccent(args[0]))
                    .append(cfg.formatMain("' could not be found.")));
            return true;
        }

        SettableProfileField field;
        try {
            field = CommandUtils.getSettableField(manager, args[1]);
        } catch (AssertionError e) {
            sender.sendMessage(cfg.getPrefix().append(cfg.formatMain(e.getMessage())));
            return true;
        }

        String result = field.clearValue(target.getUniqueId());
        if (result.equals(""))
            sender.sendMessage(cfg.getPrefix()
                    .append(cfg.formatMain("Cleared '"))
                    .append(cfg.formatAccent(field.getDisplayName()))
                    .append(cfg.formatMain("' for player "))
                    .append(cfg.formatAccent(target.getName()))
                    .append(cfg.formatMain(".")));
        else sender.sendMessage(result);

        return true;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 3)
            return CommandUtils.tabCompleteSettable(plugin.getProfileManager().getFields(), args[2]);
        else if (args.length == 2) return CommandHelper.tabCompleteNames(args[1]);
        return new ArrayList<>();
    }
}
