package me.lucyy.profiles;

import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.config.Config;
import me.lucyy.profiles.storage.Storage;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;

import java.util.Set;

public interface ProFilesPlatform extends Platform {
    Storage getStorage();

    Set<FieldFactory> getPlatformSpecificFields();

    Config getConfig();

    void registerCommand(CommandNode<PermissionHolder> node);
}
