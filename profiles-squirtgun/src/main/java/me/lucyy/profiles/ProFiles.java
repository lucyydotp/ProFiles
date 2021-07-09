package me.lucyy.profiles;

import me.lucyy.profiles.api.FieldFactory;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.command.*;
import me.lucyy.profiles.config.Config;
import me.lucyy.profiles.field.SimpleFieldFactory;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.command.node.PluginInfoNode;
import me.lucyy.squirtgun.command.node.subcommand.SubcommandNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import me.lucyy.squirtgun.platform.audience.SquirtgunUser;
import me.lucyy.squirtgun.platform.scheduler.Task;
import me.lucyy.squirtgun.plugin.SquirtgunPlugin;
import me.lucyy.squirtgun.update.PolymartUpdateChecker;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

public final class ProFiles extends SquirtgunPlugin<ProFilesPlatform> {

    private ProfileManagerImpl profileManager;

    public ProFiles(@NotNull ProFilesPlatform platform) {
        super(platform);
    }

    public ProfileManagerImpl getProfileManager() {
        return profileManager;
    }

    @Override
    public @NotNull String getPluginName() {
        return "ProFiles";
    }

    @Override
    public @NotNull String getPluginVersion() {
        return "1.1.0-DEV"; // TODO
    }

    @Override
    public @NotNull String[] getAuthors() {
        return new String[]{"__lucyy"};
    }

    @Override
    public void onEnable() {

        profileManager = new ProfileManagerImpl(getPlatform());

        CommandNode<PermissionHolder> rootNode = SubcommandNode.withFallback(
                "profile",
                "ProFiles root command",
                null,
                new ShowNode(this),
                new SetFieldNode(profileManager),
                new ClearNode(profileManager),
                new SetOtherNode(profileManager),
                new ClearOtherNode(profileManager),
                new PluginInfoNode<>("version", this),
                new ReloadNode(this)
        );

        getPlatform().registerCommand(rootNode);

        profileManager.register("simple", new SimpleFieldFactory(profileManager));
        getPlatform().getPlatformSpecificFields().forEach(profileManager::register);

        final Config config = getPlatform().getConfig();
        if (config.checkForUpdates()) {
            final FormatProvider fmt = getPlatform().getConfig().format();
            new PolymartUpdateChecker(this, 925,
                    fmt.getPrefix().append(
                            fmt.formatMain("A new version of ProFiles is available!\nFind it at "))
                            .append(fmt.formatAccent("https://lucyy.me/profiles", new TextDecoration[]{TextDecoration.UNDERLINED})
                                    .clickEvent(ClickEvent.openUrl("https://lucyy.me/profiles"))),
                    "profiles.admin"
            );
        } else {
            getPlatform().getLogger().warning("Update checking is disabled. You might be running an old version!");
        }

        // run this after plugin initialisation to give other plugins a chance to inject fields
        Task.builder()
                .action(p -> {
                try {
                    profileManager.loadFields();
                } catch (InvalidConfigurationException e) {
                    getPlatform().getLogger().severe("Failed to load config: " + e.getMessage());
                }
            }).build().execute(getPlatform());
    }
}
