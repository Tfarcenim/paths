package com.tfar.examplemod;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(modid = Paths.MODID, name = "Paths", version = "1.0")
public class Paths
{
  // Directly reference a log4j logger.

  private static final UUID MODIFIER_UUID = UUID.fromString("dd683d7b-6362-4fb8-8adf-f6059fcd7f2b");
  private static final MutableAttributeModifier MODIFIER =
          (MutableAttributeModifier) new MutableAttributeModifier(MODIFIER_UUID, "path multiplier",
                  1,1).setSaved(false);

  public static final String MODID = "paths";

  private static final Logger LOGGER = LogManager.getLogger();

  public Paths() {
    // Register the setup method for modloading
    EVENT_BUS.register(this);

  }

  public static final Map<Block,UUID> map = new HashMap<>();

  @Mod.EventHandler
  public void setup(final FMLInitializationEvent event) {
    ConfigHandle.handle();
  }

  @SubscribeEvent
  public void rightclickItem(PlayerInteractEvent.RightClickItem e){
    if (e.getEntityPlayer().capabilities.isCreativeMode && e.getItemStack().getItem() == Items.STICK){
      ConfigHandle.handle();
    }
  }

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent
  public void onServerStarting(TickEvent.PlayerTickEvent event) {
    EntityPlayer player = event.player;
    World world = player.world;
    BlockPos pos = player.getPosition();
    IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
    IBlockState state = world.getBlockState(pos.down());
    Block block = state.getBlock();
    int meta = state.getBlock().getMetaFromState(state);
    double modify = Math.max(ConfigHandle.modifierMap.getOrDefault(Pair.of(block,meta),1d)-1,-.5);
    MODIFIER.setAmount(modify);
    iattributeinstance.removeModifier(MODIFIER);
    iattributeinstance.applyModifier(MODIFIER);
  }
}
