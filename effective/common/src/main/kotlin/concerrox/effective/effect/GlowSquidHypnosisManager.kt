package concerrox.effective.effect

import concerrox.effective.Effective
import concerrox.effective.EffectiveConfig
import ladysnake.satin.api.managed.ManagedCoreShader
import ladysnake.satin.api.managed.ManagedShaderEffect
import ladysnake.satin.api.managed.ShaderEffectManager
import ladysnake.satin.api.managed.uniform.Uniform1f
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderType
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.entity.GlowSquid
import net.minecraft.world.phys.Vec2
import kotlin.math.max
import kotlin.math.sqrt


object GlowSquidHypnosisManager {

    var RENDERED_GLOW_SQUIDS = hashSetOf<GlowSquid>()
    private var lockedIntensityTimer = 0
    private var lookIntensity = 0.0
    private var lookIntensityGoal = 0.0

    val RAINBOW_SHADER: ManagedCoreShader = ShaderEffectManager.getInstance().manageCoreShader(Effective.id("jeb"))
    val rainbowSTime: Uniform1f = RAINBOW_SHADER.findUniform1f("Time")
    var rainbowTimer = 0

    private val HYPNOSIS_SHADER: ManagedShaderEffect = ShaderEffectManager.getInstance()
        .manage(Effective.id("shaders/post/hypnotize.json"))
    private val hypnosisIntensity: Uniform1f = HYPNOSIS_SHADER.findUniform1f("Intensity")
    private val hypnosisSTime: Uniform1f = HYPNOSIS_SHADER.findUniform1f("STime")
    private val hypnosisRainbow: Uniform1f = HYPNOSIS_SHADER.findUniform1f("Rainbow")

    internal fun getRainbowShaderRenderType(base: RenderType): RenderType {
        return RAINBOW_SHADER.getRenderLayer(base)
    }

    fun onShaderEffectRendered(tickDelta: Float) {
        if (!EffectiveConfig.canGlowSquidsHypnotize() || !(RENDERED_GLOW_SQUIDS.isNotEmpty() || lockedIntensityTimer > 0 || lookIntensity > 0)) {
            return
        }

        var highestLookIntensity = 0.0
        var bestSquid: GlowSquid? = null
        val player = Minecraft.getInstance().player ?: return
        val isNotPaused = !Minecraft.getInstance().isPaused

        RENDERED_GLOW_SQUIDS.forEach {
            if (it.position().distanceToSqr(player.position()) <= 49) {
                val toSquid = it.position().subtract(player.position()).normalize()
                val lookIntensity = toSquid.dot(player.getViewVector(tickDelta))
                if (lookIntensity > highestLookIntensity && player.hasLineOfSight(it)) {
                    highestLookIntensity = lookIntensity
                    bestSquid = it
                }
            }
        }
        val immutableBestSquid = bestSquid

        lookIntensityGoal = highestLookIntensity

        if (isNotPaused && lockedIntensityTimer >= 0) {
            lookIntensityGoal = 1.0
            lookIntensity += 0.001
            lockedIntensityTimer--
        }
        if (lookIntensity < lookIntensityGoal) {
            lookIntensity += 0.0005
        } else {
            lookIntensity -= 0.001
        }
        lookIntensity = Mth.clamp(lookIntensity, 0.0, 0.5)

        immutableBestSquid?.let {
            if (it.hasCustomName() && it.customName?.string.equals("jeb_")) {
                hypnosisRainbow.set(1.0f)
            } else {
                hypnosisRainbow.set(0.0f)
            }
        }
        Minecraft.getInstance().level?.let {
            hypnosisIntensity.set(max(0F, lookIntensity.toFloat()))
            hypnosisSTime.set((it.gameTime + tickDelta) / 20)
        }
        HYPNOSIS_SHADER.render(tickDelta)

        if (player.tickCount % 20 == 0 && isNotPaused) {
            player.playSound(SoundEvents.GLOW_SQUID_AMBIENT, lookIntensity.toFloat(), lookIntensity.toFloat())
        }

        if (EffectiveConfig.glowSquidHypnosis.get() == EffectiveConfig.GlowSquidHypnosisOptions.ATTRACT && immutableBestSquid != null && isNotPaused) {
            val target = immutableBestSquid.position()
            val vec3d = player.position()

            val d = target.x - vec3d.x
            val e = target.y - vec3d.y - 1
            val f = target.z - vec3d.z
            val g = sqrt(d * d + f * f)

            val currentPitch = Mth.wrapDegrees(player.getViewXRot(tickDelta))
            val currentYaw = Mth.wrapDegrees(player.getViewYRot(tickDelta))
            val desiredPitch = Mth.wrapDegrees((-(Mth.atan2(e, g) * 57.2957763671875)))
            val desiredYaw = Mth.wrapDegrees((Mth.atan2(f, d) * 57.2957763671875) - 90.0)

            val rotationChange = Vec2(Mth.wrapDegrees(desiredPitch - currentPitch).toFloat(),
                Mth.wrapDegrees(desiredYaw - currentYaw).toFloat())
            val rotationStep = rotationChange.normalized()
                .scale(lookIntensity.toFloat() * 10F * (Mth.clamp(rotationChange.length(), 0F, 10F) / 10f))

            player.xRot = player.getViewXRot(tickDelta) + rotationStep.x
            player.yRot = player.getViewYRot(tickDelta) + rotationStep.y
        }

        RENDERED_GLOW_SQUIDS.clear()
    }

}