package concerrox.effective.extension

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

internal fun Level.isBlock(x: Double, y: Double, z: Double, block: Block) = isBlock(BlockPos.containing(x, y, z), block)
internal fun Level.isBlock(pos: BlockPos, block: Block) = getBlockState(pos).`is`(block)

internal fun Level.isAir(x: Double, y: Double, z: Double) = isAir(BlockPos.containing(x, y, z))
internal fun Level.isAir(pos: BlockPos) = getBlockState(pos).isAir

internal fun Level.isWater(x: Double, y: Double, z: Double) = isWater(BlockPos.containing(x, y, z))
internal fun Level.isWater(pos: BlockPos) = getBlockState(pos).`is`(Blocks.WATER)