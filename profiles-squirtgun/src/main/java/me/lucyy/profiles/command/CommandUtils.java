package me.lucyy.profiles.command;

import me.lucyy.profiles.api.ProfileField;
import me.lucyy.profiles.field.SimpleProfileField;
import me.lucyy.squirtgun.format.FormatProvider;
import me.lucyy.squirtgun.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class CommandUtils {

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
