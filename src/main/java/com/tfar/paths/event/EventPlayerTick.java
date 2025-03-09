package com.tfar.paths.event;

import com.tfar.paths.Paths;
import com.tfar.paths.config.BlockConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import tfar.paths.paths.Tags;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class EventPlayerTick {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.world;
        BlockPos pos = player.getPosition();
        IAttributeInstance attributeInstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        Triple<Double, Integer, Boolean> triple = null;
        IBlockState state;
        Block block;
        int iter = 5;
        int meta;

        while (iter > 0 && triple == null) {
            iter--;
            state = world.getBlockState(pos.down(iter));
            block = state.getBlock();
            meta = state.getBlock().getMetaFromState(state);
            triple = BlockConfigHandler.MODIFIER_MAP.get(Pair.of(block, meta));
        }

        if (triple != null) {
            int maxDistance = triple.getMiddle();
            boolean requiresLoS = triple.getRight();
            if (iter <= maxDistance && (!requiresLoS || iter < 2)) {
                double modify = Math.max(triple.getLeft() - 1, -.5);
                Paths.MODIFIER.setAmount(modify);
                attributeInstance.removeModifier(Paths.MODIFIER);
                attributeInstance.applyModifier(Paths.MODIFIER);
            } else {
                Paths.MODIFIER.setAmount(0);
                attributeInstance.removeModifier(Paths.MODIFIER);
                attributeInstance.applyModifier(Paths.MODIFIER);
            }
        } else {
            Paths.MODIFIER.setAmount(0);
            attributeInstance.removeModifier(Paths.MODIFIER);
            attributeInstance.applyModifier(Paths.MODIFIER);
        }
    }
}
