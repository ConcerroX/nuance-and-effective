package concerrox.effective.util

import net.neoforged.neoforge.common.ModConfigSpec

internal fun ModConfigSpec.Builder.category(path: String, action: ModConfigSpec.Builder.() -> Unit) {
    push(path)
    action(this)
    pop()
}

internal fun ModConfigSpec(action: ModConfigSpec.Builder.() -> Unit): ModConfigSpec {
    val builder = ModConfigSpec.Builder()
    action(builder)
    return builder.build()
}