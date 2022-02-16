package me.lucyy.profiles.storage;

import me.lucyy.squirtgun.platform.EventListener;
import me.lucyy.squirtgun.plugin.SquirtgunPlugin;

import java.util.UUID;

public class MysqlListener extends EventListener {
    private final MysqlFileStorage storage;

    public MysqlListener(SquirtgunPlugin<?> plugin, MysqlFileStorage storage) {
        super(plugin);
        this.storage = storage;
    }

    @Override
    public void onPlayerJoin(UUID uuid) {
        storage.addPlayerToCache(uuid);
    }

    @Override
    public void onPlayerLeave(UUID uuid) {
        storage.removePlayerFromCache(uuid);
    }
}
