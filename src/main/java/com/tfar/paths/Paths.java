package com.tfar.paths;

import com.tfar.paths.config.BlockConfigHandler;
import crafttweaker.CraftTweakerAPI;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.paths.paths.Tags;

import java.util.UUID;

import static com.tfar.paths.compat.crafttweaker.CTPaths.INIT_ADD;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class Paths {
    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
    public static final UUID MODIFIER_UUID = UUID.fromString("dd683d7b-6362-4fb8-8adf-f6059fcd7f2b");

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        BlockConfigHandler.handle();

        if (Loader.isModLoaded("crafttweaker")) {
            try {
                INIT_ADD.forEach(CraftTweakerAPI::apply);
            } catch (Exception e) {
                e.printStackTrace();
                CraftTweakerAPI.logError("Error while applying actions", e);
            }
        }
    }
}
