package concerrox.effective

import net.minecraft.resources.ResourceLocation

object Effective {

    const val MOD_ID = "effective"

    fun id(path: String): ResourceLocation {
        return ResourceLocation(MOD_ID, path)
    }

    fun onInitialize() {

    }

}