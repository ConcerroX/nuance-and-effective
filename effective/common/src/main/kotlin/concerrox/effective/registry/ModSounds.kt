package concerrox.effective.registry

import concerrox.effective.Effective
import net.minecraft.sounds.SoundEvent

object ModSounds {
    val SOUNDS = mutableListOf<SoundEvent>()

    val ENTITY_PARRY by register("entity.parry")
    val AMBIENT_CASCADE by register("ambient.cascade")
    val AMBIENT_ANIMAL_BEES by register("ambient.animal.bees")
    val AMBIENT_ANIMAL_BIRDS by register("ambient.animal.birds")
    val AMBIENT_ANIMAL_CICADAS by register("ambient.animal.cicadas")
    val AMBIENT_ANIMAL_CRICKETS by register("ambient.animal.crickets")
    val AMBIENT_ANIMAL_FROGS_AND_CRICKETS by register("ambient.animal.frogs_and_crickets")
    val AMBIENT_ANIMAL_JUNGLE_DAY by register("ambient.animal.jungle_day")
    val AMBIENT_ANIMAL_JUNGLE_NIGHT by register("ambient.animal.jungle_night")
    val AMBIENT_ANIMAL_MANGROVE_BIRDS by register("ambient.animal.mangrove_birds")
    val AMBIENT_ANIMAL_OWLS by register("ambient.animal.owls")
    val AMBIENT_FOLIAGE_CAVE_LEAVES by register("ambient.foliage.cave_leaves")
    val AMBIENT_FOLIAGE_LEAVES by register("ambient.foliage.leaves")
    val AMBIENT_WATER_DRIPSTONE_CAVES by register("ambient.water.dripstone_caves")
    val AMBIENT_WATER_LUSH_CAVES by register("ambient.water.lush_caves")
    val AMBIENT_WATER_RIVER by register("ambient.water.river")
    val AMBIENT_WATER_WAVES by register("ambient.water.waves")
    val AMBIENT_WIND_ARID by register("ambient.wind.arid")
    val AMBIENT_WIND_CAVE by register("ambient.wind.cave")
    val AMBIENT_WIND_COLD by register("ambient.wind.cold")
    val AMBIENT_WIND_DEEP_DARK by register("ambient.wind.deep_dark")
    val AMBIENT_WIND_END by register("ambient.wind.end")
    val AMBIENT_WIND_MOUNTAINS by register("ambient.wind.mountains")
    val AMBIENT_WIND_TEMPERATE by register("ambient.wind.temperate")

    private fun register(id: String): Lazy<SoundEvent> {
        val sound = SoundEvent.createVariableRangeEvent(Effective.id(id))
        val lazyType = lazy { sound }
        SOUNDS += sound
        return lazyType
    }
}
