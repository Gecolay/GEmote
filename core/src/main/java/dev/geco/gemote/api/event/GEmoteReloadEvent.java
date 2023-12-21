package dev.geco.gemote.api.event;

import org.jetbrains.annotations.*;

import org.bukkit.event.*;
import org.bukkit.event.server.*;

import dev.geco.gemote.GEmoteMain;

public class GEmoteReloadEvent extends PluginEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final GEmoteMain GPM;

    public GEmoteReloadEvent(GEmoteMain GPluginMain) {
        super(GPluginMain);
        GPM = GPluginMain;
    }

    public @NotNull GEmoteMain getPlugin() { return GPM; }

    public @NotNull HandlerList getHandlers() { return HANDLERS; }

    public static HandlerList getHandlerList() { return HANDLERS; }

}