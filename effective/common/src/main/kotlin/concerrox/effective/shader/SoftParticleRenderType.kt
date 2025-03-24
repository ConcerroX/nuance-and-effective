package concerrox.effective.shader

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.logging.LogUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.renderer.texture.TextureManager
import java.util.*

class SoftParticleRenderType : ParticleRenderType {

    override fun begin(builder: BufferBuilder, textureManager: TextureManager) {
        val minecraft = Minecraft.getInstance()
        RenderSystem.setShader { softParticle }
        // Disallow soft particles from writing to the depth buffer
        RenderSystem.depthMask(false)
        // Set `Sampler0` to the particle atlas
        // noinspection deprecation
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        // Set the sampler for the depth texture
        softParticle?.setSampler("DiffuseDepthSampler", minecraft.mainRenderTarget.depthTextureId)
        return builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE)
    }

    override fun end(tesselator: Tesselator) {
        tesselator.end()
    }

    companion object {
        internal val SOFT_PARTICLE = SoftParticleRenderType()
        private var softParticle: ShaderInstance? = null

        fun loadShader(shader: ShaderInstance) {
            softParticle = Objects.requireNonNull(shader)
        }
    }
}