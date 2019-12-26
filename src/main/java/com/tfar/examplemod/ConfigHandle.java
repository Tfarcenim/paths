package com.tfar.examplemod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.*;

public class ConfigHandle {

  public static Gson g = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  public static final String location = "config/paths.json";

  public static void handle() {
    File file = new File(location);
    if (!file.exists()) writeDefaultConfig();
    load();
  }

  public static void writeDefaultConfig() {
    try {
      FileWriter writer = new FileWriter(location);
      writer.write(ExampleConfig.s);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void load() {
    try {
      configs.clear();
      Reader reader = new FileReader(location);
      JsonObject json = g.fromJson(reader, JsonObject.class);
      json.entrySet().forEach(stringJsonElementEntry -> {
        List<NestedConfigEntry> blocks = new ArrayList<>();
        stringJsonElementEntry.getValue().getAsJsonObject().entrySet().forEach(jsonElement -> {
          try {
            String s = jsonElement.getKey();
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
            if (block == null || block == Blocks.AIR)
              System.out.println(s + " is not a valid block!");
            else blocks.add(new NestedConfigEntry(block,new int[1]));
          } catch (Exception e) {
            System.out.println(e + jsonElement.toString());
          }

        });
        configs.put(Double.parseDouble(stringJsonElementEntry.getKey()), blocks);
      });

    } catch (IOException ofcourse) {
      throw new RuntimeException(ofcourse);
    }
    modifierMap.clear();
    configs.forEach((Double aDouble, List<NestedConfigEntry> nestedConfigEntries) -> {
      nestedConfigEntries.forEach(nestedConfigEntry -> {
        Arrays.stream(nestedConfigEntry.meta).forEach( meta -> {
          modifierMap.put(Pair.of(nestedConfigEntry.block, meta), aDouble);
        });
      });

    });
  }

  public static final Map<Pair<Block,Integer>, Double> modifierMap = new HashMap<>();
  private static final Map<Double, List<NestedConfigEntry>> configs = new HashMap<>();

}
