package me.lucyy.profiles;

import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.field.ProNounsProfileField;
import me.lucyy.profiles.field.SimpleProfileField;
import me.lucyy.profiles.storage.Storage;

import java.util.HashSet;
import java.util.Set;

public class ProfileManagerImpl implements ProfileManager {
    private final ProFiles plugin;
    private final Storage storage;
    private final Set<ProfileField> fields = new HashSet<>();

    public ProfileManagerImpl(ProFiles plugin, Storage storage) {
        this.plugin = plugin;
        this.storage = storage;
        fields.add(new SimpleProfileField(this, "field1", "Field 1 (Basic)"));
        fields.add(new SimpleProfileField(this, "field2", "Field 2 (Basic)"));
        fields.add(new ProNounsProfileField(this, "pronouns", "Field 3 (ProNouns)",
                plugin.getDepHandler().getProNouns()));
    }

    public Storage getStorage() {
        return null;
    }

    @Override
    public Set<ProfileField> getFields() {
        return null;
    }

    @Override
    public ProfileField getField(String key) {
        return null;
    }
}
