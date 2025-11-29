package com.github.theredbrain.rpgclassselection.config;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.config.Config;

public class ServerConfig extends Config {

	public ServerConfig() {
		super(RPGClassSelection.identifier("server"));
	}

	public boolean enable_hotkey = true;
}
