package me.lucyy.profiles.command;

import me.lucyy.profiles.FormatInverter;
import me.lucyy.profiles.ProFiles;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.argument.OnlinePlayerArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.node.AbstractNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.format.TextFormatter;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import me.lucyy.squirtgun.platform.audience.SquirtgunPlayer;
import me.lucyy.squirtgun.platform.audience.SquirtgunUser;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

import static me.lucyy.profiles.command.CommandUtils.serialiseField;

public class ShowNode extends AbstractNode<PermissionHolder> {

    private final ProFiles plugin;
    private final CommandArgument<SquirtgunPlayer> playerArg;

    public ShowNode(ProFiles plugin) {
        super("show", "Shows your, or another player's, profile.", null);
        this.plugin = plugin;
        this.playerArg = new OnlinePlayerArgument("player", "Whose profile to show", true,
                null); // TODO - proper platform
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return List.of(playerArg);
    }

    // TODO - tidy this method up
    @Override
    public @Nullable Component execute(CommandContext<PermissionHolder> context) {
        final FormatProvider fmt = context.getFormat();
        SquirtgunPlayer target = context.getArgumentValue(playerArg);
        if (target == null) {
            if (context.getTarget() instanceof SquirtgunPlayer) {
                target = (SquirtgunPlayer) context.getTarget();
            } else {
                return fmt.getPrefix().append(fmt.formatMain("Please specify a username."));
            }
        }

        final Component nl = Component.newline();
        final Component[] output = {nl
                .append(TextFormatter.formatTitle(target.getUsername() + "'s profile", fmt))
                .append(nl)};

        if (plugin.getPlatform().getConfig().subtitleEnabled()) {
            String subtitle = plugin.getProfileManager().getField("subtitle").getValue(target.getUuid());
            if (!subtitle.equals("Unset")) {
                output[0] = output[0].append(TextFormatter.centreText(subtitle, new FormatInverter(fmt), " "))
                        .append(nl);
            }
        }

        final SquirtgunPlayer finalTarget = target;
        plugin.getProfileManager().getFields().stream()
                .sorted(Comparator.comparingInt(ProfileField::getOrder)).forEach(field -> {
            if (!field.getKey().equals("subtitle")) {
                String value = field.getValue(finalTarget.getUuid());
                output[0] = output[0].append(fmt.formatMain(field.getDisplayName() + ": "))
                        .append(serialiseField(field, value, fmt))
                        .append(nl);
            }
        });
        output[0] = output[0].append(nl).append(TextFormatter.formatTitle("*", fmt));
        return output[0];
    }
}
