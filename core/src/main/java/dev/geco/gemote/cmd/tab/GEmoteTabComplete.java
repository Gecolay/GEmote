package dev.geco.gemote.cmd.tab;

import java.util.*;

import org.jetbrains.annotations.*;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import dev.geco.gemote.GEmoteMain;
import dev.geco.gemote.objects.*;

public class GEmoteTabComplete implements TabCompleter {

    private final GEmoteMain GPM;

    public GEmoteTabComplete(GEmoteMain GPluginMain) { GPM = GPluginMain; }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender Sender, @NotNull Command Command, @NotNull String Label, String[] Args) {

        List<String> complete = new ArrayList<>(), completeStarted = new ArrayList<>();

        if(Sender instanceof Player) {

            if(Args.length == 1) {

                if(GPM.getPManager().hasPermission(Sender, "Emote", "Emote.*")) for(GEmote emote : GPM.getEmoteManager().getAvailableEmotes()) if(GPM.getPManager().hasPermission(Sender, "Emote." + emote.getId(), "Emote.*")) complete.add(emote.getId());

                if(!Args[Args.length - 1].isEmpty()) {

                    for(String entry : complete) if(entry.toLowerCase().startsWith(Args[Args.length - 1].toLowerCase())) completeStarted.add(entry);

                    complete.clear();
                }
            }
        }

        return complete.isEmpty() ? completeStarted : complete;
    }

}