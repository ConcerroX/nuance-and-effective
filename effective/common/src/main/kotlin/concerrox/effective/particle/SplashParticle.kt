package concerrox.effective.particle

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import concerrox.effective.Effective
import concerrox.effective.particle.model.SplashBottomModel
import concerrox.effective.particle.model.SplashBottomRimModel
import concerrox.effective.particle.model.SplashModel
import concerrox.effective.particle.model.SplashRimModel
import concerrox.effective.particle.type.SplashParticleType
import concerrox.effective.registry.ModParticles
import concerrox.effective.util.alphaFloat
import concerrox.effective.util.blueFloat
import concerrox.effective.util.greenFloat
import concerrox.effective.util.nextDoubleOrNegative
import concerrox.effective.util.redFloat
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import org.joml.Vector3f
import java.awt.Color
import kotlin.math.roundToInt

open class SplashParticle(level: ClientLevel, x: Double, y: Double, z: Double) : Particle(level, x, y, z) {

    private val models: EntityModelSet = Minecraft.getInstance().entityModels
    var widthMultiplier: Float
    var heightMultiplier: Float
    var wave1End: Int
    var wave2Start: Int
    var wave2End: Int
    private var waterColor: Int = -1
    private var waveModel = SplashModel<Entity>(models.bakeLayer(SplashModel.MODEL_LAYER))
    private var waveBottomModel = SplashBottomModel<Entity>(models.bakeLayer(SplashBottomModel.MODEL_LAYER))
    private var waveRimModel: Model = SplashRimModel<Entity>(models.bakeLayer(SplashRimModel.MODEL_LAYER))
    private var waveBottomRimModel: Model = SplashBottomRimModel<Entity>(
        models.bakeLayer(SplashBottomRimModel.MODEL_LAYER))
    private var blockPos: BlockPos

    init {
        gravity = 0.0f
        widthMultiplier = 0f
        heightMultiplier = 0f
        wave1End = 12
        wave2Start = 7
        wave2End = 24
        blockPos = BlockPos.containing(x, y, z)
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.CUSTOM
    }

    override fun render(buffer: VertexConsumer, camera: Camera, partialTicks: Float) {
        // first splash
        if (age <= wave1End) {
            drawSplash(((age / wave1End.toFloat()) * MAX_FRAME).roundToInt(), camera, partialTicks)
        }
        // second splash
        if (age >= wave2Start) {
            val frame = (((age - wave2Start).toFloat() / (wave2End - wave2Start).toFloat()) * MAX_FRAME)
            drawSplash(frame.roundToInt(), camera, partialTicks, Vector3f(0.5F, 2F, 0.5F))
        }
    }

    private fun drawSplash(frame: Int, camera: Camera, tickDelta: Float, multiplier: Vector3f = Vector3f(1f, 1f, 1f)) {
        if (waterColor == -1) {
            waterColor = BiomeColors.getAverageWaterColor(level, BlockPos.containing(this.x, this.y, this.z))
        }
        val r = (waterColor shr 16 and 0xFF).toFloat() / 255.0f
        val g = (waterColor shr 8 and 0xFF).toFloat() / 255.0f
        val b = (waterColor and 0xFF).toFloat() / 255.0f

        val texture = Effective.id("textures/entity/splash/splash_" + Mth.clamp(frame, 0, MAX_FRAME) + ".png")
        val layer = RenderType.entityTranslucent(texture)
        val rimTexture = Effective.id("textures/entity/splash/splash_rim_" + Mth.clamp(frame, 0, MAX_FRAME) + ".png")
        val rimLayer = RenderType.entityTranslucent(rimTexture)

        // splash matrices
        val modelMatrix = getMatrixStackFromCamera(camera, tickDelta)
        modelMatrix.scale(widthMultiplier * multiplier.x(), -heightMultiplier * multiplier.y(),
            widthMultiplier * multiplier.z())
        modelMatrix.translate(0.0, -1.0, 0.0)
        val modelBottomMatrix = getMatrixStackFromCamera(camera, tickDelta)
        modelBottomMatrix.scale(widthMultiplier * multiplier.x(), heightMultiplier * multiplier.y(),
            widthMultiplier * multiplier.z())
        modelBottomMatrix.translate(0.0, 0.001, 0.0)

        // splash bottom matrices
        val modelRimMatrix = getMatrixStackFromCamera(camera, tickDelta)
        modelRimMatrix.scale(widthMultiplier * multiplier.x(), -heightMultiplier * multiplier.y(),
            widthMultiplier * multiplier.z())
        modelRimMatrix.translate(0.0, -1.0, 0.0)
        val modelRimBottomMatrix = getMatrixStackFromCamera(camera, tickDelta)
        modelRimBottomMatrix.scale(widthMultiplier * multiplier.x(), heightMultiplier * multiplier.y(),
            widthMultiplier * multiplier.z())
        modelRimBottomMatrix.translate(0.0, 0.001, 0.0)

        val light = getLightColor(tickDelta)
        val rimLight = getRimBrightness(tickDelta)

        val bufferSource = Minecraft.getInstance().renderBuffers().bufferSource()
        val modelConsumer = bufferSource.getBuffer(layer)
        waveModel.renderToBuffer(modelMatrix, modelConsumer, light, OverlayTexture.NO_OVERLAY, r, g, b, 0.9F)
        waveBottomModel.renderToBuffer(modelBottomMatrix, modelConsumer, light, OverlayTexture.NO_OVERLAY, r, g, b,
            0.9F)

        val rimModelConsumer = bufferSource.getBuffer(rimLayer)
        val rimColor = getRimColor(blockPos)
        waveRimModel.renderToBuffer(modelRimMatrix, rimModelConsumer, rimLight, OverlayTexture.NO_OVERLAY,
            rimColor.redFloat, rimColor.greenFloat, rimColor.blueFloat, rimColor.alphaFloat)
        waveBottomRimModel.renderToBuffer(modelRimBottomMatrix, rimModelConsumer, rimLight, OverlayTexture.NO_OVERLAY,
            rimColor.redFloat, rimColor.greenFloat, rimColor.blueFloat, rimColor.alphaFloat)

        bufferSource.endBatch()
    }

