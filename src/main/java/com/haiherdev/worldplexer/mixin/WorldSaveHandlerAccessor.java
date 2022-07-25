package com.haiherdev.worldplexer.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.WorldSaveHandler;

@Mixin(WorldSaveHandler.class)
public interface WorldSaveHandlerAccessor {
    @Accessor("playerDataDir")
    public File getPlayerDataDir();
}