package me.lucyy.profiles.command;

import me.lucyy.common.command.Subcommand;
import me.lucyy.profiles.config.ConfigHandler;
import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.api.SettableProfileField;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
        return "clear <field>";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean execute(CommandSender sender, CommandSender target, String[] args) {

        ConfigHandler cfg = plugin.getConfigHandler();

        if (!(target instanceof Player)) {
            sender.sendMessage(cfg.getPrefix() + cfg.formatMain("This command can only be run by a player."));
            return true;
        }

        ProfileManager manager = plugin.getProfileManager();
        if (args.length < 1) return false;

        SettableProfileField field;
        try {
            field = CommandUtils.getSettableField(manager, args[0]);
        } catch (AssertionError e) {
            sender.sendMessage(cfg.getPrefix() + cfg.formatMain(e.getMessage()));
            return true;
        }

        Player player = (Player) sender;

        String result = field.clearValue(player.getUniqueId());
        if (result.equals(""))
            sender.sendMessage(cfg.getPrefix() + cfg.formatMain("Cleared " + field.getDisplayName() + "."));
        else sender.sendMessage(result);


        return true;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        List<String> output = new ArrayList<>();
        if (args.length == 2) {
            return CommandUtils.tabCompleteSettable(plugin.getProfileManager().getFields(), args[1]);
        }
        return output;
    }
}
