@file:JvmName("LevelUtils")

package concerrox.effective.util

import net.minecraft.client.gui.font.providers.UnihexProvider.Dimensions
import net.minecraft.core.BlockPos
import net.minecraft.data.worldgen.DimensionTypes
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.dimension.DimensionDefaults
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.material.Fluids


val Level.isNightTime
    get() = getSunAngle(dayTime.toFloat()) >= 0.25965086 && getTimeOfDay(dayTime.toFloat()) <= 0.7403491

internal fun Level.getBlockState(x: Double, y: Double, z: Double) = getBlockState(BlockPos.containing(x, y, z))

internal fun Level.isBlock(x: Double, y: Double, z: Double, block: Block) = isBlock(BlockPos.containing(x, y, z), block)
internal fun Level.isBlock(pos: BlockPos, block: Block) = getBlockState(pos).`is`(block)

internal fun Level.isInBlockTag(x: Double, y: Double, z: Double, block: TagKey<Block>) =
    isInBlockTag(BlockPos.containing(x, y, z), block)

internal fun Level.isInBlockTag(pos: BlockPos, block: TagKey<Block>) = getBlockState(pos).`is`(block)

internal fun Level.isAir(x: Double, y: Double, z: Double) = isAir(BlockPos.containing(x, y, z))
internal fun Level.isAir(pos: BlockPos) = getBlockState(pos).isAir

internal fun Level.isWater(x: Double, y: Double, z: Double) = isWater(BlockPos.containing(x, y, z))
internal fun Level.isWater(pos: BlockPos) = getBlockState(pos).`is`(Blocks.WATER)

internal fun Level.isWaterFluid(x: Double, y: Double, z: Double) = isWaterFluid(BlockPos.containing(x, y, z))
internal fun Level.isWaterFluid(pos: BlockPos) = getFluidState(pos).type == Fluids.WATER

fun Level.isInOverworld(pos: BlockPos) = dimension() == Level.OVERWORLD

// method to check if the player has a stone material type block above them, more reliable to
// detect caves compared to isSkyVisible (okay nvm they removed materials we're using pickaxe mine-able instead lmao oh god this is
// going to be so unreliable)
private fun hasStoneAbove(level: Level, pos: BlockPos): Boolean {
    val mutable = pos.mutable()
    val startY = mutable.y
    for (y in startY..startY + 100) {
        mutable.setY(y)
        val state = level.getBlockState(mutable)
        if (state.isRedstoneConductor(level, pos) && state.`is`(BlockTags.MINEABLE_WITH_PICKAXE)) {
            return true
        }
    }
    return false
}

fun Level.isInCave(pos: BlockPos) = pos.y < seaLevel && hasStoneAbove(this, pos)
