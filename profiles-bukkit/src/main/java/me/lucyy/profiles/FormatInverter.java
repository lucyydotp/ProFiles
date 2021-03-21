package me.lucyy.profiles;

import me.lucyy.common.command.FormatProvider;

/**
 * A wrapper around a {@link FormatProvider}, swapping the main and accent
 * formatters.
 */
public class FormatInverter implements FormatProvider {
    private final FormatProvider base;

    public FormatInverter(FormatProvider base) {
        this.base = base;
    }
    @Override
    public String getMainColour() {
        return base.getAccentColour();
    }

    @Override
    public String formatMain(String input) {
        return base.formatAccent(input);
    }

    @Override
    public String formatMain(String s, String s1) {
        return base.formatAccent(s, s1);
    }

    @Override
    public String getAccentColour() {
        return base.getMainColour();
    }

    @Override
    public String formatAccent(String input) {
        return base.formatMain(input);
    }

    @Override
    public String formatAccent(String s, String s1) {
        return base.formatAccent(s, s1);
    }

    @Override
    public String getPrefix() {
        return base.getPrefix();
    }
}
