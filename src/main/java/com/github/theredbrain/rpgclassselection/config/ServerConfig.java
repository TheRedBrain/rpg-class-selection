package com.github.theredbrain.rpgclassselection.config;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.screen.ClassSelectionScreenHandler;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;

public class ServerConfig extends Config {

	public ServerConfig() {
		super(RPGClassSelection.identifier("server"));
	}

	public ValidatedEnum<ClassSelectionScreenHandler.ClassSelectionScreenType> class_selection_screen_type = new ValidatedEnum<>(ClassSelectionScreenHandler.ClassSelectionScreenType.RPG_SERIES);
	public ValidatedEnum<ClassSelectionScreenHandler.EmptyUpgradeMode> empty_upgrade_mode = new ValidatedEnum<>(ClassSelectionScreenHandler.EmptyUpgradeMode.NON_EMPTY_GROUPS);

	public HotkeySettings hotkeySettings = new HotkeySettings();

	public static class HotkeySettings extends ConfigSection {

		public boolean enable_class_selection_hotkey = true;
		public boolean allow_changing_class = true;
		public boolean allow_changing_upgrades = true;

	}
}
