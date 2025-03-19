package com.tfar.paths.compat.crafttweaker;

import com.tfar.paths.config.PathValue;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.Block;
import org.apache.commons.lang3.tuple.Pair;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static com.tfar.paths.config.BlockConfigHandler.MODIFIER_MAP;

@ZenRegister
@ZenClass("mods.paths.Paths")
public class CTPaths {
    public static final List<IAction> INIT_ADD = new LinkedList<>();

    @ZenMethod
    public static void addBlockConfig(double speedMultiplier, int maxDistance, boolean requiresLineOfSight, IBlock applyBlock, @Nullable int[] meta) {
        INIT_ADD.add((new ActionAddBlockConfig(speedMultiplier, maxDistance, requiresLineOfSight, applyBlock, meta)));
    }


    private static class ActionAddBlockConfig implements IAction {
        double speedMultiplier;
        int maxDistance;
        boolean requiresLineOfSight;
        IBlock applyBlock;
        int[] meta;

        protected ActionAddBlockConfig(double speedMultiplier, int maxDistance, boolean requiresLineOfSight, IBlock applyBlock, @Nullable int[] meta) {
            this.speedMultiplier = speedMultiplier;
            this.maxDistance = maxDistance;
            this.requiresLineOfSight = requiresLineOfSight;
            this.applyBlock = applyBlock;
            this.meta = meta;
        }

        @Override
        public void apply() {
            PathValue pathValue = new PathValue(speedMultiplier, maxDistance, requiresLineOfSight);
            Block block = CraftTweakerMC.getBlock(applyBlock);
            if (meta != null) {
                Arrays.stream(meta).forEach(meta -> MODIFIER_MAP.put(Pair.of(block, meta), pathValue));
            } else {
                IntStream.range(0, 16).forEach(i -> MODIFIER_MAP.put(Pair.of(block, i), pathValue));
            }
        }

        @Override
        public String describe() {
            return "Add path block config.";
        }
    }
}
