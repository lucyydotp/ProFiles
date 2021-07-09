package me.lucyy.profiles.bukkit;

import me.lucyy.profiles.ProFilesPlatform;
import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.bukkit.field.DiscordFieldFactory;
import me.lucyy.profiles.bukkit.field.PlaceholderFieldFactory;
import me.lucyy.profiles.bukkit.field.ProNounsFieldFactory;
import me.lucyy.profiles.config.Config;
import me.lucyy.profiles.storage.MysqlConnectionException;
import me.lucyy.profiles.storage.MysqlFileStorage;
import me.lucyy.profiles.storage.Storage;
import me.lucyy.squirtgun.bukkit.BukkitNodeExecutor;
import me.lucyy.squirtgun.bukkit.BukkitPlatform;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;

public class ProFilesBukkitPlatform extends BukkitPlatform implements ProFilesPlatform {

    private final Config config;
    private final JavaPlugin plugin;
    private Storage storage;
    private final Map<String, FieldFactory> fields = Map.of(
            "pronouns", new ProNounsFieldFactory(),
            "discordsrv", new DiscordFieldFactory(),
            "placeholder", new PlaceholderFieldFactory()
    );

    public ProFilesBukkitPlatform(ProFilesBukkit plugin) {
        super(plugin);
        this.plugin = plugin;
        config = new BukkitConfig(plugin);

        switch (config.storage()) {
            default:
                getLogger().warning("Unknown storage type '" + config.storage() + "', falling back to YML.");
            case "YML":
                storage = new YamlStorage(plugin);
                break;
            case "MYSQL":
                try {
                    storage = new MysqlFileStorage(plugin.getPlugin());
                } catch (MysqlConnectionException e) {
                    plugin.getLogger().severe("Failed to connect to MySQL, falling back to YML.");
                    e.printStackTrace();
                    storage = new YamlStorage(plugin);
                }
                break;
        }
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public Map<String, FieldFactory> getPlatformSpecificFields() {
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
