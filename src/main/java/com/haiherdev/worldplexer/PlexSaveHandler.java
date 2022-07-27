package com.haiherdev.worldplexer;

import java.io.File;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.fabricmc.fabric.mixin.gamerule.GameRulesAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Key;
import net.minecraft.world.level.LevelProperties;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;
import xyz.nucleoid.fantasy.util.GameRuleStore;

public class PlexSaveHandler {
    
    private static final Logger logger = LogUtils.getLogger();

    private static final String DIMENSIONS = "dimensions";
    private static final String PLEX_NAMESPACE = "worldplexer";

    private final MinecraftServer server;

    public PlexSaveHandler(MinecraftServer server) {
        this.server = server;
    }

    public void loadWorlds() {
        File dimensionsDirectory = new File(this.server.getSavePath(WorldSavePath.ROOT).toFile(), DIMENSIONS + "/" + PLEX_NAMESPACE);
        if (!dimensionsDirectory.exists() || !dimensionsDirectory.isDirectory()) {
            logger.warn("[%s] doesn't exist or is not a directory", dimensionsDirectory.getPath());
            return;
        }

        logger.info("Loading WorldPlexer worlds");
        String[] dimensions = dimensionsDirectory.list();
        Fantasy fantasy = Fantasy.get(this.server);

        for (String d : dimensions) {
            fantasy.getOrOpenPersistentWorld(new Identifier(PLEX_NAMESPACE, d), 
                new RuntimeWorldConfig().setGenerator(this.server.getOverworld().getChunkManager().getChunkGenerator()));
            logger.info("Loaded [%s:%s]", PLEX_NAMESPACE, d);
        }
    }

    public void saveWorlds() {

    }

    private RuntimeWorldConfig generateConfigFromWorld(ServerWorld world) {
        RuntimeWorldConfig config = new RuntimeWorldConfig()
            .setSeed(world.getSeed())
            .setDifficulty(world.getDifficulty())
            .setDimensionType(world.getDimensionEntry())
            .setGenerator(server.getSaveProperties().getGeneratorOptions().getChunkGenerator())
            .setRaining(((LevelProperties) server.getSaveProperties()).getRainTime())
            .setSunny(((LevelProperties) server.getSaveProperties()).getClearWeatherTime())
            .setThundering(((LevelProperties) server.getSaveProperties()).getThunderTime())
            .setTimeOfDay(world.getTimeOfDay());

        // GameRuleStore gameRuleStore = config.getGameRules();
        // for (Key<?> key : GameRulesAccessor.getRuleTypes().keySet()) {
        //     var value = world.getGameRules().get(key);
        //     if () {
                
        //     }
        // } 
        return config;
    }
}
