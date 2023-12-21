package dev.geco.gemote.util;

import org.bukkit.entity.*;

import dev.geco.gemote.GEmoteMain;

public class EnvironmentUtil {

    private final GEmoteMain GPM;

    public EnvironmentUtil(GEmoteMain GPluginMain) { GPM = GPluginMain; }

    public boolean isInAllowedWorld(Entity Entity) {

        boolean allowed = !GPM.getCManager().WORLDBLACKLIST.contains(Entity.getWorld().getName());

        if(!GPM.getCManager().WORLDWHITELIST.isEmpty() && !GPM.getCManager().WORLDWHITELIST.contains(Entity.getWorld().getName())) allowed = false;

        return allowed || GPM.getPManager().hasPermission(Entity, "ByPass.World", "ByPass.*");
    }

}