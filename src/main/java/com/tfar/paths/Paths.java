package com.tfar.paths;

import com.tfar.paths.config.BlockConfigHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.paths.paths.Tags;

import java.util.UUID;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class Paths {
    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
    public static final UUID MODIFIER_UUID = UUID.fromString("dd683d7b-6362-4fb8-8adf-f6059fcd7f2b");

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        BlockConfigHandler.handle();
    }
}
