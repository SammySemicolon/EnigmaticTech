package com.sammy.enigmatictech.content.block.fabricator;

import team.lodestar.lodestone.systems.block.LodestoneEntityBlock;

public class FabricatorBlock<T extends FabricatorBlockEntity> extends LodestoneEntityBlock<T> {
    public FabricatorBlock(Properties properties) {
        super(properties);
    }
}
