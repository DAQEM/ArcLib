package com.daqem.arc.event.triggers;

import com.daqem.arc.api.action.type.ActionType;
import com.daqem.arc.api.player.ArcServerPlayer;
import com.daqem.arc.api.action.data.ActionDataBuilder;
import com.daqem.arc.api.action.data.type.ActionDataType;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.world.entity.animal.Animal;

public class EntityEvents {

    public static void registerEvents() {
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (entity instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, ActionType.DEATH)
                        .withData(ActionDataType.DAMAGE_SOURCE, source)
                        .build()
                        .sendToAction();
            } else if (source.getEntity() instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, ActionType.KILL_ENTITY)
                        .withData(ActionDataType.ENTITY, entity)
                        .withData(ActionDataType.EXP_DROP, entity.getExperienceReward())
                        .build()
                        .sendToAction();
            }
            return EventResult.pass();
        });

        EntityEvent.LIVING_HURT.register((entity, source, amount) -> {
            if (entity instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, ActionType.GET_HURT)
                        .withData(ActionDataType.DAMAGE_SOURCE, source)
                        .withData(ActionDataType.DAMAGE_AMOUNT, amount)
                        .build()
                        .sendToAction();
            }

            if (source.getEntity() instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, ActionType.HURT_ENTITY)
                        .withData(ActionDataType.ENTITY, entity)
                        .withData(ActionDataType.DAMAGE_AMOUNT, amount)
                        .build()
                        .sendToAction();
            }
            return EventResult.pass();
        });

        EntityEvent.ANIMAL_TAME.register((animal, player) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, ActionType.TAME_ANIMAL)
                        .withData(ActionDataType.ENTITY, animal)
                        .build()
                        .sendToAction();
            }
            return EventResult.pass();
        });

        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            if (player instanceof ArcServerPlayer arcServerPlayer) {
                new ActionDataBuilder(arcServerPlayer, ActionType.INTERACT_ENTITY)
                        .withData(ActionDataType.ITEM_STACK, player.getItemInHand(hand))
                        .withData(ActionDataType.ITEM, player.getItemInHand(hand).getItem())
                        .withData(ActionDataType.ENTITY, entity)
                        .build()
                        .sendToAction();
            }
            return EventResult.pass();
        });
    }

    public static void onBreedAnimal(ArcServerPlayer player, Animal animal) {
        new ActionDataBuilder(player, ActionType.BREED_ANIMAL)
                .withData(ActionDataType.ENTITY, animal)
                .build()
                .sendToAction();
    }
}
