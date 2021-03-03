package me.lucyy.profiles.field;

import me.lucyy.profiles.ProfileManagerImpl;
import me.lucyy.pronouns.api.PronounHandler;
import me.lucyy.pronouns.api.set.PronounSet;

import java.util.UUID;

public class ProNounsProfileField extends ProfileFieldBase {

    private final PronounHandler pronounHandler;

    public ProNounsProfileField(ProfileManagerImpl manager, String key, String displayName, PronounHandler handler) {
        super(manager, key, displayName);
        this.pronounHandler = handler;
    }

    @Override
    public String getValue(UUID player) {
        if (pronounHandler == null) return "";
        return PronounSet.friendlyPrintSet(pronounHandler.getUserPronouns(player));
    }
}
