package com.tfar.paths.event;

import com.tfar.paths.Paths;
import com.tfar.paths.config.BlockConfigHandler;
import com.tfar.paths.config.PathValue;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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

        if (!world.isRemote && event.phase == TickEvent.Phase.START) {

            BlockPos pos = player.getPosition();
            double playerY = player.posY;
            IAttributeInstance attributeInstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            PathValue pathValue = null;
            IBlockState state;
            Block block;
            int iter = 0;
            int meta;
            boolean hasLOS = true;

            while (iter < 8 && pathValue == null) {
                iter++;
                state = world.getBlockState(pos.down(iter));
                block = state.getBlock();
                meta = state.getBlock().getMetaFromState(state);
                pathValue = BlockConfigHandler.MODIFIER_MAP.get(Pair.of(block, meta));
                if (pathValue == null && state.getCollisionBoundingBox(world,pos) != null) {
                    hasLOS = false;
                }
            }

            if (pathValue != null) {
                BlockPos pathPos = pos.down(iter);
                int maxDistance = pathValue.maxDistance();
                boolean requiresLoS = pathValue.requiresLineOfSight();
                double distance = playerY - pathPos.getY();
                if (distance <= maxDistance && (!requiresLoS || hasLOS)) {
                    double modify = Math.max(pathValue.speedMultiplier() - 1, -.9375);
                    safeAddAttributeModifier(player, SharedMonsterAttributes.MOVEMENT_SPEED,
                            new AttributeModifier(Paths.MODIFIER_UUID, "Path Modifier", modify, 1).setSaved(false));
                } else {
                    attributeInstance.removeModifier(Paths.MODIFIER_UUID);
                }
            } else {
                attributeInstance.removeModifier(Paths.MODIFIER_UUID);
            }
        }
    }

    public static void safeAddAttributeModifier(EntityLivingBase living, IAttribute attribute, AttributeModifier modifier) {
        IAttributeInstance entityAttribute = living.getEntityAttribute(attribute);
        if (entityAttribute.getModifier(modifier.getID())!=null) {
            entityAttribute.removeModifier(modifier.getID());
        }
        entityAttribute.applyModifier(modifier);//this will throw if the modifier exists
    }
}
