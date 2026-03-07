package com.github.theredbrain.rpgclassselection.config;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;

public class ClientConfig extends Config {

	public ClientConfig() {
		super(RPGClassSelection.identifier("client"));
	}

	public ValidatedColor class_name_text_colour = new ValidatedColor(0, 0, 0, 255);

	public ValidatedColor class_description_text_colour = new ValidatedColor(255, 255, 255, 255);

}
