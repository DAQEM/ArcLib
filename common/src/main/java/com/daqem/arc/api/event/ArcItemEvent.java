package com.daqem.arc.api.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableInt;

public interface ArcItemEvent {

    Event<DropItem> DROP_ITEM = EventFactory.createEventResult(DropItem.class);
    Event<CraftItem> CRAFT_ITEM = EventFactory.createLoop(CraftItem.class);
    Event<HurtItem> HURT_ITEM = EventFactory.createEventResult(HurtItem.class);
    Event<ThrowItem> THROW_ITEM = EventFactory.createLoop(ThrowItem.class);
    Event<UseItem> USE_ITEM = EventFactory.createEventResult(UseItem.class);

    interface DropItem {
        EventResult onDropItem(Player player, ItemEntity itemEntity);
    }

    interface CraftItem {
        void onCraftItem(ServerPlayer serverPlayer, Recipe<?> recipe, ItemStack stack);
    }

    interface HurtItem {
        EventResult onHurtItem(ServerPlayer serverPlayer, ItemStack itemStack, MutableInt damage);
    }

    interface ThrowItem {
        void onThrowItem(ServerPlayer serverPlayer, ThrowableItemProjectile projectile);
    }

    interface UseItem {
        EventResult onUseItem(Level level, Player player, InteractionHand hand, ItemStack itemStack);
    }
}
