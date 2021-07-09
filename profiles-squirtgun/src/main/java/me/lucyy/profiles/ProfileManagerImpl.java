package me.lucyy.profiles;

import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.field.SimpleProfileField;
import me.lucyy.profiles.storage.Storage;

import java.util.*;
import java.util.regex.Pattern;

public class ProfileManagerImpl implements ProfileManager {
    private final ProFilesPlatform platform;
    private final Pattern keyValidPattern = Pattern.compile("^[A-Za-z0-9\\-_]+$");
    private final Map<String, FieldFactory> factoryMap = new HashMap<>();
    private final Map<String, ProfileField> fieldMap = new HashMap<>();

    public ProfileManagerImpl(ProFilesPlatform platform) {
        this.platform = platform;
    }

    public Storage getStorage() {
        return platform.getStorage();
    }

    private void assertTrue(boolean condition, String message) throws InvalidConfigurationException {
        if (!condition) throw new InvalidConfigurationException(message);
    }

    // this should only be called after server init (scheduler)
    public void loadFields() throws InvalidConfigurationException {
		Map<String, Map<String, Object>> fieldsSection = platform.getConfig().fields();
        assertTrue(fieldsSection != null,"Fields config section not found");

		fieldMap.clear();

        for (Map.Entry<String, Map<String, Object>> entry : fieldsSection.entrySet()) {
            String key = entry.getKey();
            assertTrue(!key.equals("subtitle"),
                    "The key 'subtitle' is reserved for internal use and cannot be used as a field name");

            assertTrue(!fieldMap.containsKey(key), "Key '" + key + "' is a duplicate");

            Map<String, Object> params = entry.getValue();

            String type = params.get("type").toString();
            FieldFactory factory = factoryMap.get(type);

            if (factory == null) {
                platform.getLogger().warning("Field type '" + type + "' is unknown, the field '" + key
                        + "' will be ignored");
                continue;
            }

            try {
                fieldMap.put(key, factory.create(key, params));
            } catch (IllegalArgumentException e) {
                throw new InvalidConfigurationException("whilst loading field '" + key + "': " + e.getMessage());
            }
        }
        if (platform.getConfig().subtitleEnabled()) {
		    fieldMap.put("subtitle", new SimpleProfileField(this, "subtitle", "Subtitle", -1, false));
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
		platform.getLogger().info("Registered handler for '" + key + "'");
        factoryMap.put(key, factory);
    }
}
