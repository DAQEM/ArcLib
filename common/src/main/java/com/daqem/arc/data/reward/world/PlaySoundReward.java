package com.daqem.arc.data.reward.world;

import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.daqem.arc.model.target.ArcPositionTarget;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class PlaySoundReward extends AbstractReward {

    private final Holder<SoundEvent> soundEvent;
    private final INumberProvider volume;
    private final INumberProvider pitch;
    private final ArcPositionTarget positionTarget;
    private final SoundSource soundSource;

    public PlaySoundReward(double chance, int priority, Holder<SoundEvent> soundEvent, INumberProvider volume, INumberProvider pitch, ArcPositionTarget positionTarget, SoundSource soundSource) {
        super(chance, priority);
        this.soundEvent = soundEvent;
        this.volume = volume;
        this.pitch = pitch;
        this.positionTarget = positionTarget;
        this.soundSource = soundSource;
    }

    @Override
    public Component getDescription() {
        return getDescription(soundEvent.value().getLocation().toString());
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        if (actionData.getPlayer() instanceof ServerPlayer serverPlayer) {
            Vec3 position = positionTarget.getPosition(actionData);
            Entity causingEntity = positionTarget.getEntity(actionData);
            if (position != null) {
                float resolvedVolume = (float) volume.resolve(actionData);
                float resolvedPitch = (float) pitch.resolve(actionData);
                serverPlayer.level().playSound(causingEntity, BlockPos.containing(position), soundEvent.value(), soundSource, resolvedVolume, resolvedPitch);
            }
        }
        return new ActionResult();
    }

    public ArcPositionTarget getPositionTarget() {
        return positionTarget;
    }

    public Holder<SoundEvent> getSoundEvent() {
        return soundEvent;
    }

    public INumberProvider getPitch() {
        return pitch;
    }

    public INumberProvider getVolume() {
        return volume;
    }

    public SoundSource getSoundSource() {
        return soundSource;
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
                    getNumberProvider(jsonObject, "volume", new ConstantNumberProvider(1.0)),
                    getNumberProvider(jsonObject, "pitch", new ConstantNumberProvider(1.0)),
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
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf),
                    INumberProviderSerializer.fromNetworkStatic(friendlyByteBuf),
                    friendlyByteBuf.readEnum(ArcPositionTarget.class),
                    friendlyByteBuf.readEnum(SoundSource.class)
            );
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, PlaySoundReward type) {
            IRewardSerializer.super.toNetwork(friendlyByteBuf, type);
            SoundEvent.STREAM_CODEC.encode(friendlyByteBuf, type.soundEvent);
            INumberProviderSerializer.toNetwork(type.volume, friendlyByteBuf);
            INumberProviderSerializer.toNetwork(type.pitch, friendlyByteBuf);
            friendlyByteBuf.writeEnum(type.positionTarget);
            friendlyByteBuf.writeEnum(type.soundSource);
        }
    }
}