package com.github.theredbrain.rpgclassselection.registry;

import com.github.theredbrain.rpgclassselection.config.ServerConfig;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;

public class RPGClassSelectionConfigs {
	//instance of your config loaded from file and automatically registered to the SyncedConfigRegistry and ClientConfigRegistry using the getId() method
	//ConfigApiJava can come in handy to avoid pernicious compiler errors depending on your IDE and gradle setup.
	public static ServerConfig SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new);

	//Init function would be called in ModInitializer or some other entrypoint. Not strictly necessary if loading on-reference is ok.
	public static void bootstrap() {}
}
