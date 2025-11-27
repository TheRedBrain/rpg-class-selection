package com.github.theredbrain.rpgclassselection;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPGClassSelection implements ModInitializer {
	public static final String MOD_ID = "rpgclassselection";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
//	public static ServerConfig SERVER_CONFIG;

	public static final boolean isRunesLoaded = FabricLoader.getInstance().isModLoaded("runes");
	public static final boolean isSpellEngineLoaded = FabricLoader.getInstance().isModLoaded("spell_engine");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing class selection!");
//		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new);

//		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(MOD_ID);
//		if (modContainer.isPresent()) {
//			ResourceManagerHelper.registerBuiltinResourcePack(identifier("minecrawl_data_pack"), modContainer.get(), Text.translatable("resourcepack.minecrawl.minecrawl_data_pack.name"), ResourcePackActivationType.ALWAYS_ENABLED);
//			ResourceManagerHelper.registerBuiltinResourcePack(identifier("minecrawl_item_merging"), modContainer.get(), Text.translatable("resourcepack.minecrawl.minecrawl_item_merging.name"), ResourcePackActivationType.DEFAULT_ENABLED);
//			ResourceManagerHelper.registerBuiltinResourcePack(identifier("minecrawl_rpg_inventory_integration"), modContainer.get(), Text.translatable("resourcepack.minecrawl.minecrawl_rpg_inventory_integration.name"), ResourcePackActivationType.DEFAULT_ENABLED);
//			ResourceManagerHelper.registerBuiltinResourcePack(identifier("minecrawl_resource_pack"), modContainer.get(), Text.translatable("resourcepack.minecrawl.minecrawl_resource_pack.name"), ResourcePackActivationType.DEFAULT_ENABLED);
//		}
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}