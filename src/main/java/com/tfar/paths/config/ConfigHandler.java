package com.tfar.paths.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tfar.paths.paths.Tags;

@Config(modid = Tags.MOD_ID)
public class ConfigHandler {
    @Config.Name("Modify FOV")
    public static boolean modifyFOV = false;

    @Config.Name("Reload Block Configs")
    public static boolean reloadBlockConfigs = false;

    @Mod.EventBusSubscriber(modid = Tags.MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Tags.MOD_ID)) {
                ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
                if (reloadBlockConfigs) {
                    BlockConfigHandler.handle();
                    reloadBlockConfigs = false;
                    ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
                }
            }
        }
    }
}
