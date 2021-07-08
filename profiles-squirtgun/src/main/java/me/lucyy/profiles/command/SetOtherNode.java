package me.lucyy.profiles.command;

import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.api.SettableProfileField;
import me.lucyy.profiles.command.argument.FieldArgument;
import me.lucyy.profiles.command.argument.FieldValueArgument;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.argument.OnlinePlayerArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.node.AbstractNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import me.lucyy.squirtgun.platform.audience.SquirtgunPlayer;
import me.lucyy.squirtgun.platform.audience.SquirtgunUser;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetOtherNode extends AbstractNode<PermissionHolder> {

    private final CommandArgument<SquirtgunPlayer> playerArg;
    private final CommandArgument<ProfileField> fieldArg;
    private final CommandArgument<String> valueArg;

    public SetOtherNode(ProfileManager profileManager) {
        super("setother", "Sets a field for another player.", "profiles.moderator");
        playerArg = new OnlinePlayerArgument("player", "The player to set the field for",
                false, null); // TODO - platform
        fieldArg = new FieldArgument(profileManager);
        valueArg = new FieldValueArgument(fieldArg);
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return List.of(playerArg, fieldArg, valueArg);
    }

    @Override
    public @Nullable Component execute(CommandContext<PermissionHolder> context) {
        final FormatProvider fmt = context.getFormat();

        ProfileField field = context.getArgumentValue(fieldArg);
        if (!(field instanceof SettableProfileField)) {
            return fmt.getPrefix().append(fmt.formatMain("This field can't be set manually."));
        }

        SquirtgunPlayer player = (SquirtgunPlayer) context.getTarget();
        String value = context.getArgumentValue(valueArg);

        ((SettableProfileField) field).setValue(player.getUuid(), value);
        return fmt.getPrefix()
                .append(fmt.formatMain("Cleared '"))
                .append(fmt.formatAccent(field.getDisplayName()))
                .append(fmt.formatMain("' for player "))
                .append(fmt.formatAccent(player.getUsername()))
                .append(fmt.formatMain("."));
    }
}
