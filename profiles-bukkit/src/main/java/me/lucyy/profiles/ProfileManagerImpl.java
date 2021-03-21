package me.lucyy.profiles;

import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.field.SimpleProfileField;
import me.lucyy.profiles.storage.Storage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import java.util.*;
import java.util.regex.Pattern;

public class ProfileManagerImpl implements ProfileManager {
    private final ProFiles plugin;
    private final Storage storage;
    private final Pattern keyValidPattern = Pattern.compile("^[A-Za-z0-9\\-_]+$");
    private final Map<String, FieldFactory> factoryMap = new HashMap<>();
    private final Map<String, ProfileField> fieldMap = new HashMap<>();

    public ProfileManagerImpl(ProFiles plugin, Storage storage) {
        this.plugin = plugin;
        this.storage = storage;
    }

    public Storage getStorage() {
        return storage;
    }

    private void assertTrue(boolean condition, String message) throws InvalidConfigurationException {
        if (!condition) throw new InvalidConfigurationException(message);
    }

    // this should only be called after server init (scheduler)
    public void loadFields() throws InvalidConfigurationException {
		ConfigurationSection fieldsSection = plugin.getConfig().getConfigurationSection("fields");
        assertTrue(fieldsSection != null,"Fields config section not found");
		fieldMap.clear();
		for (String key : fieldsSection.getKeys(false)) {
            assertTrue(!key.equals("subtitle"),
                    "The key 'subtitle' is reserved for internal use and cannot be used as a field name");

            assertTrue(!fieldMap.containsKey(key), "Key '" + key + "' is a duplicate");

			ConfigurationSection section = fieldsSection.getConfigurationSection(key);

			@SuppressWarnings("ConstantConditions") String type = section.getString("type");
			FieldFactory factory = factoryMap.get(type);

			assertTrue(factory != null, "Field type '" + type + "' is unknown");

			try {
				fieldMap.put(key, factory.create(key, section));
			} catch (IllegalArgumentException e) {
			   throw new InvalidConfigurationException("whilst loading field '" + key + "': " + e.getMessage());
			}
		}

		if (plugin.getConfigHandler().subtitleEnabled()) {
		    fieldMap.put("subtitle", new SimpleProfileField(this, "subtitle", "Subtitle", -1));
        }
    }

    @Override
    public Collection<ProfileField> getFields() {
        return fieldMap.values();
    }

    @Override
    public ProfileField getField(String key) {
        return fieldMap.get(key);
    }

    @Override
    public void register(String key, FieldFactory factory) {
        if (!keyValidPattern.matcher(key).find()) throw new IllegalArgumentException("Key contains invalid characters");
        if (factoryMap.containsKey(key)) throw new IllegalStateException("Key is already registered");
		plugin.getLogger().info("Registered handler for '" + key + "'");
        factoryMap.put(key, factory);
    }
}
