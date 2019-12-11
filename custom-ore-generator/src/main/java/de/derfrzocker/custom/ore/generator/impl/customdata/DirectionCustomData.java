package de.derfrzocker.custom.ore.generator.impl.customdata;

import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.CustomDataType;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import de.derfrzocker.custom.ore.generator.impl.v1_10_R1.customdata.DirectionApplier_v1_10_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_11_R1.customdata.DirectionApplier_v1_11_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_12_R1.customdata.DirectionApplier_v1_12_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R1.customdata.DirectionApplier_v1_13_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata.DirectionApplier_v1_13_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_14_R1.customdata.DirectionApplier_v1_14_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_15_R1.customdata.DirectionApplier_v1_15_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R1.customdata.DirectionApplier_v1_18_R1;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R2.customdata.DirectionApplier_v1_18_R2;
import de.derfrzocker.custom.ore.generator.impl.v1_8_R3.customdata.DirectionApplier_v1_18_R3;
import de.derfrzocker.custom.ore.generator.impl.v1_9_R1.customdata.DirectionApplier_v1_19_R1;
import de.derfrzocker.custom.ore.generator.impl.v_1_9_R2.customdata.DirectionApplier_v1_19_R2;
import de.derfrzocker.spigot.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DirectionCustomData implements CustomData {

    public static final DirectionCustomData DOWN = new DirectionCustomData("DOWN", BlockFace.DOWN);
    public static final DirectionCustomData UP = new DirectionCustomData("UP", BlockFace.UP);
    public static final DirectionCustomData NORTH = new DirectionCustomData("NORTH", BlockFace.NORTH);
    public static final DirectionCustomData SOUTH = new DirectionCustomData("SOUTH", BlockFace.SOUTH);
    public static final DirectionCustomData WEST = new DirectionCustomData("WEST", BlockFace.WEST);
    public static final DirectionCustomData EAST = new DirectionCustomData("EAST", BlockFace.EAST);

    @NotNull
    private final String name;
    @NotNull
    private final BlockFace blockFace;
    @Nullable
    private CustomDataApplier customDataApplier;

    private DirectionCustomData(@NotNull final String name, @NotNull final BlockFace blockFace) {
        this.name = name;
        this.blockFace = blockFace;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public CustomDataType getCustomDataType() {
        return CustomDataType.BOOLEAN;
    }

    @Override
    public boolean canApply(@NotNull final OreConfig oreConfig) {
        final BlockData blockData = Bukkit.createBlockData(oreConfig.getMaterial());

        if (!(blockData instanceof MultipleFacing))
            return false;

        return ((MultipleFacing) blockData).getAllowedFaces().contains(blockFace);
    }

    @Override
    public boolean isValidCustomData(@NotNull final Object customData, @NotNull final OreConfig oreConfig) {
        return customData instanceof Boolean;
    }

    @NotNull
    @Override
    public CustomDataApplier getCustomDataApplier() {
        if (customDataApplier == null)
            customDataApplier = getCustomDataApplier0();

        return customDataApplier;
    }

    private CustomDataApplier getCustomDataApplier0() {
        switch (Version.getCurrent()) {
            case v1_15_R1:
                return new DirectionApplier_v1_15_R1(this, blockFace);
            case v1_14_R1:
                return new DirectionApplier_v1_14_R1(this, blockFace);
            case v1_13_R2:
                return new DirectionApplier_v1_13_R2(this, blockFace);
            case v1_13_R1:
                return new DirectionApplier_v1_13_R1(this, blockFace);
            case v1_12_R1:
                return new DirectionApplier_v1_12_R1(this, blockFace);
            case v1_11_R1:
                return new DirectionApplier_v1_11_R1(this, blockFace);
            case v1_10_R1:
                return new DirectionApplier_v1_10_R1(this, blockFace);
            case v1_9_R2:
                return new DirectionApplier_v1_19_R2(this, blockFace);
            case v1_9_R1:
                return new DirectionApplier_v1_19_R1(this, blockFace);
            case v1_8_R3:
                return new DirectionApplier_v1_18_R3(this, blockFace);
            case v1_8_R2:
                return new DirectionApplier_v1_18_R2(this, blockFace);
            case v1_8_R1:
                return new DirectionApplier_v1_18_R1(this, blockFace);
        }

        throw new UnsupportedOperationException("Version not supported jet!");
    }

}
