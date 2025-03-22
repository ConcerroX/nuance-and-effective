package concerrox.effective.forge

import concerrox.effective.Effective
import net.minecraftforge.fml.common.Mod

@Mod(Effective.MOD_ID)
object EffectiveForge {

    init {
        Effective.onInitialize()
    }

}