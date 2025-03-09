package com.tfar.paths.event;

import com.tfar.paths.Paths;
import com.tfar.paths.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import tfar.paths.paths.Tags;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID, value = Side.CLIENT)
public class EventFOVUpdate {
    private static float oldFov;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onFOVUpdate(FOVUpdateEvent event) {
        AttributeModifier modifier = Minecraft.getMinecraft().player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(Paths.MODIFIER_UUID);
        if (modifier != null && !ConfigHandler.modifyFOV) {
            event.setNewfov(oldFov);
        } else {
            oldFov = event.getFov();
        }
    }
}
