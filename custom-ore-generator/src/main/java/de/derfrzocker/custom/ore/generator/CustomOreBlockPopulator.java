package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.custom.ore.generator.api.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.BlockPopulator;

import java.util.*;

public class CustomOreBlockPopulator extends BlockPopulator implements WorldHandler, Listener {

    public CustomOreBlockPopulator(CustomOreGenerator customOreGenerator) {
        Bukkit.getPluginManager().registerEvents(this, customOreGenerator);
    }

    @Override
    public void populate(World world, Random random, Chunk source) {
        Set<Biome> biomes = getBiomes(source);

        CustomOreGeneratorService service = CustomOreGenerator.getService();

        WorldConfig worldConfig;

        {
            Optional<WorldConfig> optional = service.getWorldConfig(world.getName());

            if (!optional.isPresent())
                return;

            worldConfig = optional.get();
        }

        biomes.forEach(biome -> {
            List<OreConfig> oreConfigs = Arrays.asList(worldConfig.getOreConfigs().stream().filter(oreConfig -> oreConfig.getBiomes().contains(biome) || oreConfig.shouldGeneratedAll()).filter(OreConfig::isActivated).toArray(OreConfig[]::new));

            oreConfigs.forEach(oreConfig -> generate(oreConfig, world, source, biome));
        });

    }

    private Set<Biome> getBiomes(Chunk chunk) {
        Set<Biome> set = new HashSet<>();

        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                set.add(chunk.getBlock(x, 0, z).getBiome());

        return set;
    }

    private void generate(OreConfig oreConfig, World world, Chunk chunk, Biome biome) {
        CustomOreGeneratorService service = CustomOreGenerator.getService();

        Optional<OreGenerator> optional = service.getOreGenerator(oreConfig.getOreGenerator());

        if (!optional.isPresent())
            return;

        OreGenerator oreGenerator = optional.get();

        oreGenerator.generate(oreConfig, world, chunk.getX(), chunk.getZ(), service.createRandom(world.getSeed() + oreConfig.getMaterial().toString().hashCode(), chunk.getX(), chunk.getZ()), biome);
    }


    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (event.getWorld().getPopulators().contains(this))
            return;

        event.getWorld().getPopulators().add(this);
    }

}
