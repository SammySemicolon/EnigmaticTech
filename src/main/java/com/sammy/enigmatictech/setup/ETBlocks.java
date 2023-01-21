package com.sammy.enigmatictech.setup;

import com.sammy.enigmatictech.EnigmaticTechMod;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorBlock;
import com.sammy.enigmatictech.content.block.fabricator.FabricatorBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Function;

import static com.sammy.enigmatictech.EnigmaticTechMod.path;


public class ETBlocks {
    public static final Registrate BLOCK_REGISTRATE = EnigmaticTechMod.registrate();

    public static final BlockEntry<FabricatorBlock<FabricatorBlockEntity>> FABRICATOR = setupBlock("fabricator", p -> new FabricatorBlock<>(p).<FabricatorBlock<FabricatorBlockEntity>>setBlockEntity(ETBlockEntities.FABRICATOR), BlockBehaviour.Properties.of(Material.METAL).strength(5f, 10f).sound(SoundType.METAL))
            .blockstate((ctx, p) -> p.getVariantBuilder(ctx.get()).forAllStates(s -> {
                String name = Registry.BLOCK.getKey(ctx.get()).getPath();
                ModelFile modelFile = p.models().cubeBottomTop(name, path("block/" + name + "_side"), path("block/" + name + "_bottom"), path("block/" + name + "_top"));
                return ConfiguredModel.builder().modelFile(modelFile).build();
            }))
            .simpleItem()
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .register();

    public static <T extends Block> BlockBuilder<T, Registrate> setupBlock(String name, NonNullFunction<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties) {
        return BLOCK_REGISTRATE.block(name, factory).properties((x) -> properties);
    }

    public static void register() {
    }
}