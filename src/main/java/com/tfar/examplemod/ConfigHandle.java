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
import org.apache.commons.lang3.tuple.Triple;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

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
            if (block == null || block == Blocks.AIR) {
              System.out.println(s + " may not exist or is not a valid block!");
            }
            else {
              JsonArray jsonArray = jsonElement.getValue().getAsJsonArray();
              List<Integer> list = new ArrayList<>();
              jsonArray.forEach(jsonElement1 -> list.add(jsonElement1.getAsInt()));
              blocks.add(new NestedConfigEntry(block,list.toArray(new Integer[0])));
            }
          } catch (Exception e) {
            System.out.println(e + jsonElement.toString());
          }

        });
        String[] strings = stringJsonElementEntry.getKey().split(",");
        double multi = Double.parseDouble(strings[0]);
        int distance = Integer.parseInt(strings[1]);
        boolean lineofsight = Boolean.parseBoolean(strings[2]);
        configs.put(Triple.of(multi,distance,lineofsight), blocks);
      });

    } catch (Exception ofcourse) {
      ofcourse.printStackTrace();
    }
    modifierMap.clear();
    configs.forEach((Triple<Double,Integer,Boolean> tripleDoubleIntBoolean, List<NestedConfigEntry> nestedConfigEntries) -> {
      nestedConfigEntries.forEach(nestedConfigEntry -> {
        if (nestedConfigEntry.meta != null && nestedConfigEntry.meta.length > 0) {
          Arrays.stream(nestedConfigEntry.meta).forEach( meta -> {
            modifierMap.put(Pair.of(nestedConfigEntry.block, meta), tripleDoubleIntBoolean);
          });
        } else {
          IntStream.range(0,16).forEach(i -> modifierMap.put(Pair.of(nestedConfigEntry.block, i), tripleDoubleIntBoolean));
        }
      });
    });
  }

  public static final Map<Pair<Block,Integer>, Triple<Double,Integer,Boolean>> modifierMap = new HashMap<>();

  private static final Map<Triple<Double,Integer,Boolean>, List<NestedConfigEntry>> configs = new HashMap<>();

}
