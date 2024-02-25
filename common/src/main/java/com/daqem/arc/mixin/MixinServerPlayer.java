package com.daqem.arc.mixin;

import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.action.type.ActionType;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.data.PlayerActionHolderManager;
import com.daqem.arc.event.triggers.MovementEvents;
import com.daqem.arc.event.triggers.PlayerEvents;
import com.daqem.arc.event.triggers.StatEvents;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.player.stat.StatData;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements ArcServerPlayer {

    @Unique
    private final List<IActionHolder> arc$actionHolders = new ArrayList<>();
    @Unique
    private final NonNullList<StatData> arc$statData = NonNullList.create();
    @Unique
    private final Map<ICondition, Integer> arc$lastDistanceInCm = new HashMap<>();
    @Unique
    private final Map<ICondition, Integer> arc$lastRemainderInCm = new HashMap<>();
    @Unique
    private boolean arc$isSwimming = false;
    @Unique
    private int arc$swimmingDistanceInCm = 0;
    @Unique
    private boolean arc$isWalking = false;
    @Unique
    private float arc$walkingDistance = 0;
    @Unique
    private boolean arc$isSprinting = false;
    @Unique
    private float arc$sprintingDistance = 0;
    @Unique
    private boolean arc$isCrouching = false;
    @Unique
    private float arc$crouchingDistance = 0;
    @Unique
    private boolean arc$isElytraFlying = false;
    @Unique
    private float arc$elytraFlyingDistance = 0;
    @Unique
    private boolean arc$isGrinding = false;

    public MixinServerPlayer(Level level, BlockPos blockPos, float yaw, GameProfile gameProfile) {
        super(level, blockPos, yaw, gameProfile);
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, CallbackInfo ci) {
        if (((ServerPlayer) (Object) this) instanceof ArcPlayer arcPlayer) {
            arcPlayer.arc$addActionHolders(PlayerActionHolderManager.getInstance().getPlayerActionHoldersList());
        }
    }

    @Override
    public List<IActionHolder> arc$getActionHolders() {
        return arc$actionHolders;
    }

    @Override
    public void arc$addActionHolder(IActionHolder actionHolder) {
        if (actionHolder == null) return;
        this.arc$actionHolders.add(actionHolder);
    }

    @Override
    public void arc$addActionHolders(List<IActionHolder> actionHolders) {
        if (actionHolders == null) return;
        actionHolders.removeIf(Objects::isNull);
        this.arc$actionHolders.addAll(actionHolders);
    }

    @Override
    public void arc$removeActionHolder(IActionHolder actionHolder) {
        this.arc$actionHolders.remove(actionHolder);
    }

    @Override
    public ServerPlayer arc$getServerPlayer() {
        return (ServerPlayer) (Object) this;
    }

    @Override
    public NonNullList<StatData> arc$getStatData() {
        return this.arc$statData;
    }

    @Override
    public void arc$addStatData(StatData statData) {
        this.arc$statData.add(statData);
    }

    @Override
    public void arc$setSwimmingDistanceInCm(int swimmingDistanceInCm) {
        this.arc$swimmingDistanceInCm = swimmingDistanceInCm;
    }

    @Override
    public void arc$setElytraFlyingDistanceInCm(float flyingDistanceInCm) {
        this.arc$elytraFlyingDistance = flyingDistanceInCm;
    }

    @Override
    public int arc$getLastDistanceInCm(ICondition distanceCondition) {
        Integer lastDistanceInCm = arc$lastDistanceInCm.get(distanceCondition);
        return lastDistanceInCm == null ? 0 : lastDistanceInCm;
    }

    @Override
    public void arc$setLastDistanceInCm(ICondition distanceCondition, int lastDistanceInCm) {
        arc$lastDistanceInCm.put(distanceCondition, lastDistanceInCm);
    }

    @Override
    public int arc$getLastRemainderInCm(ICondition distanceCondition) {
        Integer lastRemainderInCm = arc$lastRemainderInCm.get(distanceCondition);
        return lastRemainderInCm == null ? 0 : lastRemainderInCm;
    }

    @Override
    public void arc$setLastRemainderInCm(ICondition distanceCondition, int lastRemainderInCm) {
        arc$lastRemainderInCm.put(distanceCondition, lastRemainderInCm);
    }

    @Override
    public @NotNull ItemStack eat(@NotNull Level level, @NotNull ItemStack itemStack) {
        PlayerEvents.onPlayerEat(this, itemStack);
        return super.eat(level, itemStack);
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

    @Inject(at = @At("TAIL"), method = "tick()V")
    public void tick(CallbackInfo ci) {
        if (this.arc$isSwimming && this.isSwimming()) {
            MovementEvents.onSwim(this, this.arc$swimmingDistanceInCm);
        } else {
            if (this.arc$isSwimming) {
                this.arc$isSwimming = false;
                MovementEvents.onStopSwimming(this);
            } else {
                if (this.isSwimming()) {
                    this.arc$isSwimming = true;
                    MovementEvents.onStartSwimming(this);
                }
            }
        }

        boolean isCurrentlyWalking = this.walkDist > this.arc$walkingDistance;
        float distance = this.walkDist - this.arc$walkingDistance;
        if (this.arc$isWalking && isCurrentlyWalking) {
            this.arc$walkingDistance = this.walkDist;
            MovementEvents.onWalk(this, (int) (this.arc$walkingDistance * 100));
        } else {
            if (this.arc$isWalking) {
                this.arc$isWalking = false;
                MovementEvents.onStopWalking(this);
            } else if (isCurrentlyWalking) {
                this.arc$isWalking = true;
                MovementEvents.onStartWalking(this);
            }
        }

        if (this.arc$isSprinting && this.isSprinting()) {
            this.arc$sprintingDistance += distance;
            MovementEvents.onSprint(this, (int) (this.arc$sprintingDistance * 100));
        } else {
            if (this.arc$isSprinting) {
                this.arc$isSprinting = false;
                MovementEvents.onStopSprinting(this);
            } else if (this.isSprinting()) {
                this.arc$isSprinting = true;
                MovementEvents.onStartSprinting(this);
            }
        }

        if (this.arc$isCrouching && this.isCrouching()) {
            this.arc$crouchingDistance += distance;
            MovementEvents.onCrouch(this, (int) (this.arc$crouchingDistance * 100));
        } else {
            if (this.arc$isCrouching) {
                this.arc$isCrouching = false;
                MovementEvents.onStopCrouching(this);
            } else if (this.isCrouching()) {
                this.arc$isCrouching = true;
                MovementEvents.onStartCrouching(this);
            }
        }

        if (this.arc$isElytraFlying && this.isFallFlying()) {
            MovementEvents.onElytraFly(this, (int) this.arc$elytraFlyingDistance);
        } else {
            if (this.arc$isElytraFlying) {
                this.arc$isElytraFlying = false;
                MovementEvents.onStopElytraFlying(this);
            } else {
                if (this.isFallFlying()) {
                    this.arc$isElytraFlying = true;
                    MovementEvents.onStartElytraFlying(this);
                }
            }
        }

        if (arc$getServerPlayer().containerMenu instanceof GrindstoneMenu) {
            if (arc$isGrinding) {
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
            arc$isGrinding = firstSlot && secondSlot;
        }
    }

    @Inject(at = @At("TAIL"), method = "awardStat(Lnet/minecraft/stats/Stat;I)V")
    public void awardStat(Stat<?> stat, int amount, CallbackInfo ci) {
        int previousAmount = 0;
        boolean found = false;
        for (StatData statData : arc$getStatData()) {
            if (statData.getStat().equals(stat)) {
                previousAmount = statData.getAmount();
                statData.addAmount(amount);
                found = true;
                break;
            }
        }
        if (!found) {
            arc$addStatData(new StatData(stat, amount));
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

    @Inject(at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            shift = At.Shift.BEFORE),
            method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            cancellable = true)
    public void hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = this.arc$getPlayer();
        if (entity instanceof ArcServerPlayer arcServerPlayer) {
            ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, ActionType.GET_HURT)
                    .withData(ActionDataType.DAMAGE_SOURCE, damageSource)
                    .withData(ActionDataType.DAMAGE_AMOUNT, f)
                    .build()
                    .sendToAction();

            if (actionResult.shouldCancelAction()) {
                f = 0F;
            }
            if (actionResult.getDamageModifier() != 1F) {
                f = f * actionResult.getDamageModifier();
            }
        }

        if (damageSource.getEntity() instanceof ArcServerPlayer arcServerPlayer) {
            ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, ActionType.HURT_ENTITY)
                    .withData(ActionDataType.ENTITY, entity)
                    .withData(ActionDataType.DAMAGE_AMOUNT, f)
                    .build()
                    .sendToAction();

            if (actionResult.shouldCancelAction()) {
                f = 0F;
            }
            if (actionResult.getDamageModifier() != 1F) {
                f = f * actionResult.getDamageModifier();
            }
        }
        cir.setReturnValue(super.hurt(damageSource, f));
    }

    @Inject(at = @At("HEAD"), method = "triggerRecipeCrafted")
    public void mixinTriggerRecipeCrafted(Recipe<?> recipe, List<ItemStack> list, CallbackInfo ci) {
        Level level = level();
        PlayerEvents.onCraftItem(this, recipe, recipe.getResultItem(level.registryAccess()), level);
    }
}
