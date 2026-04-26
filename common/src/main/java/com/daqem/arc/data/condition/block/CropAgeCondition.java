package com.daqem.arc.data.condition.block;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;
import java.util.Optional;

public class CropAgeCondition extends AbstractCondition {

    private final INumberProvider age;

    public CropAgeCondition(boolean inverted, INumberProvider age) {
        super(inverted);
        this.age = age;
    }

    @Override
    public Component getDescription() {
        return getDescription(age.toString());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        BlockState blockState = actionData.getData(IActionDataType.BLOCK_STATE);
        if (blockState != null) {
            Collection<Property<?>> properties = blockState.getProperties();
            Optional<Property<?>> optionalAgeProperty = properties.stream()
                    .filter(property -> property.getName().equals("age"))
                    .findFirst();
            if (optionalAgeProperty.isPresent()) {
                IntegerProperty ageProperty = (IntegerProperty) optionalAgeProperty.get();
                Optional<Integer> optionalAgeValue = blockState.getOptionalValue(ageProperty);
                if (optionalAgeValue.isPresent()) {
                    return optionalAgeValue.get() == Math.round(this.age.resolve(actionData));
                }
            }
        }
        return false;
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.CROP_AGE;
    }

    public INumberProvider getAge() {
        return age;
    }

    public static class Serializer implements IConditionSerializer<CropAgeCondition> {

        @Override
        public CropAgeCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new CropAgeCondition(
                    inverted,
                    getNumberProvider(jsonObject, "age", new ConstantNumberProvider(0.0))
            );
        }

        @Override
        public CropAgeCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new CropAgeCondition(
                    inverted,
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, CropAgeCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            INumberProviderSerializer.toNetwork(type.age, friendlyByteBuf);
        }
    }
}