package concerrox.effective.waterfall

import net.minecraft.world.level.Level

object CascadeManager {
    fun spawnCascadeCloud(level: Level, waterfall: Any) {
//        val blockPos = waterfall.blockPos()
//        for (int i = 0; i < EffectiveConfig.cascadeCloudDensity; i++) {
//            double offsetX = world.getRandom().nextGaussian() / 5f;
//            double offsetZ = world.getRandom().nextGaussian() / 5f;
//            world.addParticle(EffectiveUtils.isGlowingWater(world, waterfall.blockPos()) ? EffectiveParticles.GLOW_CASCADE : EffectiveParticles.CASCADE, blockPos.getX() + .5 + offsetX, blockPos.getY() + world.getRandom().nextFloat(), blockPos.getZ() + .5 + offsetZ, (world.getRandom().nextFloat() * waterfall.strength()) / 10f * Math.signum(offsetX), (world.getRandom().nextFloat() * waterfall.strength()) / 10f, (world.getRandom().nextFloat() * waterfall.strength()) / 10f * Math.signum(offsetZ));
//        }
//        if (EffectiveConfig.cascadeMistDensity > 0f && waterfall.strength() >= 1.6f) {
//            if ((world.random.nextFloat() * 100f) <= EffectiveConfig.cascadeMistDensity) {
//                double offsetX = world.getRandom().nextGaussian() / 5f;
//                double offsetZ = world.getRandom().nextGaussian() / 5f;
//                world.addParticle(EffectiveParticles.MIST, blockPos.getX() + .5f, blockPos.getY() + .5f, blockPos.getZ() + .5f, EffectiveUtils.getRandomFloatOrNegative(world.random) / 15f, EffectiveUtils.getRandomFloatOrNegative(world.random) / 30f, EffectiveUtils.getRandomFloatOrNegative(world.random) / 15f);
//            }
//        }
    }
}