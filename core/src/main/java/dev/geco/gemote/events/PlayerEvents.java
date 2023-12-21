package dev.geco.gemote.events;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import dev.geco.gemote.GEmoteMain;

public class PlayerEvents implements Listener {

    private final GEmoteMain GPM;

    public PlayerEvents(GEmoteMain GPluginMain) { GPM = GPluginMain; }

    @EventHandler
    public void PJoiE(PlayerJoinEvent Event) {

        Player player = Event.getPlayer();

        GPM.getUManager().loginCheckForUpdates(player);

        if(GPM.getCManager().RESTORE) GPM.getEmoteManager().restoreEmote(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void PQuiE(PlayerQuitEvent Event) {

        Player player = Event.getPlayer();

        GPM.getEmoteManager().stopEmote(player, true);
    }

}