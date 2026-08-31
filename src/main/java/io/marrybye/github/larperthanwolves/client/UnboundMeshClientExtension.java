package io.marrybye.github.larperthanwolves.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.marrybye.github.larperthanwolves.item.UnboundMeshItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class UnboundMeshClientExtension implements IClientItemExtensions {
    public static final UnboundMeshClientExtension INSTANCE = new UnboundMeshClientExtension();

    public static void register(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(INSTANCE);
    }

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        if (player.isUsingItem() && (player.getUseItem() == itemInHand || ItemStack.isSameItem(player.getUseItem(), itemInHand))) {
            int side = (arm == HumanoidArm.RIGHT) ? 1 : -1;
            poseStack.translate(side * 0.56F, -0.52F + equipProcess * -0.6F, -0.72F);
            float useTicks = (float)(itemInHand.getUseDuration(player) - player.getUseItemRemainingTicks()) + partialTick;
            float bob = Mth.abs(Mth.cos(useTicks / 4.0F * (float)Math.PI) * 0.08F);
            poseStack.translate(side * -0.32F, 0.12F + bob, 0.22F);
            poseStack.mulPose(Axis.YP.rotationDegrees(side * 65.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(side * -20.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(-45.0F));
            return true;
        }
        return false;
    }
}
