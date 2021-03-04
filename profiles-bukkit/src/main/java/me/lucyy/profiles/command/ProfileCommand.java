package me.lucyy.profiles.command;

import me.lucyy.profiles.ProfileManagerImpl;
import me.lucyy.profiles.api.ProfileField;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand implements CommandExecutor {

    private final ProfileManagerImpl manager;

    public ProfileCommand(ProfileManagerImpl manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player");
            return false;
        }
        Player player = (Player) sender;

        StringBuilder output = new StringBuilder().append("Fields for ").append(player.getDisplayName()).append("\n");
        for (ProfileField field : manager.getFields()) {
            output.append(field.getDisplayName());
            output.append(": ");
            output.append(field.getValue(player.getUniqueId()));
            output.append("\n");
        }
        sender.sendMessage(output.toString());
        return true;
    }
}
