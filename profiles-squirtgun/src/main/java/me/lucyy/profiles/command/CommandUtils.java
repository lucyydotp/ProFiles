package me.lucyy.profiles.command;

import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.api.SettableProfileField;
import me.lucyy.profiles.field.SimpleProfileField;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandUtils {

    public static List<String> tabCompleteSettable(Collection<ProfileField> fields, String input) {
        List<String> out = new ArrayList<>();
        for (ProfileField field : fields) {
            if (field instanceof SettableProfileField && field.getKey().toLowerCase().startsWith(input.toLowerCase()))
                out.add(field.getKey());
        }
        return out;
    }

    public static SettableProfileField getSettableField(ProfileManager manager, String name) throws AssertionError {
        ProfileField field = manager.getField(name);
        if (field == null) throw new AssertionError("The field '" + name + "' doesn't exist.");


        if (!(field instanceof SettableProfileField))
            throw new AssertionError("This field can't be set manually.");

        return (SettableProfileField) field;
    }

    public static Component serialiseField(ProfileField field, String value, FormatProvider cfg) {
		if (value.equals("")) return cfg.formatAccent("<empty>");
        Component valOut;
        if (field instanceof SimpleProfileField && !((SimpleProfileField) field).allowsColour()) {
            return cfg.formatAccent(value.replace("&.", ""));
        }
        valOut = TextFormatter.format(value, null, true);
        if (PlainTextComponentSerializer.plainText().serialize(valOut).equals(value)) {
            return cfg.formatAccent(value);
        }
        return valOut;
    }
}
