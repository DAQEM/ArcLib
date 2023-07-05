package com.daqem.arc.mixin;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.event.triggers.MovementEvents;
import com.daqem.arc.event.triggers.PlayerEvents;
import com.daqem.arc.event.triggers.StatEvents;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.player.stat.StatData;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements ArcServerPlayer {

    private final NonNullList<StatData> statData = NonNullList.create();
    private boolean isSwimming = false;
    private int swimmingDistanceInCm = 0;
    private boolean isWalking = false;
    private float walkingDistance = 0;
    private boolean isSprinting = false;
    private float sprintingDistance = 0;
    private boolean isCrouching = false;
    private float crouchingDistance = 0;
    private boolean isElytraFlying = false;
    private float elytraFlyingDistance = 0;
    private boolean isGrinding = false;

    public MixinServerPlayer(Level level, BlockPos blockPos, float yaw, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(level, blockPos, yaw, gameProfile, profilePublicKey);
    }

    @Override
    public List<IActionHolder> getActionHolders() {
        return new ArrayList<>();
    }

    @Override
    public @NotNull UUID getUUID() {
        return super.getUUID();
    }

    @Override
    public ServerPlayer getServerPlayer() {
        return (ServerPlayer) (Object) this;
    }

    @Override
    public String name() {
        return super.getName().getString();
    }

    @Override
    public NonNullList<StatData> getStatData() {
        return this.statData;
    }

    @Override
    public void addStatData(StatData statData) {
        this.statData.add(statData);
    }

    @Override
    public void setSwimmingDistanceInCm(int swimmingDistanceInCm) {
        this.swimmingDistanceInCm = swimmingDistanceInCm;
    }

    @Override
    public void setElytraFlyingDistanceInCm(float flyingDistanceInCm) {
        this.elytraFlyingDistance = flyingDistanceInCm;
    }

    @Override
    public @NotNull ItemStack eat(@NotNull Level level, @NotNull ItemStack itemStack) {
        PlayerEvents.onPlayerEat(this, itemStack);
        return super.eat(level, itemStack);
    }

    @Override
    public double nextRandomDouble() {
        return this.getServerPlayer().getRandom().nextDouble();
    }

    @Override
    public @NotNull Level getLevel() {
        return super.getLevel();
    }

    @Override
    public Player getPlayer() {
        return getServerPlayer();
    }

    @Inject(at = @At("TAIL"), method = "tick()V")
    public void tick(CallbackInfo ci) {
        if (this.isSwimming && this.isSwimming()) {
            MovementEvents.onSwim(this, this.swimmingDistanceInCm);
        } else {
            if (this.isSwimming) {
                this.isSwimming = false;
                MovementEvents.onStopSwimming(this);
            } else {
                if (this.isSwimming()) {
                    this.isSwimming = true;
                    MovementEvents.onStartSwimming(this);
                }
            }
        }

        boolean isCurrentlyWalking = this.walkDist > this.walkingDistance;
        float distance = this.walkDist - this.walkingDistance;
        if (this.isWalking && isCurrentlyWalking) {
            this.walkingDistance = this.walkDist;
            MovementEvents.onWalk(this, (int) (this.walkingDistance * 100));
        } else {
            if (this.isWalking) {
                this.isWalking = false;
                MovementEvents.onStopWalking(this);
            } else if (isCurrentlyWalking) {
                this.isWalking = true;
                MovementEvents.onStartWalking(this);
            }
        }

        if (this.isSprinting && this.isSprinting()) {
            this.sprintingDistance += distance;
            MovementEvents.onSprint(this, (int) (this.sprintingDistance * 100));
        } else {
            if (this.isSprinting) {
                this.isSprinting = false;
                MovementEvents.onStopSprinting(this);
            } else if (this.isSprinting()) {
                this.isSprinting = true;
                MovementEvents.onStartSprinting(this);
            }
        }

        if (this.isCrouching && this.isCrouching()) {
            this.crouchingDistance += distance;
            MovementEvents.onCrouch(this, (int) (this.crouchingDistance * 100));
        } else {
            if (this.isCrouching) {
                this.isCrouching = false;
                MovementEvents.onStopCrouching(this);
            } else if (this.isCrouching()) {
                this.isCrouching = true;
                MovementEvents.onStartCrouching(this);
            }
        }

        if (this.isElytraFlying && this.isFallFlying()) {
            MovementEvents.onElytraFly(this, (int) this.elytraFlyingDistance);
        } else {
            if (this.isElytraFlying) {
                this.isElytraFlying = false;
                MovementEvents.onStopElytraFlying(this);
            } else {
                if (this.isFallFlying()) {
                    this.isElytraFlying = true;
                    MovementEvents.onStartElytraFlying(this);
                }
            }
        }

        if (getServerPlayer().containerMenu instanceof GrindstoneMenu) {
            if (isGrinding) {
                boolean firstSlot = false;
                boolean secondSlot = false;
                for (Slot slot : containerMenu.slots) {
                    if (!(slot.getItem().getItem() instanceof AirItem || slot.container instanceof Inventory)) {

                        if (slot.getContainerSlot() == 0) {
                            firstSlot = true;
                        }
                        if (slot.getContainerSlot() == 1) {
                            secondSlot = true;
                        }
                    }
                }
                if (!firstSlot && !secondSlot) {
                    PlayerEvents.onGrindItem(this);
                }
            }
            boolean firstSlot = false;
            boolean secondSlot = false;
            for (Slot slot : containerMenu.slots) {
                if (!(slot.getItem().getItem() instanceof AirItem || slot.container instanceof Inventory)) {

                    if (slot.getContainerSlot() == 0) {
                        firstSlot = true;
                    }
                    if (slot.getContainerSlot() == 1) {
                        secondSlot = true;
                    }
                }
            }
            isGrinding = firstSlot && secondSlot;
        }
    }

    @Inject(at = @At("TAIL"), method = "awardStat(Lnet/minecraft/stats/Stat;I)V")
    public void awardStat(Stat<?> stat, int amount, CallbackInfo ci) {
        int previousAmount = 0;
        boolean found = false;
        for (StatData statData : getStatData()) {
            if (statData.getStat().equals(stat)) {
                previousAmount = statData.getAmount();
                statData.addAmount(amount);
                found = true;
                break;
            }
        }
        if (!found) {
            addStatData(new StatData(stat, amount));
        }
        StatEvents.onAwardStat(this, stat, previousAmount, previousAmount + amount);
    }

    @Inject(at = @At("TAIL"), method = "onEffectAdded(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)V")
    public void onEffectAdded(MobEffectInstance effect, @Nullable Entity entity, CallbackInfo ci) {
    }

    @Inject(at = @At("TAIL"), method = "onEnchantmentPerformed(Lnet/minecraft/world/item/ItemStack;I)V")
    public void onEnchantmentPerformed(ItemStack itemStack, int level, CallbackInfo ci) {
        PlayerEvents.onEnchantItem(this, itemStack, level);
    }
}
