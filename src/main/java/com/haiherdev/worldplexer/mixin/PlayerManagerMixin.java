package com.haiherdev.worldplexer.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
public interface PlayerManagerMixin {
    
    @Invoker("savePlayerData")
    public void invokeSavePlayerData(ServerPlayerEntity player);
}
