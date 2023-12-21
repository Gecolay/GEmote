package dev.geco.gemote.link;

import java.util.*;

import org.jetbrains.annotations.*;

import org.bukkit.*;
import org.bukkit.entity.*;

import me.clip.placeholderapi.expansion.*;

import dev.geco.gemote.GEmoteMain;

public class PlaceholderAPILink extends PlaceholderExpansion {

    private final GEmoteMain GPM;

    public PlaceholderAPILink(GEmoteMain GPluginMain) { GPM = GPluginMain; }

    @Override
    public boolean canRegister() { return GPM.isEnabled(); }

    @Override
    public @NotNull String getName() { return GPM.getDescription().getName(); }

    @Override
    public @NotNull String getIdentifier() { return GPM.NAME.toLowerCase(); }

    @Override
    public @NotNull String getAuthor() { return GPM.getDescription().getAuthors().toString(); }

    @Override
    public @NotNull String getVersion() { return GPM.getDescription().getVersion(); }

    @Override
    public @NotNull List<String> getPlaceholders() { return Arrays.asList("emoting"); }

    @Override
    public String onRequest(OfflinePlayer Player, @NotNull String Params) {

        if(Player == null) return null;

        if(Params.equalsIgnoreCase("emoting")) return Player.isOnline() ? "" + GPM.getEmoteManager().isEmoting((Player) Player) : "" + false;

        return null;
    }

}