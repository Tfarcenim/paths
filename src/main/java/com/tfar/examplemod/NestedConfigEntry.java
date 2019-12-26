package com.tfar.examplemod;

import net.minecraft.block.Block;

public class NestedConfigEntry {

  public final Block block;
  public final int[] meta;

  public NestedConfigEntry(Block block, int[] meta) {
    this.block = block;
    this.meta = meta;
  }
}
