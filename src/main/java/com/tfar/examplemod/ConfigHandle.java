package com.tfar.examplemod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

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
        List<Block> blocks = new ArrayList<>();
        stringJsonElementEntry.getValue().getAsJsonArray().forEach(jsonElement -> {
          String s = jsonElement.getAsString();
          try {

            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
            if (block == null || block.isAir(block.getDefaultState()))
              System.out.println(s + " is not a valid block!");
            else blocks.add(block);
          } catch (Exception e) {
            System.out.println(e + s);
          }

        });
        configs.put(Double.parseDouble(stringJsonElementEntry.getKey()), blocks);
      });

    } catch (IOException ofcourse) {
      throw new RuntimeException(ofcourse);
    }
    modifierMap.clear();
    configs.forEach((aDouble, blocks) -> blocks.forEach(block -> modifierMap.put(block, aDouble)));
  }

  public static final Map<Block, Double> modifierMap = new HashMap<>();
  private static final Map<Double, List<Block>> configs = new HashMap<>();

}
