package com.daqem.arc.data.condition.combat;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class DamageSourceCondition extends AbstractCondition {

    private final String source;
    @Nullable
    private final EntityType<?> directEntityType;
    @Nullable
    private final EntityType<?> causingEntityType;

    public DamageSourceCondition(boolean inverted, String source, @Nullable EntityType<?> directEntityType, @Nullable EntityType<?> causingEntityType) {
        super(inverted);
        this.source = source;
        this.directEntityType = directEntityType;
        this.causingEntityType = causingEntityType;
    }

    @Override
    public Component getDescription() {
        return getDescription(source);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        DamageSource damageSource = actionData.getData(IActionDataType.DAMAGE_SOURCE);
        if (damageSource != null) {
            if (!source.equals("any") && !damageSource.getMsgId().equals(source)) {
                return false;
            }
            if (directEntityType != null && (damageSource.getDirectEntity() == null || damageSource.getDirectEntity().getType() != directEntityType)) {
                return false;
            }
            if (causingEntityType != null && (damageSource.getEntity() == null || damageSource.getEntity().getType() != causingEntityType)) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.DAMAGE_SOURCE;
    }

    public String getSource() {
        return source;
    }

    @Nullable
    public EntityType<?> getDirectEntityType() {
        return directEntityType;
    }

    @Nullable
    public EntityType<?> getCausingEntityType() {
        return causingEntityType;
    }

    public static class Serializer implements IConditionSerializer<DamageSourceCondition> {

        @Override
        public DamageSourceCondition fromJson(ResourceLocation location, JsonObject jsonObject, boolean inverted) {
            return new DamageSourceCondition(
                    inverted,
                    GsonHelper.getAsString(jsonObject, "source", "any"),
                    getOptionalEntityType(jsonObject, "direct_entity_type"),
                    getOptionalEntityType(jsonObject, "causing_entity_type")
            );
        }

        @Override
        public DamageSourceCondition fromNetwork(ResourceLocation location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new DamageSourceCondition(
                    inverted,
                    friendlyByteBuf.readUtf(),
                    friendlyByteBuf.readBoolean() ? BuiltInRegistries.ENTITY_TYPE.byId(friendlyByteBuf.readVarInt()) : null,
                    friendlyByteBuf.readBoolean() ? BuiltInRegistries.ENTITY_TYPE.byId(friendlyByteBuf.readVarInt()) : null
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, DamageSourceCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeUtf(type.source);
            friendlyByteBuf.writeBoolean(type.directEntityType != null);
            if (type.directEntityType != null) {
                friendlyByteBuf.writeVarInt(BuiltInRegistries.ENTITY_TYPE.getId(type.directEntityType));
            }
            friendlyByteBuf.writeBoolean(type.causingEntityType != null);
            if (type.causingEntityType != null) {
                friendlyByteBuf.writeVarInt(BuiltInRegistries.ENTITY_TYPE.getId(type.causingEntityType));
            }
        }
    }
}
