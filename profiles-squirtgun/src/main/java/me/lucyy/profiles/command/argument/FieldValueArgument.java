package me.lucyy.profiles.command.argument;

import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.SettableProfileField;
import me.lucyy.profiles.field.SimpleProfileField;
import me.lucyy.squirtgun.command.argument.AbstractArgument;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Queue;

public class FieldValueArgument extends AbstractArgument<String> {

    private final CommandArgument<ProfileField> fieldArgument;

    public FieldValueArgument(CommandArgument<ProfileField> fieldArgument) {
        super("value", "The field's value", false);
        this.fieldArgument = fieldArgument;
    }

    @Override
    public boolean isOptional() {
        return fieldArgument.isOptional();
    }

    @Override
    public String getValue(Queue<String> args, CommandContext<? extends PermissionHolder> context) {
        ProfileField field = context.getArgumentValue(fieldArgument);
        if (field == null) {
            return null;
        }

        String joined = String.join(" ", args);

        if (field instanceof SimpleProfileField && ((SimpleProfileField) field).allowsColour()) {
            return joined;
        }
        return PlainTextComponentSerializer.plainText().serialize(Component.text(joined));

    }

    @Override
    public @Nullable List<String> tabComplete(Queue<String> args, CommandContext<? extends PermissionHolder> context) {
        ProfileField field = context.getArgumentValue(fieldArgument);

        String out = field instanceof SettableProfileField ? "<"
                + field.displayName().toLowerCase(Locale.ROOT) + ">" : "You can't set this field!";
        if (field instanceof SimpleProfileField && ((SimpleProfileField) field).allowsColour()) {
            out = "<this field supports colour>";
        }
        return List.of(out);
    }
}
