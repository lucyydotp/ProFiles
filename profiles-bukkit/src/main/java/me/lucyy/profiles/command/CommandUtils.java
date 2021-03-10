package me.lucyy.profiles.command;

import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.api.SettableProfileField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class CommandUtils {

    private static final Predicate<ProfileField> isSettable = field -> field instanceof SettableProfileField;

    public static List<String> tabCompleteField(Collection<ProfileField> fields, String input, Predicate<ProfileField> check) {
        List<String> out = new ArrayList<>();
        for (ProfileField field : fields) {
            if (field.getKey().toLowerCase().startsWith(input.toLowerCase()) && check.test(field))
                out.add(field.getKey());
        }
        return out;
    }

    public static List<String> tabCompleteSettable(Collection<ProfileField> fields, String input) {
        return tabCompleteField(fields, input, isSettable);
    }

    public static SettableProfileField getSettableField(ProfileManager manager, String name) throws AssertionError {
        ProfileField field = manager.getField(name);
        if (field == null) throw new AssertionError("The field '" + name + "' doesn't exist.");


        if (!(field instanceof SettableProfileField))
            throw new AssertionError("This field can't be set manually.");

        return (SettableProfileField) field;
    }
}
