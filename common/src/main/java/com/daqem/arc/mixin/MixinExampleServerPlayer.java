package com.daqem.arc.mixin;

import com.daqem.arc.Arc;
import com.daqem.arc.api.example.ExampleActionHolderManager;
import com.daqem.arc.api.example.ExampleServerPlayer;
import com.daqem.arc.api.player.ArcPlayer;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class MixinExampleServerPlayer extends Player implements ExampleServerPlayer {

    public MixinExampleServerPlayer(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, CallbackInfo ci) {
        if (((ServerPlayer) (Object) this) instanceof ArcPlayer arcPlayer) {
            if (Arc.isDebugEnvironment()) {
                arcPlayer.arc$addActionHolders(ExampleActionHolderManager.getInstance().getActionHolders());
            }
        }
    }
}
