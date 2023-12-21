package dev.geco.gemote.cmd;

import org.jetbrains.annotations.*;

import org.bukkit.command.*;
import org.bukkit.entity.*;

import dev.geco.gemote.GEmoteMain;
import dev.geco.gemote.objects.*;

public class GEmoteCommand implements CommandExecutor {

    private final GEmoteMain GPM;

    public GEmoteCommand(GEmoteMain GPluginMain) { GPM = GPluginMain; }

    @Override
    public boolean onCommand(@NotNull CommandSender Sender, @NotNull Command Command, @NotNull String Label, String[] Args) {

        if(!(Sender instanceof Player)) {

            GPM.getMManager().sendMessage(Sender, "Messages.command-sender-error");
            return true;
        }

        Player player = (Player) Sender;

        if(!GPM.getPManager().hasPermission(Sender, "Emote", "Emote.*")) {

            GPM.getMManager().sendMessage(Sender, "Messages.command-permission-error");
            return true;
        }

        if(Args.length == 0) {

            if(!GPM.getEmoteManager().isEmoting(player)) {

                GPM.getMManager().sendMessage(Sender, "Messages.action-emote-none-error");
                return true;
            }

            if(!GPM.getEmoteManager().stopEmote(player)) {

                GPM.getMManager().sendMessage(Sender, "Messages.action-emote-stop-error");
                return true;
            }

            GPM.getMManager().sendMessage(Sender, "Messages.action-emote-stop");
            return true;
        }

        GEmote emote = GPM.getEmoteManager().getEmoteByName(Args[0]);

        if(emote == null) {

            GPM.getMManager().sendMessage(Sender, "Messages.action-emote-exist-error", "%Emote%", Args[0]);
            return true;
        }

        if(!player.isValid()) {

            GPM.getMManager().sendMessage(Sender, "Messages.action-emote-now-error");
            return true;
        }

        if(!GPM.getPManager().hasPermission(Sender, "Emote." + emote.getId(), "Emote.*")) {

            GPM.getMManager().sendMessage(Sender, "Messages.action-emote-exist-error", "%Emote%", emote.getId());
            return true;
        }

        if(!GPM.getEnvironmentUtil().isInAllowedWorld(player)) {

            GPM.getMManager().sendMessage(Sender, "Messages.action-emote-world-error");
            return true;
        }

        if(!GPM.getPManager().hasPermission(Sender, "ByPass.Region", "ByPass.*")) {

            if(GPM.getWorldGuardLink() != null && !GPM.getWorldGuardLink().checkFlag(player.getLocation(), GPM.getWorldGuardLink().getFlag("emote"))) {

                GPM.getMManager().sendMessage(Sender, "Messages.action-emote-region-error");
                return true;
            }
        }

        GPM.getEmoteManager().startEmote(player, emote);
        return true;
    }

}