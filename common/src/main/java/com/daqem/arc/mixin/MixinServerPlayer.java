package com.daqem.arc.mixin;

import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.action.holder.IActionHolder;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.action.type.ActionType;
import com.daqem.arc.api.player.ArcPlayer;
import com.daqem.arc.api.player.holder.PlayerActionHolderManager;
import com.daqem.arc.event.triggers.MovementEvents;
import com.daqem.arc.event.triggers.PlayerEvents;
import com.daqem.arc.event.triggers.StatEvents;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.player.stat.StatData;
import com.mojang.authlib.GameProfile;
import dev.architectury.event.EventResult;
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
import net.minecraft.world.entity.player.ProfilePublicKey;
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
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements ArcServerPlayer {

    @Unique
    private final List<IActionHolder> actionHolders = new ArrayList<>();
    @Unique
    private final NonNullList<StatData> statData = NonNullList.create();
    @Unique
    private boolean isSwimming = false;
    @Unique
    private int swimmingDistanceInCm = 0;
    @Unique
    private boolean isWalking = false;
    @Unique
    private float walkingDistance = 0;
    @Unique
    private boolean isSprinting = false;
    @Unique
    private float sprintingDistance = 0;
    @Unique
    private boolean isCrouching = false;
    @Unique
    private float crouchingDistance = 0;
    @Unique
    private boolean isElytraFlying = false;
    @Unique
    private float elytraFlyingDistance = 0;
    @Unique
    private boolean isGrinding = false;

    public MixinServerPlayer(Level level, BlockPos blockPos, float yaw, GameProfile gameProfile) {
        super(level, blockPos, yaw, gameProfile);
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, CallbackInfo ci) {
        if (((ServerPlayer) (Object) this) instanceof ArcPlayer arcPlayer) {
            arcPlayer.arc$addActionHolders(PlayerActionHolderManager.getInstance().getActionHolders());
        }
    }

    @Override
    public List<IActionHolder> arc$getActionHolders() {
        return actionHolders;
    }

    @Override
    public void arc$addActionHolder(IActionHolder actionHolder) {
        if (actionHolder == null) return;
        this.actionHolders.add(actionHolder);
    }

    @Override
    public void arc$addActionHolders(List<IActionHolder> actionHolders) {
        if (actionHolders == null) return;
        actionHolders.removeIf(Objects::isNull);
        this.actionHolders.addAll(actionHolders);
    }

    @Override
    public void arc$removeActionHolder(IActionHolder actionHolder) {
        this.actionHolders.remove(actionHolder);
    }

    @Override
    public ServerPlayer arc$getServerPlayer() {
        return (ServerPlayer) (Object) this;
    }

    @Override
    public NonNullList<StatData> arc$getStatData() {
        return this.statData;
    }

    @Override
    public void arc$addStatData(StatData statData) {
        this.statData.add(statData);
    }

    @Override
    public void arc$setSwimmingDistanceInCm(int swimmingDistanceInCm) {
        this.swimmingDistanceInCm = swimmingDistanceInCm;
    }

    @Override
    public void arc$setElytraFlyingDistanceInCm(float flyingDistanceInCm) {
        this.elytraFlyingDistance = flyingDistanceInCm;
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

        if (arc$getServerPlayer().containerMenu instanceof GrindstoneMenu) {
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
