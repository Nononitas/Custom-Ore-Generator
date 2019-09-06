package de.derfrzocker.custom.ore.generator.impl.v_1_9_R2;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.api.OreGenerator;
import de.derfrzocker.custom.ore.generator.api.OreSetting;
import lombok.Getter;
import net.minecraft.server.v1_9_R2.*;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_9_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.util.CraftMagicNumbers;

import java.util.Random;
import java.util.Set;

public class MinableGenerator_v1_9_R2 implements OreGenerator {

    public final Predicate<IBlockData> blocks = (value) -> {
        if (value == null) {
            return false;
        } else {
            Block block = value.getBlock();
            return block == Blocks.STONE || block == Blocks.END_STONE || block == Blocks.NETHERRACK;
        }
    };

    @Getter
    private final Set<OreSetting> neededOreSettings = Sets.newHashSet(OreSetting.VEIN_SIZE, OreSetting.VEINS_PER_CHUNK, OreSetting.HEIGHT_RANGE, OreSetting.MINIMUM_HEIGHT);

    @Getter
    private final String name = "vanilla_minable_generator";

    @Override
    public void generate(OreConfig config, World world, int x2, int z2, Random random, Biome biome) {
        final int veinSize = config.getValue(OreSetting.VEIN_SIZE).orElse(OreSetting.VEIN_SIZE.getSaveValue());
        final int veinsPerChunk = config.getValue(OreSetting.VEINS_PER_CHUNK).orElse(OreSetting.VEINS_PER_CHUNK.getSaveValue());
        final int heightRange = config.getValue(OreSetting.HEIGHT_RANGE).orElse(OreSetting.HEIGHT_RANGE.getSaveValue());
        final int minimumHeight = config.getValue(OreSetting.MINIMUM_HEIGHT).orElse(OreSetting.MINIMUM_HEIGHT.getSaveValue());

        final CraftWorld craftWorld = (CraftWorld) world;
        final CraftChunk craftChunk = (CraftChunk) world.getChunkAt(x2, z2);

        final IBlockData blockData = CraftMagicNumbers.getBlock(config.getMaterial()).getBlockData();

        WorldGenMinable generator = new WorldGenMinable(blockData, veinSize, blocks);

        for (int trys = 0; trys < veinsPerChunk; ++trys) {
            int x = random.nextInt(16);
            int y = random.nextInt(heightRange) + minimumHeight;
            int z = random.nextInt(16);

            if (biome == null || craftChunk.getBlock(x, y, z).getBiome() == biome) {
                craftWorld.getHandle().captureTreeGeneration = true;
                craftWorld.getHandle().captureBlockStates = true;

                generator.generate(craftWorld.getHandle(), random, new BlockPosition(x + (x2 << 4), y, z + (z2 << 4)));

                craftWorld.getHandle().captureTreeGeneration = false;
                craftWorld.getHandle().captureBlockStates = false;

                for (org.bukkit.block.BlockState blockState : craftWorld.getHandle().capturedBlockStates) {
                    if (craftWorld.getHandle().setTypeAndData(new BlockPosition(blockState.getX(), blockState.getY(), blockState.getZ()), blockData, 2)) {
                        config.getCustomData().forEach((customData, object) -> customData.getCustomDataApplier().apply(config, new BlockPosition(blockState.getLocation().getBlockX(), blockState.getLocation().getBlockY(), blockState.getLocation().getBlockZ()), craftWorld.getHandle()));
                    }
                }

                craftWorld.getHandle().capturedBlockStates.clear();
            }
        }
    }
}

