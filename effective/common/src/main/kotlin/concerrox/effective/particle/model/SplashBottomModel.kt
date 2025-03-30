package concerrox.effective.particle.model

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import net.minecraft.world.entity.Entity
import concerrox.effective.Effective.id

class SplashBottomModel<T : Entity>(root: ModelPart) : EntityModel<T>() {

    private val splash: ModelPart = root.getChild("splash")

    override fun renderToBuffer(
        matrices: PoseStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        splash.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }

    override fun setupAnim(
        entity: T, limbAngle: Float, limbDistance: Float, animationProgress: Float, headYaw: Float, headPitch: Float
    ) {
    }

    companion object {
        val MODEL_LAYER: ModelLayerLocation = ModelLayerLocation(id("splash_bottom"), "main")

        @JvmStatic
        fun createBodyLayer(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData: PartDefinition = modelData.root

            modelPartData.addOrReplaceChild("splash",
                CubeListBuilder.create().texOffs(0, 0).addBox(-6.0f, 0.0f, -6.0f, 12.0f, 0.0f, 12.0f),
                PartPose.offset(0.0f, 0.0f, 0.0f))

            return LayerDefinition.create(modelData, 48, 28)
        }
    }
}