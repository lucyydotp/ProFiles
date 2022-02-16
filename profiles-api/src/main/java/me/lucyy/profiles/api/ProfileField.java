package me.lucyy.profiles.api;

import java.util.UUID;

/**
 * Represents a field as part of a profile. Subclasses <b>MUST</b> provide a constructor with the signature
 * {@code ProfileField(String)}.
 *
 * @author lucy
 */
public abstract class ProfileField {

    private final String key;

    public String key() {
        return key;
    }

    @ProfileFieldParameter(required = true)
    private String displayName;

    public String displayName() {
        return displayName == null ? key : displayName;
    }

    @ProfileFieldParameter()
    private int order;

    public int order() {
        return order;
    }

    public abstract String getValue(UUID uuid);

    protected ProfileField(String key) {
        this.key = key;
    }
}
