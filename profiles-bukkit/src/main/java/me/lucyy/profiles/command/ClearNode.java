package me.lucyy.profiles.command;

import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.api.SettableProfileField;
import me.lucyy.profiles.command.argument.FieldArgument;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.command.node.AbstractNode;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.platform.audience.SquirtgunPlayer;
import me.lucyy.squirtgun.platform.audience.SquirtgunUser;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class ClearNode extends AbstractNode<SquirtgunUser> {

    private final CommandArgument<ProfileField> fieldArg;

    public ClearNode(ProfileManager manager) {
        super("clear", "Clear a field.", null);
        fieldArg = new FieldArgument(manager);
    }

    @Override
    public @Nullable Component execute(CommandContext<SquirtgunUser> context) {
        final FormatProvider fmt = context.getFormat();
        if (!(context.getTarget() instanceof SquirtgunPlayer)) {
            return fmt.getPrefix().append(fmt.formatMain("This command can only be run by a player."));
        }

        ProfileField field = context.getArgumentValue(fieldArg);
        if (!(field instanceof SettableProfileField)) {
            return fmt.getPrefix().append(fmt.formatMain("This field can't be set manually."));
        }

        SquirtgunPlayer player = (SquirtgunPlayer) context.getTarget();
        ((SettableProfileField) field).clearValue(player.getUuid());
        return fmt.getPrefix().append(fmt.formatMain("Cleared " + field.getDisplayName() + "."));
    }
}
