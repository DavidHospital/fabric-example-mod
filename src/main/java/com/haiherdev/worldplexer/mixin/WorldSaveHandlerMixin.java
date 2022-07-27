package com.haiherdev.worldplexer.mixin;

import java.io.File;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.WorldSaveHandler;

import com.haiherdev.worldplexer.PlexSaveHandler;

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
              at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getUuidAsString()Ljava/lang/String;"))
    private String savePlayerDataRedirect(PlayerEntity player) {
        System.out.println(String.format("savePlayerDataRedirect called: %s", player.getUuidAsString()));
        String namespace = player.getWorld().getRegistryKey().getValue().getNamespace();
        String value = player.getWorld().getRegistryKey().getValue().getPath();
        System.out.println(String.format("Player is in world [%s:%s]", namespace, value));
        if (namespace == PlexSaveHandler.PLEX_NAMESPACE) {
            System.out.println(String.format("dimensions/%s/%s/%s", namespace, value, player.getUuidAsString()));
        }
        return player.getUuidAsString();
    }
}
