package com.daqem.arc.mixin;

import com.daqem.arc.api.MovementType;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.event.ArcMovementEvent;
import com.daqem.arc.api.event.ArcPlayerEvent;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.data.PlayerActionHolderManager;
import com.daqem.arc.networking.ClientboundSyncPlayerActionHoldersPacket;
import com.daqem.arc.player.BlockPosCache;
import com.mojang.authlib.GameProfile;
import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements ArcServerPlayer {

    @Shadow
    public ServerGamePacketListenerImpl connection;
    @Unique
    private final Map<ResourceLocation, IActionHolder> arc$actionHolders = new HashMap<>();
    @Unique
    private MovementType arc$previousMovementType = MovementType.IDLE;
    @Unique
    private double arc$totalWalkedCm = 0;
    @Unique
    private double arc$totalSprintedCm = 0;
    @Unique
    private double arc$totalSwamCm = 0;
    @Unique
    private double arc$totalCrouchedCm = 0;
    @Unique
    private double arc$totalElytraFlyCm = 0;
    @Unique
    private double arc$totalHorseRideCm = 0;
    @Unique
    private final Map<Object, Double> arc$actionLastMetDistances = new HashMap<>();
    @Unique
    public BlockPosCache arc$blockPosCache = new BlockPosCache();
    @Unique
    private boolean arc$isApplyingRewardEffect = false;

    public MixinServerPlayer(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Override
    public List<IActionHolder> arc$getActionHolders() {
        return new ArrayList<>(arc$actionHolders.values());
    }

    @Override
    public void arc$addActionHolder(IActionHolder actionHolder) {
        if (actionHolder == null) return;
        this.arc$actionHolders.put(actionHolder.getLocation(), actionHolder);
        arc$syncActionHoldersWithClient();
    }

    @Override
    public void arc$addActionHolders(List<IActionHolder> actionHolders) {
        if (actionHolders == null) return;
        for (IActionHolder actionHolder : actionHolders) {
            arc$addActionHolder(actionHolder);
        }
    }

    @Override
    public void arc$removeActionHolder(IActionHolder actionHolder) {
        this.arc$actionHolders.remove(actionHolder.getLocation());
    }

    @Override
    public void arc$clearActionHolders() {
        this.arc$actionHolders.clear();
    }

    @Override
    public ServerPlayer arc$getServerPlayer() {
        return (ServerPlayer) (Object) this;
    }

    @Override
    public double arc$getTotalWalkedCm() {
        return this.arc$totalWalkedCm;
    }

    @Override
    public double arc$getTotalSprintedCm() {
        return this.arc$totalSprintedCm;
    }

    @Override
    public double arc$getTotalSwamCm() {
        return this.arc$totalSwamCm;
    }

    @Override
    public double arc$getTotalCrouchedCm() {
        return this.arc$totalCrouchedCm;
    }

    @Override
    public double arc$getTotalElytraFlyCm() {
        return this.arc$totalElytraFlyCm;
    }

    @Override
    public double arc$getTotalHorseRideCm() {
        return this.arc$totalHorseRideCm;
    }

    @Override
    public Map<Object, Double> arc$getActionLastMetDistances() {
        return this.arc$actionLastMetDistances;
    }

    @Override
    public void arc$setActionLastMetDistance(Object action, double distance) {
        this.arc$actionLastMetDistances.put(action, distance);
    }

    @Override
    public void arc$syncActionHoldersWithClient() {
        if (this.connection == null) return;
        NetworkManager.sendToPlayer(arc$getServerPlayer(), new ClientboundSyncPlayerActionHoldersPacket(arc$getActionHolders()));
    }

    @Override
    public BlockPosCache arc$getBlockPosCache() {
        return this.arc$blockPosCache;
    }

    @Override
    public boolean arc$isApplyingRewardEffect() {
        return this.arc$isApplyingRewardEffect;
    }

    @Override
    public void arc$setApplyingRewardEffect(boolean isApplying) {
        this.arc$isApplyingRewardEffect = isApplying;
    }

    @Override
    public double arc$nextRandomDouble() {
        return this.arc$getServerPlayer().getRandom().nextDouble();
    }

    @Override
    public @NotNull Level arc$getLevel() {
        return super.level();
    }

    @Override
    public String arc$getName() {
        return super.getName().getString();
    }

    @Override
    public Player arc$getPlayer() {
        return arc$getServerPlayer();
    }

    @Inject(method = "checkMovementStatistics", at = @At("HEAD"))
    public void onCheckMovementStatistics(double movedX, double movedY, double movedZ, CallbackInfo ci) {
        MovementType currentMovementType = MovementType.IDLE;
        int distanceInCm = 0;

        if (this.isPassenger() && this.getRootVehicle() instanceof AbstractHorse horse && horse.isSaddled()) {
            currentMovementType = MovementType.HORSE_RIDING;
            distanceInCm = Math.round((float) Math.sqrt(movedX * movedX + movedZ * movedZ) * 100.0F);
        } else if (this.isSwimming()) {
            currentMovementType = MovementType.SWIMMING;
            distanceInCm = Math.round((float) Math.sqrt(movedX * movedX + movedY * movedY + movedZ * movedZ) * 100.0F);
        } else if (this.onGround()) {
            distanceInCm = Math.round((float) Math.sqrt(movedX * movedX + movedZ * movedZ) * 100.0F);
            if (this.isSprinting()) {
                currentMovementType = MovementType.SPRINTING;
            } else if (this.isCrouching()) {
                currentMovementType = MovementType.CROUCHING;
            } else {
                currentMovementType = MovementType.WALKING;
            }
        } else if (this.isFallFlying()) {
            currentMovementType = MovementType.ELYTRA_FLYING;
            distanceInCm = Math.round((float) Math.sqrt(movedX * movedX + movedY * movedY + movedZ * movedZ) * 100.0F);
        }

        if (distanceInCm <= 0) {
            currentMovementType = MovementType.IDLE;
        }

        if (currentMovementType != this.arc$previousMovementType) {
            // Fire the STOP event for the old state
            arc$fireStopEvent(this.arc$previousMovementType, this.arc$getServerPlayer());
            // Fire the START event for the new state
            arc$fireStartEvent(currentMovementType, this.arc$getServerPlayer());
            // Update the state for the next tick
            this.arc$previousMovementType = currentMovementType;
        }

        if (distanceInCm > 0) {
            switch (currentMovementType) {
                case WALKING:
                    this.arc$totalWalkedCm += distanceInCm;
                    ArcMovementEvent.WALK.invoker().onWalk(this.arc$getServerPlayer(), this.arc$totalWalkedCm);
                    break;
                case SPRINTING:
                    this.arc$totalSprintedCm += distanceInCm;
                    ArcMovementEvent.SPRINT.invoker().onSprint(this.arc$getServerPlayer(), this.arc$totalSprintedCm);
                    break;
                case SWIMMING:
                    this.arc$totalSwamCm += distanceInCm;
                    ArcMovementEvent.SWIM.invoker().onSwim(this.arc$getServerPlayer(), this.arc$totalSwamCm);
                    break;
                case CROUCHING:
                    this.arc$totalCrouchedCm += distanceInCm;
                    ArcMovementEvent.CROUCH.invoker().onCrouch(this.arc$getServerPlayer(), this.arc$totalCrouchedCm);
                    break;
                case ELYTRA_FLYING:
                    this.arc$totalElytraFlyCm += distanceInCm;
                    ArcMovementEvent.ELYTRA_FLY.invoker().onElytraFly(this.arc$getServerPlayer(), this.arc$totalElytraFlyCm);
                    break;
                case HORSE_RIDING:
                    this.arc$totalHorseRideCm += distanceInCm;
                    ArcMovementEvent.HORSE_RIDE.invoker().onHorseRide(this.arc$getServerPlayer(), this.arc$totalHorseRideCm);
                    break;
            }
        }
    }

    @Unique
    private void arc$fireStartEvent(MovementType type, ServerPlayer player) {
        switch (type) {
            case WALKING:
                ArcMovementEvent.START_WALK.invoker().onStartWalk(player);
                break;
            case SPRINTING:
                ArcMovementEvent.START_SPRINT.invoker().onStartSprint(player);
                break;
            case SWIMMING:
                ArcMovementEvent.START_SWIM.invoker().onStartSwim(player);
                break;
            case CROUCHING:
                ArcMovementEvent.START_CROUCH.invoker().onStartCrouch(player);
                break;
            case ELYTRA_FLYING:
                ArcMovementEvent.START_ELYTRA_FLY.invoker().onStartElytraFly(player);
                break;
            case HORSE_RIDING:
                ArcMovementEvent.START_HORSE_RIDE.invoker().onStartHorseRide(player);
                break;
        }
    }

    @Unique
    private void arc$fireStopEvent(MovementType type, ServerPlayer player) {
        switch (type) {
            case WALKING:
                ArcMovementEvent.STOP_WALK.invoker().onStopWalk(player);
                break;
            case SPRINTING:
                ArcMovementEvent.STOP_SPRINT.invoker().onStopSprint(player);
                break;
            case SWIMMING:
                ArcMovementEvent.STOP_SWIM.invoker().onStopSwim(player);
                break;
            case CROUCHING:
                ArcMovementEvent.STOP_CROUCH.invoker().onStopCrouch(player);
                break;
            case ELYTRA_FLYING:
                ArcMovementEvent.STOP_ELYTRA_FLY.invoker().onStopElytraFly(player);
                break;
            case HORSE_RIDING:
                ArcMovementEvent.STOP_HORSE_RIDE.invoker().onStopHorseRide(player);
                break;
        }
    }

    @Inject(at = @At("TAIL"), method = "onEffectAdded(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)V")
    public void onEffectAdded(MobEffectInstance effect, @Nullable Entity entity, CallbackInfo ci) {
    }

    @Inject(at = @At("TAIL"), method = "onEnchantmentPerformed(Lnet/minecraft/world/item/ItemStack;I)V")
    public void onEnchantmentPerformed(ItemStack itemStack, int level, CallbackInfo ci) {
        ArcPlayerEvent.ENCHANT_ITEM.invoker().onEnchantItem((ServerPlayer) (Object) this, itemStack, level);
    }

    @Inject(at = @At("TAIL"), method = "restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V")
    public void restoreFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        if (oldPlayer instanceof ArcServerPlayer arcServerPlayer) {
            this.arc$totalWalkedCm = arcServerPlayer.arc$getTotalWalkedCm();
            this.arc$totalSprintedCm = arcServerPlayer.arc$getTotalSprintedCm();
            this.arc$totalSwamCm = arcServerPlayer.arc$getTotalSwamCm();
            this.arc$totalCrouchedCm = arcServerPlayer.arc$getTotalCrouchedCm();
            this.arc$totalElytraFlyCm = arcServerPlayer.arc$getTotalElytraFlyCm();
            this.arc$totalHorseRideCm = arcServerPlayer.arc$getTotalHorseRideCm();
            this.arc$actionLastMetDistances.putAll(arcServerPlayer.arc$getActionLastMetDistances());
            this.arc$blockPosCache = arcServerPlayer.arc$getBlockPosCache();
        }
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    public void readAdditionalSaveData(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ClientInformation clientInformation, CallbackInfo ci) {
        if (((ServerPlayer) (Object) this) instanceof ArcPlayer arcPlayer) {
            arcPlayer.arc$addActionHolders(PlayerActionHolderManager.getInstance().getPlayerActionHoldersList());
        }
    }
}
