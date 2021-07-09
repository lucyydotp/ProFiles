package me.lucyy.profiles;

import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.config.Config;
import me.lucyy.profiles.storage.Storage;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;

import java.util.Map;

public interface ProFilesPlatform extends Platform {
    Storage getStorage();

    Map<String, FieldFactory> getPlatformSpecificFields();

    Config getConfig();

    void registerCommand(CommandNode<PermissionHolder> node);
}
