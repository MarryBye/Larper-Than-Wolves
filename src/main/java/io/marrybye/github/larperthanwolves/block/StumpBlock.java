package io.marrybye.github.larperthanwolves.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import java.util.function.Supplier;

public class StumpBlock extends RotatedPillarBlock {
    private final Supplier<Block> baseLog;

    public StumpBlock(Supplier<Block> baseLog, Properties properties) {
        super(properties);
        this.baseLog = baseLog;
    }

    public Block getBaseLog() {
        return baseLog.get();
    }
}
