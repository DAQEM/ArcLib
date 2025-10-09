package com.daqem.arc.event;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.action.IActionType;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.IActionDataType;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;

public class EntityEvents {

    public static void registerEvents() {
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.DEATH)
                        .withData(IActionDataType.DAMAGE_SOURCE, source)
                        .build()
                        .sendToAction();
            } else if (source.getEntity() instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, IActionType.KILL_ENTITY)
                        .withData(IActionDataType.ENTITY, entity)
                        .withData(IActionDataType.BLOCK_POSITION, entity.blockPosition())
                        .withData(IActionDataType.WORLD, entity.level())
                        .withData(IActionDataType.EXP_DROP, entity.getExperienceReward((ServerLevel) entity.level(), entity))
                        .build()
                        .sendToAction();
            }
            return EventResult.pass();
        });

        EntityEvent.ANIMAL_TAME.register((animal, player) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.TAME_ANIMAL)
                        .withData(IActionDataType.ENTITY, animal)
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.interruptFalse();
                }
            }
            return EventResult.pass();
        });

        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.INTERACT_ENTITY)
                        .withData(IActionDataType.ITEM_STACK, player.getItemInHand(hand))
                        .withData(IActionDataType.ITEM, player.getItemInHand(hand).getItem())
                        .withData(IActionDataType.ENTITY, entity)
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.interruptFalse();
                }
            }
            return EventResult.pass();
        });

        EntityEvent.LIVING_HURT.register((entity, source, amount) -> {
            if (source.getEntity() instanceof ArcServerPlayer arcServerPlayer) {
                ActionResult actionResult = new ActionDataBuilder(arcServerPlayer, IActionType.HURT_ENTITY)
                        .withData(IActionDataType.ENTITY, entity)
                        .withData(IActionDataType.DAMAGE_SOURCE, source)
                        .withData(IActionDataType.DAMAGE_AMOUNT, amount)
                        .build()
                        .sendToAction();

                if (actionResult.shouldCancelAction()) {
                    return EventResult.interruptFalse();
                }
            }
            return EventResult.pass();
        });
    }

    public static ActionResult onBreedAnimal(ArcServerPlayer player, Animal animal) {
        return new ActionDataBuilder(player, IActionType.BREED_ANIMAL)
                .withData(IActionDataType.ENTITY, animal)
                .build()
                .sendToAction();
    }
}
