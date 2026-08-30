package io.marrybye.github.larperthanwolves.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.marrybye.github.larperthanwolves.LarperThanWolves;
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

        // 1. Static base pivot post: 4x2x4 centered at (0, 0, 0)
        root.addOrReplaceChild("base", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 4.0F),
                PartPose.ZERO);

        // 2. Rotating handle: Collar (5x2x5), Arm (2x2x6), Grip Peg (2x5x2)
        root.addOrReplaceChild("handle", CubeListBuilder.create()
                // Central collar
                .texOffs(0, 6).addBox(-2.5F, 1.9F, -2.5F, 5.0F, 2.0F, 5.0F)
                // Horizontal arm (extends along Z+)
                .texOffs(0, 13).addBox(-1.0F, 2.0F, 0.0F, 2.0F, 2.0F, 6.0F)
                // Vertical grip peg
                .texOffs(16, 0).addBox(-1.0F, 4.0F, 4.0F, 2.0F, 5.0F, 2.0F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void render(MillCrankBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.0D, 0.5D);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));

        // Render static base
        this.base.render(poseStack, vertexConsumer, packedLight, packedOverlay);

        // Render rotating handle
        float angle = be.getInterpolatedAngle(partialTick);
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        this.handle.render(poseStack, vertexConsumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
