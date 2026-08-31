package io.marrybye.github.larperthanwolves.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.marrybye.github.larperthanwolves.LarperThanWolves;
import io.marrybye.github.larperthanwolves.block.MillCrankBlock;
import io.marrybye.github.larperthanwolves.block.entity.MillCrankBlockEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class MillCrankRenderer implements BlockEntityRenderer<MillCrankBlockEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(LarperThanWolves.MODID, "mill_crank"), "main");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            LarperThanWolves.MODID, "textures/block/mill_crank.png");

    private final ModelPart base;
    private final ModelPart handle;

    public MillCrankRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart root = context.bakeLayer(LAYER_LOCATION);
        this.base = root.getChild("base");
        this.handle = root.getChild("handle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // 1. Static base pivot post: 6x2x6 centered at (0, 0, 0)
        root.addOrReplaceChild("base", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-3.0F, 0.0F, -3.0F, 6.0F, 2.0F, 6.0F),
                PartPose.ZERO);

        // 2. Rotating large handle (Create-inspired style: large hub, long horizontal arm, vertical upright grip peg with top knob)
        root.addOrReplaceChild("handle", CubeListBuilder.create()
                // Central hub/collar
                .texOffs(0, 8).addBox(-2.5F, 1.9F, -2.5F, 5.0F, 2.0F, 5.0F)
                // Long horizontal arm (3x2x8, extends from Z = -1 to Z = 7)
                .texOffs(0, 16).addBox(-1.5F, 2.0F, -1.0F, 3.0F, 2.0F, 8.0F)
                // Vertical upright grip handle (2x7x2 at end of arm, Z = 5)
                .texOffs(16, 0).addBox(-1.0F, 4.0F, 5.0F, 2.0F, 7.0F, 2.0F)
                // Handle top knob / rounded cap
                .texOffs(24, 0).addBox(-1.5F, 10.0F, 4.5F, 3.0F, 1.0F, 3.0F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void render(MillCrankBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Direction facing = be.getBlockState().hasProperty(MillCrankBlock.FACING)
                ? be.getBlockState().getValue(MillCrankBlock.FACING) : Direction.UP;

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);

        // Rotate according to the 6 possible attached block facings
        switch (facing) {
            case DOWN -> poseStack.mulPose(Axis.XP.rotationDegrees(180));
            case NORTH -> poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            case SOUTH -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
            case WEST -> poseStack.mulPose(Axis.ZP.rotationDegrees(90));
            case EAST -> poseStack.mulPose(Axis.ZP.rotationDegrees(-90));
            case UP -> {
                // Default UP orientation
            }
        }
        poseStack.translate(0.0D, -0.5D, 0.0D);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));

        // Render static base
        this.base.render(poseStack, vertexConsumer, packedLight, packedOverlay);

        // Render rotating handle around outward normal axis
        float angle = be.getInterpolatedAngle(partialTick);
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        this.handle.render(poseStack, vertexConsumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
