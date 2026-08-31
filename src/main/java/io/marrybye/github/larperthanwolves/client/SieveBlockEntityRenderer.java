package io.marrybye.github.larperthanwolves.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.marrybye.github.larperthanwolves.LarperThanWolves;
import io.marrybye.github.larperthanwolves.block.SieveBlock;
import io.marrybye.github.larperthanwolves.block.entity.SieveBlockEntity;
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

public class SieveBlockEntityRenderer implements BlockEntityRenderer<SieveBlockEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(LarperThanWolves.MODID, "sieve_mesh"), "main");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            LarperThanWolves.MODID, "textures/block/sieve_mesh.png");

    private final ModelPart meshTray;

    public SieveBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart root = context.bakeLayer(LAYER_LOCATION);
        this.meshTray = root.getChild("mesh_tray");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // 10.5 x 0.4 x 10.8 vibrating mesh screen tray centered at (0, 0, 0)
        // Sits cleanly inside the table opening without intersecting side rims or rails
        root.addOrReplaceChild("mesh_tray", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-5.25F, -0.2F, -5.4F, 10.5F, 0.4F, 10.8F),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void render(SieveBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Direction facing = be.getBlockState().hasProperty(SieveBlock.FACING)
                ? be.getBlockState().getValue(SieveBlock.FACING) : Direction.NORTH;

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.8125D, 0.5D);

        // Rotate according to facing
        switch (facing) {
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
            default -> {}
        }

        // Apply smooth horizontal shaking offset (vibrates left/right along local X axis)
        float xOffset = be.getInterpolatedOffset(partialTick);
        poseStack.translate(xOffset, 0.0D, 0.0D);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
        this.meshTray.render(poseStack, vertexConsumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}