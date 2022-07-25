package net.fabricmc.example.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldSaveHandler;

@Mixin(WorldSaveHandler.class)
public class WorldSaveHandlerMixin {
    
    @Redirect(at = @At(value = "INVOKE", target = "File(Ljava/io/File;Ljava/lang/String;)Ljava/io/File;"), 
              method = "loadPlayerData(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/nbt/NbtCompound;")
    private File loadPlayerDataRedirectFile(File parent, String child) {
        System.out.println("WorldSaveHandler#loadPlayerData()#<File>()V called");
        return new File(parent, child);
    }

    // @Inject(at = @At("HEAD"), method = "savePlayerData(Lnet/minecraft/entity/player/PlayerEntity;)V")
    // private void savePlayerData(ServerPlayerEntity serverPlayerEntity, CallbackInfo info) {
    //     System.out.println("WorldSaveHandler#savePlayerData() called");
    // }
}
