package com.haiherdev.worldplexer;

import java.io.File;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.haiherdev.worldplexer.mixin.PersistentStateManagerMixin;
import com.mojang.logging.LogUtils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorage;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;
import xyz.nucleoid.fantasy.mixin.MinecraftServerAccess;

public class PlexSaveHandler {
    
    private static final Logger logger = LogUtils.getLogger();

    private static final String DIMENSIONS = "dimensions";
    public static final String PLEX_NAMESPACE = "worldplexer";

    public static PlexSaveHandler plexSaveHandler;

    private final MinecraftServer server;
    private final MinecraftServerAccess serverAccess;
    private final File dimensionsDirectory;

    private PlexSaveHandler(MinecraftServer server) {
        this.server = server;
        this.serverAccess = (MinecraftServerAccess) server;
        this.dimensionsDirectory = new File(this.server.getSavePath(WorldSavePath.ROOT).toFile(), DIMENSIONS + "/" + PLEX_NAMESPACE);
        PlexSaveHandler.plexSaveHandler = this;
    }

    public static PlexSaveHandler get(MinecraftServer server) {
        new PlexSaveHandler(server);
        return PlexSaveHandler.plexSaveHandler;
    }

    public void loadWorldsToRegistry() {
        if (!dimensionsDirectory.exists() || !dimensionsDirectory.isDirectory()) {
            logger.warn("[%s] doesn't exist or is not a directory", dimensionsDirectory.getPath());
            return;
        }
 
        logger.info("Loading WorldPlexer worlds");
        String[] dimensions = dimensionsDirectory.list();

        for (String d : dimensions) {
            // ServerWorld world = this.LoadWorld(Identifier.of(PLEX_NAMESPACE, d));
            // ((MinecraftServerAccess) server).getWorlds().put(world.getRegistryKey(), world);

            // server.getSaveProperties().getGeneratorOptions().getDimensions().getEntrySet().add(
            //     new AbstractMap.SimpleEntry<RegistryKey<DimensionOptions>, DimensionOptions>(
            //         RegistryKey.of(Registry.DIMENSION_KEY, Identifier.of(PLEX_NAMESPACE, d)),
            //         new DimensionOptions(RegistryEntry<DimensionType>.of(DimensionType), chunkGenerator))
            // );
        }
    }

    public void saveWorlds() {
        if (!dimensionsDirectory.exists() || !dimensionsDirectory.isDirectory()) {
            logger.warn("[%s] doesn't exist or is not a directory", dimensionsDirectory.getPath());
            return;
        }

        // logger.info("Saving WorldPlexer worlds");
        // Map<RegistryKey<World>, ServerWorld> worlds = serverAccess.getWorlds();
        // for(RegistryKey<World> key : worlds.keySet().stream().filter(
        //     key -> key.getValue().getNamespace() == PLEX_NAMESPACE
        // ).collect(Collectors.toList())) {
        //     ServerWorld world = worlds.get(key);
        //     ((PersistentStateManagerMixin) world.getChunkManager().getPersistentStateManager()).setDirectory(this.dimensionsDirectory);
        //     world.save(null, true, false);
        // }

        Map<RegistryKey<World>, ServerWorld> worlds = serverAccess.getWorlds();
        LevelStorage.Session session = serverAccess.getSession();
    }

    // private RuntimeWorldConfig generateConfigFromWorld(ServerWorld world) {
    //     RuntimeWorldConfig config = new RuntimeWorldConfig()
    //         .setSeed(world.getSeed())
    //         .setDifficulty(world.getDifficulty())
    //         .setDimensionType(world.getDimensionEntry())
    //         .setGenerator(server.getSaveProperties().getGeneratorOptions().getChunkGenerator())
    //         .setRaining(((LevelProperties) server.getSaveProperties()).getRainTime())
    //         .setSunny(((LevelProperties) server.getSaveProperties()).getClearWeatherTime())
    //         .setThundering(((LevelProperties) server.getSaveProperties()).getThunderTime())
    //         .setTimeOfDay(world.getTimeOfDay());

    //     // GameRuleStore gameRuleStore = config.getGameRules();
    //     // for (Key<?> key : GameRulesAccessor.getRuleTypes().keySet()) {
    //     //     var value = world.getGameRules().get(key);
    //     //     if () {
                
    //     //     }
    //     // } 
    //     return config;
    // }
}
