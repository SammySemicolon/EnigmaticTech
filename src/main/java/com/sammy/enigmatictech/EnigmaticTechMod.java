package com.sammy.enigmatictech;

import com.sammy.enigmatictech.setup.ETBlockEntities;
import com.sammy.enigmatictech.setup.ETBlocks;
import com.sammy.enigmatictech.setup.ETItems;
import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Random;

import static com.sammy.enigmatictech.EnigmaticTechMod.MODID;

@Mod(MODID)
public class EnigmaticTechMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "enigmatic_tech";
	public static final Random RANDOM = new Random();
	private static final Lazy<Registrate> REGISTRATE = Lazy.of(() -> Registrate.create(MODID));

	public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(MODID) {
		@Override
		@Nonnull
		public ItemStack makeIcon() {
			return new ItemStack(ETBlocks.FABRICATOR.get());
		}
	};

	public static Registrate registrate() {
		return REGISTRATE.get();
	}

	public EnigmaticTechMod() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		registrate().creativeModeTab(() -> ITEM_GROUP);

		ETBlockEntities.register();
		ETItems.register();
		ETBlocks.register();

	}

	public static ResourceLocation path(String path) {
		return new ResourceLocation(MODID, path);
	}
}