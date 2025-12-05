package com.github.theredbrain.rpgclassselection.registry;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.config.ServerConfig;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.stat.Stats;

public class ServerEventRegistry {

	public static void initializeServerEvents() {
		ServerPlayerEvents.JOIN.register((player) -> {
			if (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.LEAVE_GAME)) < 1) {
				ServerConfig serverConfig = RPGClassSelection.SERVER_CONFIG;
				RPGClassSelection.openRPGClassSelectionScreen(
						player,
						"",
						serverConfig.firstJoinScreenSettings.allow_changing_class,
						serverConfig.firstJoinScreenSettings.allow_changing_upgrades
				);
			}
		});
	}
}
