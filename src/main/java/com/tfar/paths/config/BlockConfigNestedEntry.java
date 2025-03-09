package com.tfar.paths.config;

import net.minecraft.block.Block;

import javax.annotation.Nullable;

public class BlockConfigNestedEntry {
    public final Block block;
    public final @Nullable Integer[] meta;

    public BlockConfigNestedEntry(Block block, @Nullable Integer[] meta) {
        this.block = block;
        this.meta = meta;
    }
}
