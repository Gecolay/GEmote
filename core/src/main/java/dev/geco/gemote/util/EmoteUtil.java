package dev.geco.gemote.util;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.bukkit.*;
import org.bukkit.block.data.*;
import org.bukkit.configuration.file.*;
import org.bukkit.inventory.*;

import dev.geco.gemote.manager.*;
import dev.geco.gemote.objects.*;

public class EmoteUtil {

    public GEmote createEmoteFromRawData(File File) {

        try {

            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(File);

            List<String> pattern = configuration.getStringList("pattern");

            List<GEmotePart> parts = new ArrayList<>();

            for(String part : pattern) {

                Supplier<Stream<String>> sPart = () -> Arrays.stream(part.toLowerCase().split(" "));

                try {

                    Particle particle;

                    try { particle = Particle.valueOf(sPart.get().filter(s -> s.startsWith("particle:")).findFirst().orElse(":.").split(":")[1].toUpperCase()); } catch (Exception ignored) { continue; }

                    String sDelay = sPart.get().filter(s -> s.startsWith("delay:")).findFirst().orElse(":0").split(":")[1];

                    long delay = Long.parseLong(sDelay);

                    String sRepeat = sPart.get().filter(s -> s.startsWith("repeat:")).findFirst().orElse(":1").split(":")[1];

                    long repeat = Long.parseLong(sRepeat);

                    String sLoop = sPart.get().filter(s -> s.startsWith("loop:")).findFirst().orElse(":1").split(":")[1];

                    boolean loop = Byte.parseByte(sLoop) == 1;

                    String sAmount = sPart.get().filter(s -> s.startsWith("amount:")).findFirst().orElse(":1").split(":")[1];

                    int amount = Integer.parseInt(sAmount);

                    String sXOffset = sPart.get().filter(s -> s.startsWith("xoffset:")).findFirst().orElse(":0.0").split(":")[1];

                    double xoffset = Double.parseDouble(sXOffset);

                    String sYOffset = sPart.get().filter(s -> s.startsWith("yoffset:")).findFirst().orElse(":0.0").split(":")[1];

                    double yoffset = Double.parseDouble(sYOffset);

                    String sZOffset = sPart.get().filter(s -> s.startsWith("zoffset:")).findFirst().orElse(":0.0").split(":")[1];

                    double zoffset = Double.parseDouble(sZOffset);

                    String sExtra = sPart.get().filter(s -> s.startsWith("extra:")).findFirst().orElse(":1.0").split(":")[1];

                    double extra = Double.parseDouble(sExtra);

                    String[] sData = sPart.get().filter(s -> s.startsWith("data:")).findFirst().orElse(":.").split(":");

                    Object data = null;

                    if(Particle.DustOptions.class.equals(particle.getDataType())) {

                        data = new Particle.DustOptions(Color.fromRGB(Integer.parseInt(sData[1]), Integer.parseInt(sData[2]), Integer.parseInt(sData[3])), sData.length > 4 ? Float.parseFloat(sData[4]) : 1f);
                    } else if(BlockData.class.equals(particle.getDataType())) {

                        data = Material.getMaterial(sData[1].toUpperCase()).createBlockData();
                    } else if(ItemStack.class.equals(particle.getDataType())) {

                        data = new ItemStack(Material.getMaterial(sData[1].toUpperCase()));
                    } else if(Particle.DustTransition.class.equals(particle.getDataType())) {

                        data = new Particle.DustTransition(Color.fromRGB(Integer.parseInt(sData[1]), Integer.parseInt(sData[2]), Integer.parseInt(sData[3])), Color.fromRGB(Integer.parseInt(sData[4]), Integer.parseInt(sData[5]), Integer.parseInt(sData[6])), sData.length > 7 ? Float.parseFloat(sData[7]) : 1f);
                    } else if(Float.class.equals(particle.getDataType())) {

                        data = Float.parseFloat(sData[1]);
                    } else if(Integer.class.equals(particle.getDataType())) {

                        data = Integer.parseInt(sData[1]);
                    } else if(Vibration.class.equals(particle.getDataType())) { }

                    for(int i = 0; i < repeat; i++) parts.add(new GEmotePart(particle, delay, repeat, loop, amount, xoffset, yoffset, zoffset, extra, data));
                } catch (Throwable e) { e.printStackTrace(); }
            }

            String name = File.getName().toLowerCase();
            for(String extension : EmoteManager.FILE_EXTENSIONS) name = name.replace(extension, "");
            long loop = configuration.getLong("loop", 0);
            boolean head = configuration.getBoolean("head", true);
            boolean hideAsPassenger = configuration.getBoolean("hide-as-passenger", false);
            boolean hideAsVehicle = configuration.getBoolean("hide-as-vehicle", false);

            return new GEmote(name, parts, loop, head, hideAsPassenger, hideAsVehicle);
        } catch (Throwable e) { e.printStackTrace(); }

        return null;
    }

}