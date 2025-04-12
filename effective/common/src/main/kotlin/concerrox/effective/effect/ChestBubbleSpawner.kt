package concerrox.effective.effect

import concerrox.effective.registry.ModParticles
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AbstractChestBlock
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ChestBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.LidBlockEntity
import net.minecraft.world.level.block.state.properties.ChestType


object ChestBubbleSpawner {

    fun doUnderwaterChestLogic(level: Level, blockEntity: BlockEntity) {
        if (blockEntity is LidBlockEntity) {
            val blockState = blockEntity.blockState
            val chestType = if (blockState.hasProperty(ChestBlock.TYPE)) blockState.getValue(
                ChestBlock.TYPE) else ChestType.SINGLE
            val facing = if (blockState.hasProperty(ChestBlock.FACING)) blockState.getValue(
                ChestBlock.FACING) else Direction.NORTH
            val block = blockState.block
            if (block is AbstractChestBlock<*> && level.isWaterAt(blockEntity.blockPos) && level.isWaterAt(
                    blockEntity.blockPos.relative(Direction.UP, 1))) {
                val doubleChest = chestType != ChestType.SINGLE
                val propertySource = block.combine(blockState, level, blockEntity.blockPos, true)

                val openFactor = propertySource.apply(ChestBlock.opennessCombiner(blockEntity)).get(1.0F)
                if (openFactor > 0) {
                    if (doubleChest) {
                        if (chestType == ChestType.LEFT) {
                            var xOffset = 0.0
                            var zOffset = 0.0
                            var xOffsetRand = 0.0
                            var zOffsetRand = 0.0
                            if (facing === Direction.NORTH) {
                                xOffset = 1.0
                                zOffset = 0.5
                                xOffsetRand = (level.random.nextDouble() - level.random.nextDouble()) * 0.8
                                zOffsetRand = (level.random.nextDouble() - level.random.nextDouble()) * 0.3
                            } else if (facing === Direction.SOUTH) {
                                xOffset = 0.0
                                zOffset = 0.5
                                xOffsetRand = (level.random.nextDouble() - level.random.nextDouble()) * 0.8
                                zOffsetRand = (level.random.nextDouble() - level.random.nextDouble()) * 0.3
                            } else if (facing === Direction.EAST) {
                                xOffset = 0.5
                                zOffset = 1.0
                                xOffsetRand = (level.random.nextDouble() - level.random.nextDouble()) * 0.3
                                zOffsetRand = (level.random.nextDouble() - level.random.nextDouble()) * 0.8
                            } else if (facing === Direction.WEST) {
                                xOffset = 0.5
                                zOffset = 0.0
                                xOffsetRand = (level.random.nextDouble() - level.random.nextDouble()) * 0.3
                                zOffsetRand = (level.random.nextDouble() - level.random.nextDouble()) * 0.8
                            }

                            for (i in 0 until 1 + level.random.nextInt(3)) {
                                spawnBubble(level, blockEntity.blockPos.x + xOffset + xOffsetRand,
                                    blockEntity.blockPos.y + 0.5, blockEntity.blockPos.z + zOffset + zOffsetRand,
                                    block === Blocks.ENDER_CHEST)
                            }

                            if (openFactor <= .6f) {
                                spawnClosingBubble(level, blockEntity.blockPos.x + xOffset,
                                    blockEntity.blockPos.y + 0.5, blockEntity.blockPos.z + zOffset, facing, true,
                                    block === Blocks.ENDER_CHEST)
                            }
                        }
                    } else {
                        for (i in 0 until 1 + level.random.nextInt(3)) {
                            spawnBubble(level,
                                blockEntity.blockPos.x + 0.5 + (level.random.nextDouble() - level.random.nextDouble()) * 0.3,
                                blockEntity.blockPos.y + 0.5,
                                blockEntity.blockPos.z + 0.5 + (level.random.nextDouble() - level.random.nextDouble()) * 0.3,
                                block === Blocks.ENDER_CHEST)
                        }

                        if (openFactor <= .6f) {
                            spawnClosingBubble(level, blockEntity.blockPos.x + 0.5, blockEntity.blockPos.y + 0.5,
                                blockEntity.blockPos.z + 0.5, facing, false, block === Blocks.ENDER_CHEST)
                        }
                    }
                }
            }
        }
    }

    private fun spawnBubble(level: Level, x: Double, y: Double, z: Double, endChest: Boolean) {
        level.addParticle(if (endChest) ModParticles.END_BUBBLE else ModParticles.BUBBLE, x, y, z, 0.0,
            0.05 + level.random.nextDouble() * 0.05, 0.0)
    }

    private fun spawnClosingBubble(
        level: Level, x: Double, y: Double, z: Double, direction: Direction, doubleChest: Boolean, endChest: Boolean
    ) {
        for (i in 0 until (if (doubleChest) 10 else 5)) {
            var velX = 0.5
            var velZ = 0.5
            if (direction == Direction.NORTH) {
                velX = (level.random.nextDouble() - level.random.nextDouble()) / (if (doubleChest) 20.5 else 5.0)
                velZ = -0.05 - (level.random.nextDouble() / 5.0)
            } else if (direction == Direction.SOUTH) {
                velX = (level.random.nextDouble() - level.random.nextDouble()) / (if (doubleChest) 20.5 else 5.0)
                velZ = 0.05 + (level.random.nextDouble() / 5.0)
            } else if (direction == Direction.EAST) {
                velX = 0.05 + (level.random.nextDouble() / 5.0)
                velZ = (level.random.nextDouble() - level.random.nextDouble()) / (if (doubleChest) 20.5 else 5.0)
            } else if (direction == Direction.WEST) {
                velX = -0.05 - (level.random.nextDouble() / 5.0)
                velZ = (level.random.nextDouble() - level.random.nextDouble()) / (if (doubleChest) 20.5 else 5.0)
            }
            level.addParticle(if (endChest) ModParticles.END_BUBBLE else ModParticles.BUBBLE, x, y, z, velX,
                0.1 - (level.random.nextDouble() * 0.1), velZ)
        }
    }


}