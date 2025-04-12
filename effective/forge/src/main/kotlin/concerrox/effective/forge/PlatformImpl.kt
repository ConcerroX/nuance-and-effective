@file:JvmName("PlatformImpl")

package concerrox.effective.forge

import net.minecraftforge.fml.ModList

private val isSatinInstalledOnForge by lazy {
    ModList.get().isLoaded("satin")
}

fun isSatinInstalled(): Boolean {
    return isSatinInstalledOnForge
}
