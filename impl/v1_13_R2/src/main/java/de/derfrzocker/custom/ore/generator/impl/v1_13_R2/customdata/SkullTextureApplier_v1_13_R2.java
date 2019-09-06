package de.derfrzocker.custom.ore.generator.impl.v1_13_R2.customdata;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.derfrzocker.custom.ore.generator.api.CustomData;
import de.derfrzocker.custom.ore.generator.api.CustomDataApplier;
import de.derfrzocker.custom.ore.generator.api.OreConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.GeneratorAccess;
import net.minecraft.server.v1_13_R2.TileEntity;
import net.minecraft.server.v1_13_R2.TileEntitySkull;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class SkullTextureApplier_v1_13_R2 implements CustomDataApplier {

    @NonNull
    private final CustomData customData;

    @Override
    public void apply(OreConfig oreConfig, Object location, Object blockAccess) {

        final BlockPosition blockPosition = (BlockPosition) location;
        final GeneratorAccess generatorAccess = (GeneratorAccess) blockAccess;
        final TileEntity tileEntity = generatorAccess.getTileEntity(blockPosition);

        generatorAccess.y(blockPosition).d(blockPosition);
        generatorAccess.y(blockPosition).a(blockPosition, tileEntity);

        if (tileEntity == null)
            return; //TODO maybe throw exeption?

        if (!(tileEntity instanceof TileEntitySkull))
            return; //TODO maybe throw exeption?

        final TileEntitySkull skull = (TileEntitySkull) tileEntity;
        final Optional<Object> objectOptional = oreConfig.getCustomData(customData);

        if (!objectOptional.isPresent())
            return; //TODO maybe throw exeption?

        final String texture = (String) objectOptional.get();
        final GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), null);

        gameProfile.getProperties().put("textures", new Property("textures", texture));

        skull.setGameProfile(gameProfile);
    }

}
