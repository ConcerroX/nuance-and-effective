package concerrox.effective.render

import com.mojang.blaze3d.platform.GlStateManager
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

        override fun begin(tesselator: Tesselator, textureManager: TextureManager): BufferBuilder? {
            RenderSystem.enableDepthTest()
            RenderSystem.enableBlend()
            RenderSystem.depthMask(false)
//            RenderSystem.setShader { ModShaders.PARTICLE }
//            RenderSystem.blendFuncSeparate(
//                GlStateManager.SourceFactor.SRC_ALPHA,
//                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
//                GlStateManager.SourceFactor.ONE,
//                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
//            )
            @Suppress("DEPRECATION") RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES)
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE)
        }

        override fun toString() = "ADDITIVE"

    }

    /**
     * Not implemented: it will be replaced by the real soft render type in the future
     */
    val PARTICLE_SHEET_SOFT: ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

}