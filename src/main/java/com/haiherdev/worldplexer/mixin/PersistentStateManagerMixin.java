package com.haiherdev.worldplexer.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.PersistentStateManager;

@Mixin(PersistentStateManager.class)
public interface PersistentStateManagerMixin {
    
    @Accessor("directory")
    public void setDirectory(File directory);
}
