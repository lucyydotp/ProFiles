package me.lucyy.profiles;

import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.field.ProNounsProfileField;
import me.lucyy.profiles.field.SimpleProfileField;
import me.lucyy.profiles.storage.Storage;

import java.util.*;

public class ProfileManagerImpl implements ProfileManager {
    private final ProFiles plugin;
    private final Storage storage;
    private final Map<String, ProfileField> fields = new HashMap<>();

    public ProfileManagerImpl(ProFiles plugin, Storage storage) {
        this.plugin = plugin;
        this.storage = storage;
        fields.put("field1", new SimpleProfileField(this, "field1", "Field 1 Text"));
        fields.put("field2", new SimpleProfileField(this, "field2", "Field 2 Text"));
        fields.put("pronouns",new ProNounsProfileField(this, "pronouns", "Pronouns",
                plugin.getDepHandler().getProNouns()));
    }

    public Storage getStorage() {
        return storage;
    }

    @Override
    public Collection<ProfileField> getFields() {
        return fields.values();
    }

    @Override
    public ProfileField getField(String key) {
        return fields.get(key);
    }
}
