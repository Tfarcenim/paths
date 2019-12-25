package com.tfar.examplemod;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Paths.MODID)
public class Paths
{
  // Directly reference a log4j logger.

  private static final UUID MODIFIER_UUID = UUID.fromString("dd683d7b-6362-4fb8-8adf-f6059fcd7f2b");
  private static final MutableAttributeModifier MODIFIER =
          (MutableAttributeModifier) new MutableAttributeModifier(MODIFIER_UUID, "path multiplier",
                  1, AttributeModifier.Operation.ADDITION).setSaved(false);

  public static final String MODID = "paths";

  private static final Logger LOGGER = LogManager.getLogger();

  public Paths() {
    // Register the setup method for modloading
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

    // Register ourselves for server and other game events we are interested in
    MinecraftForge.EVENT_BUS.register(this);
  }

  public static final Map<Block,UUID> map = new HashMap<>();

  private void setup(final FMLCommonSetupEvent event) {
    ConfigHandle.handle();
  }

  Block lastblock = Blocks.AIR;

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent
  public void onServerStarting(TickEvent.PlayerTickEvent event) {
    PlayerEntity player = event.player;
    World world = player.world;
    BlockPos pos = player.getPosition();
    IAttributeInstance iattributeinstance = player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
    Block block = world.getBlockState(pos.down()).getBlock();
    MODIFIER.setAmount(Math.max(ConfigHandle.modifierMap.getOrDefault(block,1d) - 1,-.1));
    iattributeinstance.removeModifier(MODIFIER);
    iattributeinstance.applyModifier(MODIFIER);
  }
}
