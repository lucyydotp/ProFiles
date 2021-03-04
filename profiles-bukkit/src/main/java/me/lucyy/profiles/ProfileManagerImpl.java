package me.lucyy.profiles;

import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.storage.Storage;

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

    // this should only be called after server init (scheduler)
    public void loadFields() {

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
        factoryMap.put(key, factory);
    }
}
