package com.daqem.arc.event;

import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.event.ArcEntityEvent;
import com.daqem.arc.api.event.EventPriority;
import com.daqem.arc.api.event.EventResult;
import com.daqem.arc.api.player.ArcServerPlayer;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class EntityEvents {

    public static void registerEvents() {
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof ServerPlayer serverPlayer) {
                EventResult eventResult = ArcEntityEvent.PLAYER_DEATH.invoker().onPlayerDeath(serverPlayer, source);
                if (eventResult.cancelsEvent()) {
                    return dev.architectury.event.EventResult.interruptFalse();
                }
            }
            if (source.getEntity() instanceof ServerPlayer serverPlayer) {
                EventResult eventResult = ArcEntityEvent.PLAYER_KILL_ENTITY.invoker().onPlayerKillEntity(serverPlayer, entity, source);
                if (eventResult.cancelsEvent()) {
                    return dev.architectury.event.EventResult.interruptFalse();
                }
            }
            return dev.architectury.event.EventResult.pass();
        });
        EntityEvent.ANIMAL_TAME.register((animal, player) ->
                ArcEntityEvent.TAME_ANIMAL.invoker().onTameAnimal(animal, player).toArchEventResult());
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) ->
                ArcEntityEvent.INTERACT_WITH_ENTITY.invoker().onInteractWithEntity(player, entity, hand).toArchEventResult());


        ArcEntityEvent.PLAYER_DEATH.register((serverPlayer, damageSource) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.DEATH)
                        .withData(IActionDataType.ENTITY, damageSource.getEntity())
                        .withData(IActionDataType.DAMAGE_SOURCE, damageSource)
                        .withData(IActionDataType.BLOCK_POSITION, serverPlayer.blockPosition())
                        .withData(IActionDataType.WORLD, serverPlayer.level())
                        .withData(IActionDataType.EXP_DROP, serverPlayer.getExperienceReward(serverPlayer.level(), damageSource.getEntity()))
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        ArcEntityEvent.PLAYER_KILL_ENTITY.register((serverPlayer, entity, damageSource) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.KILL_ENTITY)
                        .withData(IActionDataType.ENTITY, entity)
                        .withData(IActionDataType.DAMAGE_SOURCE, damageSource)
                        .withData(IActionDataType.BLOCK_POSITION, entity.blockPosition())
                        .withData(IActionDataType.WORLD, entity.level())
                        .withData(IActionDataType.EXP_DROP, entity.getExperienceReward((ServerLevel) entity.level(), entity))
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        ArcEntityEvent.TAME_ANIMAL.register((animal, player) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.TAME_ANIMAL)
                        .withData(IActionDataType.ENTITY, animal)
                        .withData(IActionDataType.BLOCK_POSITION, animal.blockPosition())
                        .withData(IActionDataType.WORLD, animal.level())
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        });

        ArcEntityEvent.INTERACT_WITH_ENTITY.register((player, entity, hand) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.INTERACT_ENTITY)
                        .withData(IActionDataType.ITEM_STACK, player.getItemInHand(hand))
                        .withData(IActionDataType.ITEM, player.getItemInHand(hand).getItem())
                        .withData(IActionDataType.HAND, hand)
                        .withData(IActionDataType.ENTITY, entity)
                        .withData(IActionDataType.WORLD, entity.level())
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        });

        ArcEntityEvent.PLAYER_HURT_ENTITY.register((serverPlayer, entity, damageSource, damage) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.HURT_ENTITY)
                        .withData(IActionDataType.ENTITY, entity)
                        .withData(IActionDataType.DAMAGE_SOURCE, damageSource)
                        .withData(IActionDataType.DAMAGE_AMOUNT, damage.floatValue())
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }

                if (actionResult.getDamageModifier() != 1F) {
                    damage.setValue(damage.getValue() * actionResult.getDamageModifier());
                }
            }
            return EventResult.PASS;
        });

        ArcEntityEvent.BREED_ANIMAL.register((serverLevel, serverPlayer, baby) -> {
            if (serverPlayer instanceof ArcServerPlayer arcServerPlayer) {
                var actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.BREED_ANIMAL)
                        .withData(IActionDataType.ENTITY, baby)
                        .build()
                        .sendToAction();
                if (actionResult.shouldCancelAction()) {
                    return EventResult.INTERRUPT_FALSE;
                }
            }
            return EventResult.PASS;
        }, EventPriority.HIGH);

        ArcEntityEvent.TRADE_WITH_VILLAGER.register((player, merchant, offer, boughtStack) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer && merchant instanceof Entity merchantEntity) {
                new ActionDataBuilder(arcServerPlayer, IActionType.TRADE_WITH_VILLAGER)
                        .withData(IActionDataType.ENTITY, merchantEntity)
                        .withData(IActionDataType.ITEM_STACK, boughtStack)
                        .withData(IActionDataType.TRADE_OFFER, offer)
                        .withData(IActionDataType.WORLD, player.level())
                        .build()
                        .sendToAction();
            }
        }, EventPriority.HIGH);
    }
}