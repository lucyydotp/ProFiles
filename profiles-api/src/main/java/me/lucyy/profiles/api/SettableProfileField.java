package me.lucyy.profiles.api;

/**
 * A profile field with a settable value.
 * @author lucy
 */
public interface SettableProfileField extends ProfileField {
    /**
     * Sets the value of this field.
     * @param value the value to set
     */
    void setValue(String value);
}
