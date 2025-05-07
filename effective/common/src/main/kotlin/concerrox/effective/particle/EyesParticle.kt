package concerrox.effective.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import concerrox.effective.EffectiveConfig
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.client.renderer.LightTexture
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.concurrent.ThreadLocalRandom

class EyesParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    dx: Double,
    dy: Double,
    dz: Double,
    private val spriteSet: SpriteSet
) : TextureSheetParticle(level, x, y, z, dx, dy, dz) {

    init {
        roll = 0F
        hasPhysics = true
        quadSize *= 1F + random.nextFloat()
        lifetime = ThreadLocalRandom.current().nextInt(400, 1201)
        setSprite(spriteSet.get(0, 3))
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        // disable if night vision or config is set to disabled
        if (camera.entity is LivingEntity && (camera.entity as LivingEntity).hasEffect(
                MobEffects.NIGHT_VISION) || EffectiveConfig.eyesInTheDark.get() == EffectiveConfig.EyesInTheDarkOptions.NEVER) {
            remove()
        }
        val quaternion: Quaternionf
        if (roll == 0F) {
            quaternion = camera.rotation()
        } else {
            quaternion = Quaternionf(camera.rotation())
            val i = Mth.lerp(tickDelta, oRoll, roll)
            quaternion.rotateZ(i)
        }
        renderRotatedQuad(vertexConsumer, camera, quaternion, tickDelta)
    }

    override fun renderRotatedQuad(
        vertexConsumer: VertexConsumer, camera: Camera, quaternion: Quaternionf, tickDelta: Float
    ) {
        val vec3d = camera.position
        val g = (Mth.lerp(tickDelta.toDouble(), xo, x) - vec3d.x).toFloat()
        val h = (Mth.lerp(tickDelta.toDouble(), yo, y) - vec3d.y).toFloat()
        val i = (Mth.lerp(tickDelta.toDouble(), zo, z) - vec3d.z).toFloat()
        renderRotatedQuad(vertexConsumer, quaternion, g, h, i, tickDelta)
    }

    override fun renderRotatedQuad(
        vertexConsumer: VertexConsumer, quaternion: Quaternionf, g: Float, h: Float, i: Float, tickDelta: Float
    ) {
        val vec3f = Vector3f(-1.0f, -1.0f, 0.0f)
        vec3f.rotate(quaternion)
        val vector3fs = arrayOf(Vector3f(-1.0f, -1.0f, 0.0f), Vector3f(-1.0f, 1.0f, 0.0f), Vector3f(1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, -1.0f, 0.0f))

        for (k in 0..3) {
            val vector3f = vector3fs[k]
            vector3f.rotate(quaternion)
            vector3f.mul(getQuadSize(tickDelta))
            vector3f.add(g, h, i)
        }

        val minU = u0
        val maxU = u1
        val minV = v0
        val maxV = v1
        val lightColor = LightTexture.FULL_BRIGHT

        vertexConsumer.addVertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z())
            .setUv(maxU, maxV).setColor(rCol, gCol, bCol, alpha).setLight(lightColor)
        vertexConsumer.addVertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z())
            .setUv(maxU, minV).setColor(rCol, gCol, bCol, alpha).setLight(lightColor)
        vertexConsumer.addVertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z())
            .setUv(minU, minV).setColor(rCol, gCol, bCol, alpha).setLight(lightColor)
        vertexConsumer.addVertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z())
            .setUv(minU, maxV).setColor(rCol, gCol, bCol, alpha).setLight(lightColor)
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun tick() {
        if (age++ < lifetime) {
            if (age < 1) {
                setSprite(spriteSet.get(0, 3))
            } else if (age < 2) {
                setSprite(spriteSet.get(1, 3))
            } else if (age < 3) {
                setSprite(spriteSet.get(2, 3))
            } else {
                setSprite(spriteSet.get(3, 3))
            }
        } else {
            if (age < lifetime + 1) {
                setSprite(spriteSet.get(2, 3))
            } else if (age < lifetime + 2) {
                setSprite(spriteSet.get(1, 3))
            } else if (age < lifetime + 3) {
                setSprite(spriteSet.get(0, 3))
            } else {
                remove()
            }
        }

        xo = x
        yo = y
        zo = z

        // disappear if light or if player gets too close
        if (lifetime > age && (level.getMaxLocalRawBrightness(BlockPos.containing(x, y, z)) > 0 /*|| level.getNearestPlayer(x, y, z,
                EffectiveCosmetics.EYES_VANISHING_DISTANCE, false) != null*/)) {
            lifetime = age
        }
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
            return EyesParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }

}