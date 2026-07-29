package com.daqem.arc.data.reward.combat;

import com.daqem.arc.api.action.data.IActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.math.INumberProvider;
import com.daqem.arc.api.math.INumberProviderSerializer;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.IRewardSerializer;
import com.daqem.arc.api.reward.IRewardType;
import com.daqem.arc.data.ActionData;
import com.daqem.arc.data.math.ConstantNumberProvider;
import com.google.gson.JsonObject;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

public class MultipleArrowsReward extends AbstractReward {

    private final INumberProvider amount;

    public MultipleArrowsReward(double chance, int priority, INumberProvider amount) {
        super(chance, priority);
        this.amount = amount;
    }

    @Override
    public Component getDescription() {
        return getDescription(amount.getDescription());
    }

    @Override
    public IRewardType<?> getType() {
        return IRewardType.MULTIPLE_ARROWS;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Entity entity = actionData.getData(IActionDataType.ENTITY);
        int resolvedAmount = (int) Math.round(amount.resolve(actionData));

        if (resolvedAmount < 1) return new ActionResult();
        if (resolvedAmount > 25) resolvedAmount = 25; // Limit to 25 arrows to prevent lag

        if (entity instanceof AbstractArrow) {
            Player player = actionData.getPlayer().arc$getPlayer();
            ItemStack bow;
            if (player.getMainHandItem().getItem() instanceof BowItem) {
                bow = player.getMainHandItem();
            } else if (player.getOffhandItem().getItem() instanceof BowItem) {
                bow = player.getOffhandItem();
            } else {
                return new ActionResult();
            }
            if (bow.getItem() instanceof BowItem bowItem) {
                float power = BowItem.getPowerForTime(bowItem.getUseDuration(bow, player) - player.getUseItemRemainingTicks());
                float[] afloat = getShotPitches(new Random());
                int[] arrowPositions = scatterArrows(resolvedAmount);
                for (int i = 0; i < resolvedAmount; i++) {
                    shootProjectile(player.level(), player, bow, Items.ARROW.getDefaultInstance(), afloat[1], power * 3, arrowPositions[i]);
                }
            }
        }
        return new ActionResult();
    }

    public static int[] scatterArrows(int numArrows) {
        if (numArrows == 1) return new int[]{0};
        int[] arrowPositions = new int[numArrows];
        double interval = 20.0 / (numArrows - 1);
        for (int i = 0; i < numArrows; i++) {
            arrowPositions[i] = (int) (-10 + i * interval);
        }
        return arrowPositions;
    }

    private void shootProjectile(Level level, LivingEntity livingEntity, ItemStack bow, ItemStack arrow, float shotPitch, float power, float pitch) {
        if (livingEntity instanceof Player player) {
            AbstractArrow projectile = new AbstractArrow(EntityTypes.ARROW, livingEntity, level, arrow, bow) {
                @Override
                protected @NotNull ItemStack getDefaultPickupItem() {
                    return arrow;
                }

                @Override
                protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
                    super.onHitBlock(blockHitResult);
                    this.discard();
                }
            };
            projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            Vec3 vec3 = player.getUpVector(1.0f);
            Quaternionf quaternionf = new Quaternionf().setAngleAxis(pitch * ((float) Math.PI / 180), vec3.x, vec3.y, vec3.z);
            Vec3 vec32 = player.getViewVector(1.0f);
            Vector3f vector3f = vec32.toVector3f().rotate(quaternionf);
            projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), power, 1.0F);

            bow.hurtAndBreak(1, player, player.getUsedItemHand());
            level.addFreshEntity(projectile);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, shotPitch);
        }
    }

    private float[] getShotPitches(Random random) {
        boolean flag = random.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(flag, random), getRandomShotPitch(!flag, random)};
    }

    private float getRandomShotPitch(boolean p_150798_, Random random) {
        float f = p_150798_ ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    public INumberProvider getAmount() {
        return amount;
    }

    public static class Serializer implements IRewardSerializer<MultipleArrowsReward> {

        @Override
        public MultipleArrowsReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new MultipleArrowsReward(chance, priority, getNumberProvider(jsonObject, "amount", new ConstantNumberProvider(1.0)));
        }

        @Override
        public MultipleArrowsReward fromNetwork(RegistryFriendlyByteBuf buf, double chance, int priority) {
            return new MultipleArrowsReward(chance, priority, INumberProviderSerializer.fromNetworkStatic(buf));
        }

        @Override
        public void toNetwork(RegistryFriendlyByteBuf buf, MultipleArrowsReward type) {
            IRewardSerializer.super.toNetwork(buf, type);
            INumberProviderSerializer.toNetwork(type.amount, buf);
        }
    }
}