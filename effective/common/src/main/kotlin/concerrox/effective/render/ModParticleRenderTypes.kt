package concerrox.effective.render

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.renderer.texture.TextureManager


object ModParticleRenderTypes {

    val ADDITIVE = object : ParticleRenderType {
        override fun begin(bufferBuilder: BufferBuilder, textureManager: TextureManager) {
            RenderSystem.enableDepthTest()
            RenderSystem.enableBlend()
            RenderSystem.depthMask(true)
            RenderSystem.setShader { ModShaders.PARTICLE }
            @Suppress("DEPRECATION") RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES)
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE)
        }

        override fun end(tesselator: Tesselator) {
            tesselator.end()
            RenderSystem.disableBlend()
            RenderSystem.defaultBlendFunc()
            RenderSystem.enableDepthTest()
        }

        override fun toString() = "ADDITIVE"
    }

    /**
     * Not implemented: it will be replaced by the real soft render type in the future
     */
    val PARTICLE_SHEET_SOFT: ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

}