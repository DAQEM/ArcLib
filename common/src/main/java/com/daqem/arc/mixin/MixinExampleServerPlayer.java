package com.daqem.arc.mixin;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.example.ExampleActionHolderManager;
import com.daqem.arc.api.example.ExampleServerPlayer;
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

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayer.class)
public abstract class MixinExampleServerPlayer extends Player implements ExampleServerPlayer {

    private final List<IActionHolder> actionHolders = new ArrayList<>();

    public MixinExampleServerPlayer(Level level, BlockPos blockPos, float f, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(level, blockPos, f, gameProfile, profilePublicKey);
    }

    @Override
    public List<IActionHolder> getActionHolders() {
        return actionHolders;
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ProfilePublicKey profilePublicKey, CallbackInfo ci) {
        this.actionHolders.addAll(ExampleActionHolderManager.getInstance().getActionHolders());
    }
}
