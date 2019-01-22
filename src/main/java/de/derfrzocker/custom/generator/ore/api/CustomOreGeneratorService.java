package de.derfrzocker.custom.generator.ore.api;


import org.bukkit.World;

import java.util.Optional;
import java.util.Set;

public interface CustomOreGeneratorService {

    Optional<OreGenerator> getOreGenerator(String name);

    void registerOreGenerator(OreGenerator oreGenerator);

    Optional<WorldConfig> getWorldConfig(String world);

    WorldConfig createWorldConfig(World world);

    void saveWorldConfig(WorldConfig config);

    Set<WorldConfig> getWorldConfigs();

    void setDefaultOreGenerator(OreGenerator oreGenerator);

    OreGenerator getDefaultOreGenerator();

}
