package me.lucyy.profiles.command;

import me.lucyy.common.command.FormatProvider;
import me.lucyy.common.format.TextFormatter;
import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.api.ProfileManager;
import me.lucyy.profiles.api.SettableProfileField;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

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

    public static String formatIfNotAlready(String in, FormatProvider format) {
        if (in.contains(ChatColor.COLOR_CHAR + "")) return in;
        String formatted = TextFormatter.format(in, null, true);
        return formatted.equals(in) ? format.formatAccent(in) : formatted;
    }
}
