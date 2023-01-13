package com.sammy.enigmatictech.setup;

import com.sammy.enigmatictech.EnigmaticTechMod;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ETBlockEntities {

    public static final Registrate BLOCK_ENTITY_REGISTRATE = EnigmaticTechMod.registrate();
    public static final BlockEntityEntry<FabricatorBlockEntity> FABRICATOR =
            BLOCK_ENTITY_REGISTRATE.blockEntity("fabricator", FabricatorBlockEntity::new).validBlocks(ETBlocks.FABRICATOR).register();


    public static void register() {
    }
}