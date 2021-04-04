package me.lucyy.profiles;

import me.lucyy.common.DependencyChecker;
import me.lucyy.common.command.Command;
import me.lucyy.common.command.HelpSubcommand;
import me.lucyy.common.command.VersionSubcommand;
import me.lucyy.common.update.UpdateChecker;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.command.*;
import me.lucyy.profiles.config.ConfigHandler;
import me.lucyy.profiles.field.factory.PlaceholderFieldFactory;
import me.lucyy.profiles.field.factory.ProNounsFieldFactory;
import me.lucyy.profiles.field.factory.SimpleFieldFactory;
import me.lucyy.profiles.storage.MySqlFileStorage;
import me.lucyy.profiles.storage.MysqlConnectionException;
import me.lucyy.profiles.storage.Storage;
import me.lucyy.profiles.storage.YamlStorage;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class ProFiles extends JavaPlugin {

    private final OptionalDependencyHandler depHandler = new OptionalDependencyHandler(this);
	private ProfileManagerImpl profileManager;
    private Storage storage;

	private ConfigHandler config;

	public ProfileManagerImpl getProfileManager() {
		return profileManager;
	}

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onEnable() {
		if (!DependencyChecker.adventurePresent(this)) {
			getPluginLoader().disablePlugin(this);
			return;
		}

		Metrics metrics = new Metrics(this,10559);

		config = new ConfigHandler(this);

		switch (config.getStorage()) {
			case "MYSQL":
				try {
					storage = new MySqlFileStorage(this);
					getServer().getPluginManager().registerEvents((MySqlFileStorage) storage, this);
				} catch (MysqlConnectionException ignored) {
					getPluginLoader().disablePlugin(this);
					return;
				}
				break;
			case "YML":
			default:
				storage = new YamlStorage(this);
		}
        profileManager = new ProfileManagerImpl(this, storage);

        getServer().getServicesManager().register(ProfileManager.class, profileManager, this, ServicePriority.Normal);

		Command cmd = new Command(config);
        cmd.register(new HelpSubcommand(cmd, config, this, "profile"));
        cmd.register(new ReloadSubcommand(this));
        cmd.register(new VersionSubcommand(config, this));
        cmd.register(new SetSubcommand(this));
		cmd.register(new ClearSubcommand(this));
		cmd.register(new SetOtherSubcommand(this));
		cmd.register(new ClearOtherSubcommand(this));

		ShowSubcommand defaultSub = new ShowSubcommand(this);
        cmd.register(defaultSub);
        cmd.setDefaultSubcommand(defaultSub);

        getCommand("profile").setExecutor(cmd);
		getCommand("profile").setTabCompleter(cmd);

		profileManager.register("simple", new SimpleFieldFactory(profileManager));
        profileManager.register("pronouns", new ProNounsFieldFactory(depHandler));
		profileManager.register("placeholder", new PlaceholderFieldFactory());

		if (getConfigHandler().checkForUpdates()) {
        	new UpdateChecker(this,
					"https://api.spigotmc.org/legacy/update.php?resource=89959",
							"A new version of ProFiles is available!\nFind it at https://lucyy.me/profiles",
					"profiles.admin"
			);
		} else {
			getLogger().warning("Update checking is disabled. You might be running an old version!");
		}

		new BukkitRunnable() {
        	@Override
			public void run() {
        		try {
					profileManager.loadFields();
				} catch (InvalidConfigurationException e) {
        			getLogger().severe("Failed to load config: " + e.getMessage());
				}
			}
		}.runTaskLater(this, 1);
    }

    @Override
    public void onDisable() {
		if (storage != null) storage.close();
    }

	public ConfigHandler getConfigHandler() {
		return config;
	}
}
