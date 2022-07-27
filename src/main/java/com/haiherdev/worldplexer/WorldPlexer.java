package com.haiherdev.worldplexer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldPlexer implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("worldplexer");

	private MinecraftServer server;
	private PlexSaveHandler plexSaveHandler;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world, this is Worldplexer!");

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            this.onServerStarting(server);
        });

		// Register commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			Commands.registerCommands(dispatcher);
        });
	}

	private void onServerStarting(MinecraftServer server) {
		this.server = server;
		this.plexSaveHandler = new PlexSaveHandler(server);
		this.plexSaveHandler.loadWorlds();
	}
}
