package com.daqem.arc.event;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;

public class ItemEvents {

    public static void registerEvents() {
        PlayerEvent.DROP_ITEM.register((player, itemStack) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.DROP_ITEM)
                        .withData(IActionDataType.ITEM, itemStack.getItem().getItem())
                        .withData(IActionDataType.ITEM_STACK, itemStack.getItem())
                        .build()
                        .sendToAction();
            }
            return EventResult.pass();
        });
    }

    /**
     * Called when a player uses an item.
     *
     * @param player   - The player that used the item.
     * @param usedItem - The item that was used.
     */
    public static void onUseItem(ArcServerPlayer player, Item usedItem) {
        new ActionDataBuilder(player, IActionType.USE_ITEM)
                .withData(IActionDataType.ITEM, usedItem)
                .build()
                .sendToAction();
    }

    public static void onThrowItem(ArcServerPlayer player, ThrowableItemProjectile thrownItemEntity) {
        new ActionDataBuilder(player, IActionType.THROW_ITEM)
                .withData(IActionDataType.ITEM_STACK, thrownItemEntity.getItem())
                .withData(IActionDataType.ENTITY, thrownItemEntity)
                .build()
                .sendToAction();
    }
}
