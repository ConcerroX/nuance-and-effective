package concerrox.effective.screenshake

import net.minecraft.client.Camera
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource

open class ScreenShake(val duration: Int) {
    var progress = 0
    private var intensity1 = 0f
    private var intensity2 = 0f
    private var intensity3 = 0f
    private var intensityCurveStartEasing = Easing.LINEAR
    private var intensityCurveEndEasing = Easing.LINEAR

    fun setIntensity(intensity1: Float, intensity2: Float, intensity3: Float): ScreenShake {
        this.intensity1 = intensity1
        this.intensity2 = intensity2
        this.intensity3 = intensity3
        return this
    }

    open fun updateIntensity(camera: Camera, random: RandomSource): Float {
        progress++
        val percentage = progress / duration.toFloat()
        return if (intensity2 != intensity3) {
            if (percentage >= 0.5f) {
                Mth.lerp(intensityCurveEndEasing.ease(percentage - 0.5f, 0f, 1f, 0.5f), intensity2, intensity1)
            } else {
                Mth.lerp(intensityCurveStartEasing.ease(percentage, 0f, 1f, 0.5f), intensity1, intensity2)
            }
        } else {
            Mth.lerp(intensityCurveStartEasing.ease(percentage, 0f, 1f, 1f), intensity1, intensity2)
        }
    }
}