    private fun getMatrixStackFromCamera(camera: Camera, tickDelta: Float): PoseStack {
        return PoseStack().apply {
            val cameraPos = camera.position
            val x = (Mth.lerp(tickDelta.toDouble(), xo, x) - cameraPos.x).toFloat()
            val y = (Mth.lerp(tickDelta.toDouble(), yo, y) - cameraPos.y).toFloat()
            val z = (Mth.lerp(tickDelta.toDouble(), zo, z) - cameraPos.z).toFloat()
            translate(x, y, z)
        }
    }

    override fun tick() {
        if (widthMultiplier == 0f) {
            remove()
        }

        xo = x
        yo = y
        zo = z

        widthMultiplier *= 1.03f

        if (age++ >= wave2End) {
            remove()
        }

        if (age == 1) {
            var i = 0
            while (i < widthMultiplier * 10.0) {
                level.addParticle(getDropletParticle(), x + (random.nextDoubleOrNegative() * widthMultiplier / 5f), y,
                    z + (random.nextDoubleOrNegative() * widthMultiplier / 5.0),
                    random.nextDoubleOrNegative() / 5.0 * widthMultiplier / 2.5,
                    random.nextDouble() / 10.0 + heightMultiplier / 2.8,
                    random.nextDoubleOrNegative() / 10.0 * widthMultiplier / 2.5)
                i++
            }
        } else if (age == wave2Start) {
            var i = 0
            while (i < widthMultiplier * 5.0) {
                level.addParticle(getDropletParticle(),
                    x + (random.nextDoubleOrNegative() * widthMultiplier / 5.0 * 0.5), y,
                    z + (random.nextDoubleOrNegative() * widthMultiplier / 5.0 * 0.5),
                    random.nextDoubleOrNegative() / 10.0 * widthMultiplier / 5.0,
                    random.nextDouble() / 10.0 + heightMultiplier / 2.2,
                    random.nextDoubleOrNegative() / 10.0 * widthMultiplier / 5.0)
                i++
            }
        }
    }

    open fun getRimBrightness(tickDelta: Float): Int {
        return getLightColor(tickDelta)
    }

    open fun getRimColor(pos: BlockPos): Color {
        return Color(0xFFFFFFFF.toInt())
    }

    open fun getDropletParticle(): SimpleParticleType {
        return ModParticles.DROPLET
    }

    @Environment(EnvType.CLIENT)
    internal class Provider(private val spriteSet: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            type: SimpleParticleType,
            level: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            val instance = SplashParticle(level, x, y, z)
            (type as SplashParticleType).initialData?.let {
                val width = it.width * 2
                instance.widthMultiplier = width
                instance.heightMultiplier = it.velocityY.toFloat() * width
                instance.wave1End = 10 + Math.round(width * 1.2f)
                instance.wave2Start = 6 + Math.round(width * 0.7f)
                instance.wave2End = 20 + Math.round(width * 2.4f)
            }
            return instance
        }
    }

    companion object {
        const val MAX_FRAME: Int = 12
    }
}