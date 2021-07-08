package me.lucyy.profiles.bukkit;

import me.lucyy.profiles.ProFilesPlatform;
import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.bukkit.field.DiscordFieldFactory;
import me.lucyy.profiles.bukkit.field.PlaceholderFieldFactory;
import me.lucyy.profiles.bukkit.field.ProNounsFieldFactory;
import me.lucyy.profiles.config.Config;
import me.lucyy.profiles.storage.Storage;
import me.lucyy.squirtgun.bukkit.BukkitNodeExecutor;
import me.lucyy.squirtgun.bukkit.BukkitPlatform;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import me.lucyy.squirtgun.platform.audience.SquirtgunUser;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Set;

public class ProFilesBukkitPlatform extends BukkitPlatform implements ProFilesPlatform {

    private final Config config;
    private final JavaPlugin plugin;
    private final Set<FieldFactory> fields = Set.of(
            new ProNounsFieldFactory(),
            new DiscordFieldFactory(),
            new PlaceholderFieldFactory()
    );

    public ProFilesBukkitPlatform(ProFilesBukkit plugin) {
        super(plugin);
        this.plugin = plugin;
        config = new BukkitConfig(plugin);
    }

    @Override
    public Storage getStorage() {
        return null;
    }

    @Override
    public Set<FieldFactory> getPlatformSpecificFields() {
        return fields;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public void registerCommand(CommandNode<PermissionHolder> node) {
        PluginCommand command = plugin.getCommand(node.getName());
        Objects.requireNonNull(command);
        TabExecutor executor = new BukkitNodeExecutor(node, config.format(), this);
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }
}
