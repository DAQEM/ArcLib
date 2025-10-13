package com.daqem.arc.data.reward.world;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.model.target.ArcPositionTarget;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class PlaySoundReward extends AbstractReward {

    private final Holder<SoundEvent> soundEvent;
    private final float volume;
    private final float pitch;
    private final ArcPositionTarget positionTarget;
    private final SoundSource soundSource;

    public PlaySoundReward(double chance, int priority, Holder<SoundEvent> soundEvent, float volume, float pitch, ArcPositionTarget positionTarget, SoundSource soundSource) {
        super(chance, priority);
        this.soundEvent = soundEvent;
        this.volume = volume;
        this.pitch = pitch;
        this.positionTarget = positionTarget;
        this.soundSource = soundSource;
    }

    @Override
    public Component getDescription() {
        return getDescription(soundEvent.value().location());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getPlayer() instanceof ServerPlayer serverPlayer) {
            Vec3 position = positionTarget.getPosition(actionData);
            Entity causingEntity = positionTarget.getEntity(actionData);
            if (position != null) {
                serverPlayer.level().playSound(causingEntity, position.x, position.y, position.z, soundEvent.value(), soundSource, volume, pitch);
            }
        }
        return new ActionResult();
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.PLAY_SOUND;
    }

    public static class Serializer implements IRewardSerializer<PlaySoundReward> {

        @Override
        public PlaySoundReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new PlaySoundReward(
                    chance,
                    priority,
                    getSoundEvent(jsonObject, "sound"),
                    GsonHelper.getAsFloat(jsonObject, "volume", 1.0f),
                    GsonHelper.getAsFloat(jsonObject, "pitch", 1.0f),
                    getPositionTarget(jsonObject, "position", ArcPositionTarget.PLAYER),
                    getSoundSource(jsonObject, "sound_source", SoundSource.PLAYERS)
            );
        }

        @Override
        public PlaySoundReward fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new PlaySoundReward(
                    chance,
                    priority,
                    SoundEvent.STREAM_CODEC.decode(friendlyByteBuf),
                    friendlyByteBuf.readFloat(),
                    friendlyByteBuf.readFloat(),
                    friendlyByteBuf.readEnum(ArcPositionTarget.class),
                    friendlyByteBuf.readEnum(SoundSource.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, PlaySoundReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            SoundEvent.STREAM_CODEC.encode(friendlyByteBuf, type.soundEvent);
            friendlyByteBuf.writeFloat(type.volume);
            friendlyByteBuf.writeFloat(type.pitch);
            friendlyByteBuf.writeEnum(type.positionTarget);
            friendlyByteBuf.writeEnum(type.soundSource);
        }
    }
}