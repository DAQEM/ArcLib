package com.daqem.arc.api.reward;

import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.result.ActionResult;

public abstract class AbstractReward implements IReward {

    private double chance;

    public AbstractReward(double chance) {
        this.chance = chance;
    }

    @Override
    public double getChance() {
        return chance;
    }

    public abstract ActionResult apply(ActionData actionData);

    public boolean passedChance(ActionData actionData) {
        return chance == 100 || actionData.getPlayer().arc$nextRandomDouble() * 100 <= chance;
    }

//    public static class ActionRewardSerializer<T extends AbstractReward> implements JsonDeserializer<T> {
//
//        private static Gson getGson() {
//            GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
//
//            for (RewardType<?> type : RewardType.ACTION_REWARD_TYPES) {
//                builder.registerTypeAdapter(type.clazz(), type.deserializer());
//            }
//
//            return builder.create();
//        }
//
//        @Override
//        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//            JsonObject rewardObject = json.getAsJsonObject();
//            String type = GsonHelper.getAsString(rewardObject, "type");
//            ResourceLocation location = new ResourceLocation(type);
//            Class<? extends AbstractReward> clazz = Rewards.getClass(location);
//
//            return (T) getGson().fromJson(rewardObject, clazz)
//                    .withChance(GsonHelper.getAsDouble(rewardObject, "chance", 100));
//        }
//    }
}
