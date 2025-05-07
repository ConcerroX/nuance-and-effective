package concerrox.effective.neoforge.shader

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceProvider

class ShaderHolder(
    val shaderLocation: ResourceLocation,
    val shaderFormat: VertexFormat,
    vararg uniformsToCache: String?,
) {
    private var shaderInstance: ExtendedShaderInstance? = null
    var uniformsToCache: Collection<String?> = ArrayList(listOf(*uniformsToCache))

    fun createInstance(resourceManager: ResourceProvider): ExtendedShaderInstance {
        val shaderHolder = this
        val shaderInstance =
            object : ExtendedShaderInstance(resourceManager, shaderLocation, shaderFormat) {
                override val shaderHolder: ShaderHolder
                    get() = shaderHolder
            }
        this.shaderInstance = shaderInstance
        return shaderInstance
    }
}
