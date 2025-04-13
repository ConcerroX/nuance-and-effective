package concerrox.effective.screenshake

import concerrox.effective.EffectiveConfig
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import kotlin.math.min
import kotlin.math.pow

object ScreenShakeManager {

    private val SCREEN_SHAKES = arrayListOf<ScreenShake>()
    private var intensity = 0F
    private var yawOffset = 0F
    private var pitchOffset = 0F

    fun cameraTick(camera: Camera) {
        Minecraft.getInstance().level?.random?.let {
            if (intensity >= 0.1) {
                //TODO: make this perlin noise based rather than just random gibberish
                yawOffset = randomizeOffset(it)
                pitchOffset = randomizeOffset(it)
                camera.setRotation(camera.yRot + yawOffset, camera.xRot + pitchOffset)
            }
        }
    }

    fun tick() {
        val minecraft = Minecraft.getInstance()
        val level = minecraft.level
        if (level != null && !minecraft.isPaused) {
            val camera = minecraft.gameRenderer.mainCamera
            intensity = min(SCREEN_SHAKES.map {
                it.updateIntensity(camera, level.random)
            }.sum(), EffectiveConfig.screenShakeIntensity.get().toFloat()).pow(3)
            SCREEN_SHAKES.removeIf { it.progress >= it.duration }
        }
    }

    fun addScreenshake(instance: ScreenShake) {
        SCREEN_SHAKES.add(instance)
    }

    private fun randomizeOffset(random: RandomSource): Float {
        return Mth.nextFloat(random, -intensity * 2, intensity * 2)
    }
}