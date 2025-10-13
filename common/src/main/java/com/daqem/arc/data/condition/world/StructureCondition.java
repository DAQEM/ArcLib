package com.daqem.arc.data.condition.world;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class StructureCondition extends AbstractCondition {

    private final TagKey<Structure> structureTag;

    public StructureCondition(boolean inverted, TagKey<Structure> structureTag) {
        super(inverted);
        this.structureTag = structureTag;
    }

    @Override
    public Component getDescription() {
        return getDescription(structureTag.location());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        if (actionData.getPlayer().arc$getLevel() instanceof ServerLevel serverLevel) {
            return serverLevel.structureManager().getStructureWithPieceAt(actionData.getPlayer().arc$getPlayer().blockPosition(), structureTag).isValid();
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.STRUCTURE;
    }

    public static class Serializer implements IConditionSerializer<StructureCondition> {

        @Override
        public StructureCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new StructureCondition(
                    inverted,
                    TagKey.create(Registries.STRUCTURE, getResourceLocation(jsonObject, "structure"))
            );
        }

        @Override
        public StructureCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new StructureCondition(
                    inverted,
                    TagKey.create(Registries.STRUCTURE, friendlyByteBuf.readResourceLocation())
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, StructureCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeResourceLocation(type.structureTag.location());
        }
    }
}