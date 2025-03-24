package concerrox.effective

import concerrox.effective.registry.ModParticles
import net.minecraft.resources.ResourceLocation


object Effective {

    const val MOD_ID = "effective"

    fun id(path: String): ResourceLocation {
        return ResourceLocation(MOD_ID, path)
    }

    fun onInitialize() {
        ModParticles.initialize()
//        Minecraft.getInstance().level?.addParticle(
//            ModParticles.CASCADE.type.invoke(),
//        )
    }

}