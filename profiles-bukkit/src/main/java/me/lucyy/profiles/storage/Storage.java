package me.lucyy.profiles.storage;

import java.util.UUID;

public interface Storage {
    void setField(UUID uuid, String key, String value);
    String getField(UUID uuid, String key);
    void clearField(UUID uuid, String key);
    void close();
}
