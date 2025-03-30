package concerrox.effective.forge.shader

import com.google.gson.JsonElement
import com.mojang.blaze3d.shaders.Uniform
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.ChainedJsonException
import net.minecraft.server.packs.resources.ResourceProvider
import net.minecraft.util.GsonHelper
import java.util.function.Consumer

abstract class ExtendedShaderInstance(
    resourceProvider: ResourceProvider, location: ResourceLocation, vertexFormat: VertexFormat
) : ShaderInstance(resourceProvider, location, vertexFormat) {
    private var defaultUniformData: MutableMap<String?, Consumer<Uniform?>>? = null

    abstract val shaderHolder: ShaderHolder

    private fun getDefaultUniformData(): MutableMap<String?, Consumer<Uniform?>> {
        if (defaultUniformData == null) {
            defaultUniformData = HashMap()
        }
        return defaultUniformData as MutableMap<String?, Consumer<Uniform?>>
    }

    @Throws(ChainedJsonException::class)
    override fun parseUniformNode(pJson: JsonElement) {
        super.parseUniformNode(pJson)
        val jsonObject = GsonHelper.convertToJsonObject(pJson, "uniform")
        val uniformName = GsonHelper.getAsString(jsonObject, "name")
        if (shaderHolder.uniformsToCache.contains(uniformName)) {
            val uniform = uniforms[uniforms.size - 1]

            val consumer: Consumer<Uniform?>
            if (uniform.type <= 3) {
                val buffer = uniform.intBuffer
                buffer.position(0)
                val array = IntArray(uniform.count)
                for (i in 0 until uniform.count) {
                    array[i] = buffer[i]
                }
                consumer = Consumer {
                    buffer.position(0)
                    buffer.put(array)
                }
            } else {
                val buffer = uniform.floatBuffer
                buffer.position(0)
                val array = FloatArray(uniform.count)
                for (i in 0 until uniform.count) {
                    array[i] = buffer[i]
                }
                consumer = Consumer {
                    buffer.position(0)
                    buffer.put(array)
                }
            }

            getDefaultUniformData()[uniformName] = consumer
        }
    }
}