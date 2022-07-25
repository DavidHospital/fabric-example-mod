package com.haiherdev.worldplexer.mixin;

import java.io.File;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.WorldSaveHandler;

@Mixin(WorldSaveHandler.class)
public class WorldSaveHandlerMixin {
    
    @Redirect(method = "loadPlayerData(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/nbt/NbtCompound;",
              at = @At(value = "FIELD", target = "Lnet/minecraft/world/WorldSaveHandler;playerDataDir:Ljava/io/File;", opcode = Opcodes.GETFIELD))
    private File loadPlayerDataRedirect(WorldSaveHandler worldSaveHandler) {
        File playerDataDir = ((WorldSaveHandlerAccessor) worldSaveHandler).getPlayerDataDir();
        System.out.println(String.format("loadPlayerDataRedirect called: %s", playerDataDir.getAbsolutePath()));
        return playerDataDir;
    }

    @Redirect(method = "savePlayerData(Lnet/minecraft/entity/player/PlayerEntity;)V",
              at = @At(value = "FIELD", target = "Lnet/minecraft/world/WorldSaveHandler;playerDataDir:Ljava/io/File;", opcode = Opcodes.GETFIELD))
    private File savePlayerDataRedirect(WorldSaveHandler worldSaveHandler) {
        File playerDataDir = ((WorldSaveHandlerAccessor) worldSaveHandler).getPlayerDataDir();
        System.out.println(String.format("savePlayerDataRedirect called: %s", playerDataDir.getAbsolutePath()));
        return playerDataDir;
    }
}
