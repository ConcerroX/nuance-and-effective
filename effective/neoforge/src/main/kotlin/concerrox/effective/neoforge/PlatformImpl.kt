@file:JvmName("PlatformImpl")

package concerrox.effective.neoforge

import net.neoforged.fml.ModList

private val isSatinInstalledOnForge by lazy {
    ModList.get().isLoaded("satin")
}

fun isSatinInstalled(): Boolean {
    return isSatinInstalledOnForge
}
