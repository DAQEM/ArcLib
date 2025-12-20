package com.daqem.arc.data.condition.world;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BiomeCondition extends AbstractCondition {

    private final List<Identifier> biomes;

    public BiomeCondition(boolean inverted, List<Identifier> biomes) {
        super(inverted);
        this.biomes = biomes;
    }

    @Override
    public Component getDescription() {
        String biomesString = biomes.stream().map(Identifier::toString).collect(Collectors.joining(", "));
        return getDescription(biomesString);
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Holder<Biome> biome = actionData.getPlayer().arc$getPlayer().level().getBiome(actionData.getPlayer().arc$getPlayer().blockPosition());
        return biome.unwrapKey().map(key -> biomes.contains(key.identifier())).orElse(false);
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.BIOME;
    }

    public static class Serializer implements IConditionSerializer<BiomeCondition> {

        @Override
        public BiomeCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            List<Identifier> biomes = new ArrayList<>();
            JsonArray biomesArray = jsonObject.getAsJsonArray("biomes");
            for (JsonElement biomeElement : biomesArray) {
                biomes.add(Identifier.parse(biomeElement.getAsString()));
            }
            return new BiomeCondition(inverted, biomes);
        }

        @Override
        public BiomeCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new BiomeCondition(inverted, friendlyByteBuf.readList(FriendlyByteBuf::readIdentifier));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, BiomeCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeCollection(type.biomes, FriendlyByteBuf::writeIdentifier);
        }
    }
}