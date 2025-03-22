package concerrox.effective.fabric

import concerrox.effective.Effective
import net.fabricmc.api.ModInitializer

class EffectiveFabric : ModInitializer {

    override fun onInitialize() {
        Effective.onInitialize()
    }

}