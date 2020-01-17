package com.tfar.examplemod;

import net.minecraft.block.Block;

import javax.annotation.Nullable;

public class NestedConfigEntry {

  public final Block block;
  public final @Nullable Integer[] meta;

  public NestedConfigEntry(Block block, Integer[] meta) {
    this.block = block;
    this.meta = meta;
  }
}
