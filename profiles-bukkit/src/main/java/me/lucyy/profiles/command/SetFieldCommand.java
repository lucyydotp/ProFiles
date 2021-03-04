package me.lucyy.profiles.command;

import me.lucyy.profiles.ProfileManagerImpl;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.SettableProfileField;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetFieldCommand implements CommandExecutor {

    private final ProfileManagerImpl manager;

    public SetFieldCommand(ProfileManagerImpl manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player");
            return true;
        }

        if (args.length < 2) return false;

        ProfileField field = manager.getField(args[0]);
        if (field == null) {
            sender.sendMessage("The field '" +  args[0] + "' doesn't exist");
            return true;
        }

        if (!(field instanceof SettableProfileField)) {
            sender.sendMessage("This field can't be set manually");
            return true;
        }

        ((SettableProfileField) field).setValue(((Player) sender).getUniqueId(), args[1]);
        return true;
    }
}
