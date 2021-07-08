package me.lucyy.profiles.command.argument;

import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.squirtgun.command.argument.AbstractArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class FieldArgument extends AbstractArgument<ProfileField> {

    private final ProfileManager manager;

    public FieldArgument(ProfileManager manager) {
        super("field", "The profile field", false);
        this.manager = manager;
    }

    @Override
    public ProfileField getValue(Queue<String> args, CommandContext<? extends PermissionHolder> context) {
        String name = args.poll();
        if (name == null) return null;
        return manager.getField(name);
    }

    @Override
    public @Nullable List<String> tabComplete(Queue<String> args, CommandContext<? extends PermissionHolder> context) {
        String name = args.poll();

        // not optional - this will always be non-null
        Objects.requireNonNull(name);
        return manager.getFields().stream()
                .map(ProfileField::getKey)
                .filter(key -> key.startsWith(name.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }
}
