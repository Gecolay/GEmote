package dev.geco.gemote.manager;

import org.bukkit.entity.*;
import org.bukkit.permissions.*;

import dev.geco.gemote.GEmoteMain;

public class PManager {

    private final GEmoteMain GPM;

    public PManager(GEmoteMain GPluginMain) { GPM = GPluginMain; }

    public boolean hasPermission(Permissible Permissible, String... Permissions) {

        if(!(Permissible instanceof Player)) return true;

        for(String permission : Permissions) {

            if(Permissible.isPermissionSet(GPM.NAME + "." + permission)) return Permissible.hasPermission(GPM.NAME + "." + permission);
            if(Permissible.hasPermission(GPM.NAME + "." + permission)) return true;
        }

        return Permissible.hasPermission(GPM.NAME + ".*");
    }

}