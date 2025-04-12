@file:JvmName("Platform")

package concerrox.effective

import dev.architectury.injectables.annotations.ExpectPlatform

@ExpectPlatform
fun isSatinInstalled(): Boolean {
    throw NotImplementedError()
}