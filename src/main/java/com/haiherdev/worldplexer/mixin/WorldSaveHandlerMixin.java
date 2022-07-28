package com.haiherdev.worldplexer.mixin;

import java.io.File;

import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.WorldSaveHandler;

import com.haiherdev.worldplexer.PlexSaveHandler;
import com.mojang.logging.LogUtils;

@Mixin(WorldSaveHandler.class)
public class WorldSaveHandlerMixin {
    private static final Logger LOGGER = LogUtils.getLogger();
    
    // @Redirect(method = "loadPlayerData(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/nbt/NbtCompound;",
    //           at = @At(value = "FIELD", target = "Lnet/minecraft/world/WorldSaveHandler;playerDataDir:Ljava/io/File;", opcode = Opcodes.GETFIELD))
    // private File loadPlayerDataRedirect(WorldSaveHandler worldSaveHandler) {
    //     File playerDataDir = ((WorldSaveHandlerAccessor) worldSaveHandler).getPlayerDataDir();
    //     System.out.println(String.format("loadPlayerDataRedirect called: %s", playerDataDir.getAbsolutePath()));
    //     return playerDataDir;
    // }

    @Inject(method = "loadPlayerData(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/nbt/NbtCompound;",
            at = @At(value = "HEAD"), cancellable = true)
    private void loadPlayerDataRedirect(PlayerEntity player, CallbackInfoReturnable<NbtCompound> ci) {
        Identifier worldId = player.getWorld().getRegistryKey().getValue();
        String namespace = worldId.getNamespace();
        String dimensionName = worldId.getPath();
        LOGGER.info("Player [{}] loaded in dimension [{}:{}]", player.getName().toString(), namespace, dimensionName);
        if (!PlexSaveHandler.PLEX_NAMESPACE.equals(namespace)) {
            return;
        }
        
        String worldName = "world";
        File playerDataDir = new File("./" + worldName + "/dimensions/" + PlexSaveHandler.PLEX_NAMESPACE + "/" + dimensionName + "/playerData");

        NbtCompound nbtCompound = null;
        try {
            File file = new File(playerDataDir, player.getUuidAsString() + ".dat");
            LOGGER.info("Starting load for player data from [{}]...", file.getPath());
            if (file.exists() && file.isFile()) {
                LOGGER.info("Loading player data from [{}]", file.getPath());
                nbtCompound = NbtIo.readCompressed(file);
                LOGGER.info(nbtCompound.asString());
            }
        }
        catch (Exception exception) {
            LOGGER.warn("Failed to load player data for {}", (Object)player.getName().getString());
        }
        // if (nbtCompound != null) {
        //     int i = nbtCompound.contains("DataVersion", NbtElement.INT_TYPE) ? nbtCompound.getInt("DataVersion") : -1;
        //     player.readNbt(NbtHelper.update(this.dataFixer, DataFixTypes.PLAYER, nbtCompound, i));
        // }
        ci.setReturnValue(nbtCompound);
    }

    @Inject(method = "savePlayerData(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At(value = "HEAD"), cancellable = true)
    private void savePlayerDataInject(PlayerEntity player, CallbackInfo ci) {
        Identifier worldId = player.getWorld().getRegistryKey().getValue();
        String namespace = worldId.getNamespace();
        String dimensionName = worldId.getPath();
        LOGGER.info("Player [{}] saved in dimension [{}:{}]", player.getName().toString(), namespace, dimensionName);
        if (!PlexSaveHandler.PLEX_NAMESPACE.equals(namespace)) {
            return;
        }

        String worldName = "world";
        File playerDataDir = new File("./" + worldName + "/dimensions/" + PlexSaveHandler.PLEX_NAMESPACE + "/" + dimensionName + "/playerData");

        LOGGER.info("Saving player data to [{}]", playerDataDir.getPath());
        try {
            NbtCompound nbtCompound = player.writeNbt(new NbtCompound());
            File file = File.createTempFile(player.getUuidAsString() + "-", ".dat", playerDataDir);
            NbtIo.writeCompressed(nbtCompound, file);
            File file2 = new File(playerDataDir, player.getUuidAsString() + ".dat");
            File file3 = new File(playerDataDir, player.getUuidAsString() + ".dat_old");
            Util.backupAndReplace(file2, file, file3);
        }
        catch (Exception exception) {
            LOGGER.warn("Failed to save player data for {}", (Object)player.getName().getString());
        }
        ci.cancel();
    }
}
