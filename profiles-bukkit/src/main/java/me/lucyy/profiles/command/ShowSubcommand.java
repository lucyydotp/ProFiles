package me.lucyy.profiles.command;

import me.lucyy.common.command.CommandHelper;
import me.lucyy.common.command.Subcommand;
import me.lucyy.common.format.TextFormatter;
import me.lucyy.profiles.FormatInverter;
import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.config.ConfigHandler;
import me.lucyy.profiles.field.SimpleProfileField;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static me.lucyy.profiles.command.CommandUtils.serialiseField;

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
    public boolean execute(CommandSender sender, CommandSender ignored, String[] args) {
        ConfigHandler cfg = plugin.getConfigHandler();
        OfflinePlayer target;
        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(cfg.getPrefix() + "Please specify a username.");
            return true;
        }

        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);
        } else target = (Player) sender;

        if (target == null) {
            sender.sendMessage(cfg.getPrefix()
                    .append(cfg.formatMain("Player '"))
                    .append(cfg.formatAccent(args[0]))
                    .append(cfg.formatMain("' could not be found."))
            );
            return true;
        }

        Component nl = Component.text(" \n");

        final Component[] output = {nl
                .append(TextFormatter.formatTitle(target.getName() + "'s profile", cfg))
                .append(nl)};

        if (plugin.getConfigHandler().subtitleEnabled()) {
            String subtitle = plugin.getProfileManager().getField("subtitle").getValue(target.getUniqueId());
            if (!subtitle.equals("Unset")) {
                output[0] = output[0].append(TextFormatter.centreText(subtitle, new FormatInverter(plugin.getConfigHandler()), " "))
                        .append(nl);
            }
        }

        plugin.getProfileManager().getFields().stream()
                .sorted(Comparator.comparingInt(ProfileField::getOrder)).forEach(field -> {
            if (!field.getKey().equals("subtitle")) {
                String value = field.getValue(target.getUniqueId());
                output[0] = output[0].append(cfg.formatMain(field.getDisplayName() + ": "))
                        .append(serialiseField(field, value, cfg))
                        .append(nl);
            }
        });
        output[0] = output[0].append(nl).append(TextFormatter.formatTitle("*", cfg));
        sender.sendMessage(output[0]);
        return true;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length != 2) return new ArrayList<>();
        return CommandHelper.tabCompleteNames(args[1]);
    }
}
