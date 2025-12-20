package com.daqem.arc.data.condition.advancement;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.ICondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class AdvancementCondition extends AbstractCondition {

    @Nullable
    private final Identifier id;
    @Nullable
    private final Identifier parentId;
    @Nullable
    private final AdvancementType type;

    public AdvancementCondition(boolean inverted, @Nullable Identifier id, @Nullable Identifier parentId, @Nullable AdvancementType type) {
        super(inverted);
        this.id = id;
        this.parentId = parentId;
        this.type = type;
    }

    @Override
    public IConditionType<? extends ICondition> getType() {
        return IConditionType.ADVANCEMENT;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean isMet(ActionData actionData) {
        AdvancementHolder advancementHolder = actionData.getData(IActionDataType.ADVANCEMENT);
        if (advancementHolder != null) {
            if (id != null && !advancementHolder.id().equals(id)) {
                return false;
            }
            Advancement advancement = advancementHolder.value();
            if (parentId != null && advancement.parent().isPresent() && !advancement.parent().get().equals(parentId)) {
                return false;
            }
            if (type != null && advancement.display().isPresent() && advancement.display().get().getType() != type) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static class Serializer implements IConditionSerializer<AdvancementCondition> {

        @Override
        public AdvancementCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new AdvancementCondition(
                    inverted,
                    getOptionalIdentifier(jsonObject, "id"),
                    getOptionalIdentifier(jsonObject, "parent_id"),
                    AdvancementType.CODEC.decode(JsonOps.INSTANCE, jsonObject.get("type")).result().orElse(new Pair<>(null, null)).getFirst()
            );
        }

        @Override
        public AdvancementCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new AdvancementCondition(
                    inverted,
                    friendlyByteBuf.readNullable(FriendlyByteBuf::readIdentifier),
                    friendlyByteBuf.readNullable(FriendlyByteBuf::readIdentifier),
                    friendlyByteBuf.readNullable(buf -> buf.readEnum(AdvancementType.class))
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, AdvancementCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeNullable(type.id, FriendlyByteBuf::writeIdentifier);
            friendlyByteBuf.writeNullable(type.parentId, FriendlyByteBuf::writeIdentifier);
            friendlyByteBuf.writeNullable(type.type, FriendlyByteBuf::writeEnum);
        }
    }
}
