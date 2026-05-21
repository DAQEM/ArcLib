package com.daqem.arc.mixin;

import com.daqem.arc.api.blockentity.IArcBrewingStand;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.event.triggers.PlayerEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(BrewingStandBlockEntity.class)
public abstract class MixinBrewingStandBlockEntity implements IArcBrewingStand {

    @Unique
    private final Map<Integer, UUID> arc$brewingStandItemOwners = new HashMap<>();
    @Unique
    private UUID arc$lastPlayerToInteract;

    @Override
    public void arc$setLastPlayerToInteract(UUID uuid) {
        this.arc$lastPlayerToInteract = uuid;
    }

    @Override
    public UUID arc$getLastPlayerToInteract() {
        return this.arc$lastPlayerToInteract;
    }

    @Override
    public void arc$addBrewingStandItemOwner(int slot, UUID uuid) {
        this.arc$brewingStandItemOwners.put(slot, uuid);
    }

    @Override
    public void arc$removeBrewingStandItemOwner(int slot) {
        this.arc$brewingStandItemOwners.remove(slot);
    }

    @Override
    public UUID arc$getBrewingStandItemOwner(int slot) {
        return this.arc$brewingStandItemOwners.get(slot);
    }

    @Override
    public Map<Integer, UUID> arc$getBrewingStandItemOwners() {
        return this.arc$brewingStandItemOwners;
    }

    @Inject(at = @At("HEAD"), method = "serverTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BrewingStandBlockEntity;)V")
    private static void serverTick(Level level, BlockPos blockPos, BlockState blockState, BrewingStandBlockEntity brewingStandBlockEntity, CallbackInfo info) {
        IArcBrewingStand arcBrewingStand = (IArcBrewingStand) brewingStandBlockEntity;
        for (int i = 0; i < 3; i++) {
            if (brewingStandBlockEntity.getItem(i).isEmpty()) {
                if (arcBrewingStand.arc$getBrewingStandItemOwner(i) != null) {
                    arcBrewingStand.arc$removeBrewingStandItemOwner(i);
                }
            } else {
                if (arcBrewingStand.arc$getBrewingStandItemOwner(i) == null) {
                    if (arcBrewingStand.arc$getLastPlayerToInteract() != null) {
                        arcBrewingStand.arc$addBrewingStandItemOwner(i, arcBrewingStand.arc$getLastPlayerToInteract());
                    }
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "doBrew(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/NonNullList;)V")
    private static void doBrew(Level level, BlockPos blockPos, NonNullList<ItemStack> nonNullList, CallbackInfo ci) {
        if (level != null && level.getBlockEntity(blockPos) instanceof BrewingStandBlockEntity brewingStandBlockEntity) {
            IArcBrewingStand arcBrewingStand = (IArcBrewingStand) brewingStandBlockEntity;
            if (arcBrewingStand.arc$getBrewingStandItemOwners().size() == nonNullList.stream().filter(itemStack -> (itemStack.getItem() instanceof PotionItem)).toList().size()) {
                for (Map.Entry<Integer, UUID> entry : arcBrewingStand.arc$getBrewingStandItemOwners().entrySet()) {
                    int slot = entry.getKey();
                    UUID uuid = entry.getValue();
                    Player player = level.getServer() != null ? level.getServer().getPlayerList().getPlayer(uuid) : null;

                    if (player instanceof ArcServerPlayer arcServerPlayer) {
                        PlayerEvents.onBrewPotion(arcServerPlayer, nonNullList.get(slot), blockPos, level);
                    }
                }
            }
        }
    }
}