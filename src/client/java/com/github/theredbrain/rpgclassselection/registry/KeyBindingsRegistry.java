package com.github.theredbrain.rpgclassselection.registry;

import com.github.theredbrain.rpgclassselection.RPGClassSelection;
import com.github.theredbrain.rpgclassselection.config.ServerConfig;
import com.github.theredbrain.rpgclassselection.network.packet.OpenClassSelectionScreenPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(value = EnvType.CLIENT)
public class KeyBindingsRegistry {

	public static KeyBinding openClassSelectionScreen;
	public static boolean openClassSelectionScreenBoolean;

	public static void registerKeyBindings() {
		KeyBindingsRegistry.openClassSelectionScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.rpgclassselection.open_class_selection_screen",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_K,
				KeyBinding.GAMEPLAY_CATEGORY
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (KeyBindingsRegistry.openClassSelectionScreen.wasPressed()) {
				if (!openClassSelectionScreenBoolean) {
					openClassSelectionScreen(client);
				}
				openClassSelectionScreenBoolean = true;
			} else if (openClassSelectionScreenBoolean) {
				openClassSelectionScreenBoolean = false;
			}
		});
	}

	public static void openClassSelectionScreen(MinecraftClient client) {
		if (client.player != null) {
			ServerConfig serverConfig = RPGClassSelectionConfigs.SERVER_CONFIG;
			if (serverConfig.hotkeySettings.enable_class_selection_hotkey) {
				ClientPlayNetworking.send(new OpenClassSelectionScreenPacket("", false, serverConfig.hotkeySettings.allow_changing_class, serverConfig.hotkeySettings.allow_changing_upgrades));
			} else {
				client.player.sendMessage(Text.translatable("hud.message.class_selection_hot_key_was_disabled"), true);
			}
		}
	}
}
