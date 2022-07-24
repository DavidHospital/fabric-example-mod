package net.fabricmc.example.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    
    @Inject(at = @At("HEAD"), method = "loadPlayerData(Lnet/minecraft/server/network/ServerPlayerEntity;)Lnet/minecraft/nbt/NbtCompound;")
    private void loadPlayerData(ServerPlayerEntity serverPlayerEntity, CallbackInfoReturnable<NbtCompound> info) {
        System.out.println("PlayerManager#loadPlayerData() called");
    }

    @Inject(at = @At("HEAD"), method = "savePlayerData(Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    private void savePlayerData(ServerPlayerEntity serverPlayerEntity, CallbackInfo info) {
        System.out.println("PlayerManager#savePlayerData() called");
    }
}
