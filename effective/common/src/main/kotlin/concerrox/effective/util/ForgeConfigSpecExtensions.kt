package concerrox.effective.util

import net.minecraftforge.common.ForgeConfigSpec

internal fun ForgeConfigSpec.Builder.category(path: String, action: ForgeConfigSpec.Builder.() -> Unit) {
    push(path)
    action(this)
    pop()
}

internal fun ForgeConfigSpec(action: ForgeConfigSpec.Builder.() -> Unit): ForgeConfigSpec {
    val builder = ForgeConfigSpec.Builder()
    action(builder)
    return builder.build()
}