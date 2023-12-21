package dev.geco.gemote.api;

import java.util.*;

import org.jetbrains.annotations.*;

import org.bukkit.entity.*;

import dev.geco.gemote.GEmoteMain;
import dev.geco.gemote.objects.*;

public class GEmoteAPI {

    /**
     * Returns the Plugin-Instance for GEmote
     * @author Gecolay
     * @since 1.0.0
     * @return Plugin-Instance
     */
    public static GEmoteMain getInstance() { return GEmoteMain.getInstance(); }

    /**
     * Checks if a Player is currently emoting
     * @author Gecolay
     * @since 1.0.0
     * @param Player Player for this Emote-Object
     * @return <code>true</code> if the Player is emoting
     */
    public static boolean isEmoting(@NotNull Player Player) {
        return getInstance().getEmoteManager().isEmoting(Player);
    }

    /**
     * Gets all Emote-Objects
     * @author Gecolay
     * @since 1.0.0
     * @return List of all Emote-Objects
     */
    public static HashMap<Player, GEmote> getEmotes() {
        return getInstance().getEmoteManager().getEmotes();
    }

    /**
     * Gets all available Emotes
     * @author Gecolay
     * @since 1.0.0
     * @return List of all available Emotes
     */
    public static List<GEmote> getAvailableEmotes() {
        return getInstance().getEmoteManager().getAvailableEmotes();
    }

    /**
     * Gets the Emote-Object of a Player
     * @author Gecolay
     * @since 1.0.0
     * @param Player Player for this Emote-Object
     * @return Emote-Object or <code>null</code> if there was no Emote-Object
     */
    public static GEmote getEmote(@NotNull Player Player) {
        return getInstance().getEmoteManager().getEmote(Player);
    }

    /**
     * Starts an Emote for a Player
     * @author Gecolay
     * @since 1.0.0
     * @param Player Player for this Emote-Object
     * @param Emote Emote
     * @return <code>true</code> or <code>false</code> if the creation was canceled
     */
    public static boolean startEmote(@NotNull Player Player, @NotNull GEmote Emote) {
        return getInstance().getEmoteManager().startEmote(Player, Emote);
    }

    /**
     * Stops an Emote from a Player
     * @author Gecolay
     * @since 1.0.0
     * @param Player Player for this Emote-Object
     * @return <code>true</code> or <code>false</code> if the deletion was canceled
     */
    public static boolean stopEmote(@NotNull Player Player) {
        return getInstance().getEmoteManager().stopEmote(Player);
    }

    /**
     * Stops an Emote from a Player
     * @author Gecolay
     * @since 1.5.2
     * @param Player Player for this Emote-Object
     * @param SaveRestore Defines whether the emote is saved for restore
     * @return <code>true</code> or <code>false</code> if the deletion was canceled
     */
    public static boolean stopEmote(@NotNull Player Player, boolean SaveRestore) {
        return getInstance().getEmoteManager().stopEmote(Player, SaveRestore);
    }

}