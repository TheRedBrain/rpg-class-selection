package com.github.theredbrain.rpgclassselection.config;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.config.Config;

@ConvertFrom(fileName = "client.json5", folder = "rpgclassselection")
public class ClientConfig extends Config {

	public ClientConfig() {
		super(RPGClassSelection.identifier("client"));
	}

}
