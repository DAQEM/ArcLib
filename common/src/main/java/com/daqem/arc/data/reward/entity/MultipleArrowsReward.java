package com.daqem.arc.data.reward.entity;

import com.daqem.arc.Arc;
import com.daqem.arc.api.action.data.ActionData;
import com.daqem.arc.api.action.data.type.ActionDataType;
import com.daqem.arc.api.action.result.ActionResult;
import com.daqem.arc.api.reward.AbstractReward;
import com.daqem.arc.api.reward.serializer.IRewardSerializer;
import com.daqem.arc.api.reward.serializer.RewardSerializer;
import com.daqem.arc.api.reward.type.IRewardType;
import com.daqem.arc.api.reward.type.RewardType;
import com.google.gson.JsonObject;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MultipleArrowsReward extends AbstractReward {

    private final int amount;

    public MultipleArrowsReward(double chance, int priority, int amount) {
        super(chance, priority);
        this.amount = amount;
    }

    @Override
    public IRewardType<?> getType() {
        return RewardType.MULTIPLE_ARROWS;
    }

    @Override
    public IRewardSerializer<?> getSerializer() {
        return RewardSerializer.MULTIPLE_ARROWS;
    }

    @Override
    public ActionResult apply(ActionData actionData) {
        Entity entity = actionData.getData(ActionDataType.ENTITY);
        if (entity instanceof AbstractArrow shotArrow) {
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
                float power = BowItem.getPowerForTime(bowItem.getUseDuration(bow) - player.getUseItemRemainingTicks());
                float[] afloat = getShotPitches(new Random());
                int[] arrowPositions = scatterArrows(amount);
                for (int i = 0; i < amount; i++) {
                    shootProjectile(shotArrow, player.level, player, bow, Items.ARROW.getDefaultInstance(), afloat[1], power * 3, arrowPositions[i]);
                }
            }
        }
        return new ActionResult();
    }

    public static int[] scatterArrows(int numArrows) {
        if (numArrows < 1 || numArrows > 20) {
            throw new IllegalArgumentException("Number of arrows should be between 1 and 20.");
        }

        int[] arrowPositions = new int[numArrows];
        double interval = 20.0 / (numArrows - 1);

        for (int i = 0; i < numArrows; i++) {
            arrowPositions[i] = (int) (-10 + i * interval);
        }

        return arrowPositions;
    }

    private void shootProjectile(AbstractArrow shotArrow, Level level, LivingEntity livingEntity, ItemStack bow, ItemStack arrow,
                                 float shotPitch, float power, float pitch) {
        if (livingEntity instanceof Player player) {
            AbstractArrow projectile = new AbstractArrow(EntityType.ARROW, livingEntity, level) {

                {
                    int l;
                    int k;
                    this.setBaseDamage(shotArrow.getBaseDamage());
                    this.setKnockback(shotArrow.getKnockback());
                    this.setCritArrow(shotArrow.isCritArrow());
                    this.setSecondsOnFire(shotArrow.getRemainingFireTicks());
                    if (power == 1.0f) {
                        setCritArrow(true);
                    }
                    if ((k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bow)) > 0) {
                        setBaseDamage(getBaseDamage() + (double)k * 0.5 + 0.5);
                    }
                    if ((l = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bow)) > 0) {
                        setKnockback(l);
                    }
                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) > 0) {
                        setSecondsOnFire(100);
                    }
                    this.setSoundEvent(SoundEvents.ARROW_HIT);
                }

                @Override
                public @NotNull ItemStack getPickupItem() {
                    return arrow;
                }

                @Override
                protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
                    super.onHitBlock(blockHitResult);
                    this.discard();
                }
            };
            Arc.LOGGER.info("base: " + projectile.getBaseDamage());
            Arc.LOGGER.info("length: " + projectile.getDeltaMovement().length());
            Arc.LOGGER.info("power: " + power);
            projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            Vector3f vector3f = new Vector3f(player.getViewVector(1.0F));
            vector3f.transform(new Quaternion(new Vector3f(player.getUpVector(1.0F)), pitch, true));
            projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), power, (float) 1.0);

            bow.hurtAndBreak(1, player, player2 -> player2.broadcastBreakEvent(player.getUsedItemHand()));
            level.addFreshEntity(projectile);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, shotPitch);
        }
    }

    private AbstractArrow getArrow(Level p_40915_, LivingEntity p_40916_, ItemStack stack, ItemStack p_40918_) {
        ArrowItem arrowitem = (ArrowItem) (p_40918_.getItem() instanceof ArrowItem ? p_40918_.getItem() : Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(p_40915_, p_40918_, p_40916_);
        if (p_40916_ instanceof Player) {
            abstractarrow.setCritArrow(true);
        }

        abstractarrow.setSoundEvent(SoundEvents.ARROW_HIT);
        abstractarrow.setShotFromCrossbow(true);
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, stack);
        if (i > 0) {
            abstractarrow.setPierceLevel((byte) i);
        }

        return abstractarrow;
    }

    private float[] getShotPitches(Random random) {
        boolean flag = random.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(flag, random), getRandomShotPitch(!flag, random)};
    }

    private float getRandomShotPitch(boolean p_150798_, Random random) {
        float f = p_150798_ ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    public static class Serializer implements RewardSerializer<MultipleArrowsReward> {

        @Override
        public MultipleArrowsReward fromJson(JsonObject jsonObject, double chance, int priority) {
            return new MultipleArrowsReward(
                    chance,
                    priority,
                    Math.min(20, Math.max(1, GsonHelper.getAsInt(jsonObject, "amount"))));
        }

        @Override
        public MultipleArrowsReward fromNetwork(FriendlyByteBuf friendlyByteBuf, double chance, int priority) {
            return new MultipleArrowsReward(
                    chance,
                    priority,
                    friendlyByteBuf.readInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, MultipleArrowsReward type) {
            RewardSerializer.super.toNetwork(friendlyByteBuf, type);
            friendlyByteBuf.writeInt(type.amount);
        }
    }
}
