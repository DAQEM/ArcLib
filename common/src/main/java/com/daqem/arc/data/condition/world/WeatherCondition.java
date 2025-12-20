package com.daqem.arc.data.condition.world;

import com.daqem.arc.api.condition.AbstractCondition;
import com.daqem.arc.api.condition.IConditionSerializer;
import com.daqem.arc.api.condition.IConditionType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.ArcWeatherType;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public class WeatherCondition extends AbstractCondition {

    private final ArcWeatherType weatherType;

    public WeatherCondition(boolean inverted, ArcWeatherType weatherType) {
        super(inverted);
        this.weatherType = weatherType;
    }

    @Override
    public Component getDescription() {
        return getDescription(weatherType.name());
    }

    @Override
    public boolean isMet(ActionData actionData) {
        Level level = actionData.getPlayer().arc$getLevel();
        return switch (weatherType) {
            case CLEAR -> !level.isRaining() && !level.isThundering();
            case RAIN -> level.isRaining();
            case THUNDER -> level.isThundering();
        };
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.WEATHER;
    }

    public static class Serializer implements IConditionSerializer<WeatherCondition> {

        @Override
        public WeatherCondition fromJson(Identifier location, JsonObject jsonObject, boolean inverted) {
            return new WeatherCondition(
                    inverted,
                    getWeatherType(jsonObject, "weather")
            );
        }

        @Override
        public WeatherCondition fromNetwork(Identifier location, RegistryFriendlyByteBuf friendlyByteBuf, boolean inverted) {
            return new WeatherCondition(
                    inverted,
                    friendlyByteBuf.readEnum(ArcWeatherType.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, WeatherCondition type) {
            IConditionSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeEnum(type.weatherType);
        }
    }
}