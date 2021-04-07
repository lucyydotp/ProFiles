package me.lucyy.profiles;

import me.lucyy.common.command.FormatProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

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
    public Component formatMain(@NotNull String input) {
        return base.formatAccent(input);
    }

    @Override
    public Component formatMain(@NotNull String s, @NotNull TextDecoration[] textDecorations) {
        return base.formatAccent(s, textDecorations);
    }

    @Override
    public Component formatAccent(@NotNull String input) {
        return base.formatMain(input);
    }

    @Override
    public Component formatAccent(@NotNull String s, @NotNull TextDecoration[] textDecorations) {
        return base.formatMain(s, textDecorations);
    }

    @Override
    public Component getPrefix() {
        return null;
    }

}
