package com.tfar.paths.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tfar.paths.Paths;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class BlockConfigHandler {
    public static final String LOCATION = "config/paths.json";
    public static final Map<Pair<Block, Integer>, PathValue> MODIFIER_MAP = new HashMap<>();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final Map<PathValue, List<BlockConfigNestedEntry>> CONFIGS = new HashMap<>();

    public static void handle() {
        File file = new File(LOCATION);
        if (!file.exists()) writeDefaultConfig();
        load();
    }

    public static void writeDefaultConfig() {
        Paths.LOGGER.info("Block config file not found, writing defaults");
        try {
            FileWriter writer = new FileWriter(LOCATION);
            writer.write(BlockConfigDefault.defaults);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        Paths.LOGGER.info("Loading entries from block config file");
        try {
            CONFIGS.clear();
            Reader reader = new FileReader(LOCATION);
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            json.entrySet().forEach(stringJsonElementEntry -> {
                List<BlockConfigNestedEntry> blocks = new ArrayList<>();
                stringJsonElementEntry.getValue().getAsJsonObject().entrySet().forEach(jsonElement -> {
                    try {
                        String s = jsonElement.getKey();
                        ResourceLocation rl = new ResourceLocation(s);
                        if (Loader.isModLoaded(rl.getNamespace())) {
                            Block block = ForgeRegistries.BLOCKS.getValue(rl);
                            if (block == null || block == Blocks.AIR) {
                                Paths.LOGGER.error("{} may not exist or is not a valid block", s);
                            } else {
                                JsonArray jsonArray = jsonElement.getValue().getAsJsonArray();
                                List<Integer> list = new ArrayList<>();
                                jsonArray.forEach(jsonElement1 -> list.add(jsonElement1.getAsInt()));
                                blocks.add(new BlockConfigNestedEntry(block, list.toArray(new Integer[0])));
                                Paths.LOGGER.debug("Successfully added {} to the block list", block.getRegistryName());
                            }
                        }
                    } catch (Exception e) {
                        Paths.LOGGER.error("{} - {}", e, jsonElement.toString());
                    }
                });
                String[] strings = stringJsonElementEntry.getKey().split(",");
                double multi = Double.parseDouble(strings[0]);
                int distance = Integer.parseInt(strings[1]);
                boolean lineOfSight = Boolean.parseBoolean(strings[2]);
                CONFIGS.put(new PathValue(multi, distance, lineOfSight), blocks);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        MODIFIER_MAP.clear();
        CONFIGS.forEach((pathValue, nestedConfigEntries) -> nestedConfigEntries.forEach(nestedConfigEntry -> {
            if (nestedConfigEntry.meta != null && nestedConfigEntry.meta.length > 0) {
                Arrays.stream(nestedConfigEntry.meta).forEach(meta -> MODIFIER_MAP.put(Pair.of(nestedConfigEntry.block, meta), pathValue));
            } else {
                IntStream.range(0, 16).forEach(i -> MODIFIER_MAP.put(Pair.of(nestedConfigEntry.block, i), pathValue));
            }
        }));
    }
}
