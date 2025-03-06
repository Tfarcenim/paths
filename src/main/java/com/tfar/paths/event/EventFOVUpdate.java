package com.tfar.paths.event;

import com.tfar.paths.Paths;
import com.tfar.paths.config.ConfigHandler;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import tfar.paths.paths.Tags;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID, value = Side.CLIENT)
public class EventFOVUpdate {
    private static float oldFov;

    @SubscribeEvent
    public static void onFOVUpdate(FOVUpdateEvent event) {
        if (Paths.MODIFIER.getAmount() != 0 && !ConfigHandler.modifyFOV) {
            event.setNewfov(oldFov);
        } else {
            oldFov = event.getFov();
        }
    }
}
