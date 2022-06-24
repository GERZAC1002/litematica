package fi.dy.masa.litematica.world;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.Reference;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.render.LitematicaRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import javax.annotation.Nullable;
import java.util.OptionalLong;

public class SchematicWorldHandler
{
    @Nullable private static WorldSchematic world;
    public static final DimensionType DIMENSIONTYPE = new DimensionType(OptionalLong.of(6000L), false, false, false, false, 1.0,
                                                                           false, false, -64, 384, 384,
                                                                            BlockTags.INFINIBURN_END, DimensionTypes.OVERWORLD_ID, 0.0F,
            new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 15), 1));

    @Nullable
    public static WorldSchematic getSchematicWorld()
    {
        return world;
    }

    public static WorldSchematic createSchematicWorld()
    {
        ClientWorld.Properties levelInfo = new ClientWorld.Properties(Difficulty.PEACEFUL, false, true);

        RegistryEntry<DimensionType> entry = BuiltinRegistries.DIMENSION_TYPE.getEntry(RegistryKey.of(BuiltinRegistries.DIMENSION_TYPE.getKey(), new Identifier(Reference.MOD_ID)))
                .orElseThrow();

        //RegistryEntry<DimensionType> entry = RegistryEntry.of(BuiltinRegistries.DIMENSION_TYPE.get(new Identifier(Reference.MOD_ID)));
        return new WorldSchematic(levelInfo, entry, MinecraftClient.getInstance()::getProfiler);
    }

    public static void recreateSchematicWorld(boolean remove)
    {
        if (remove)
        {
            if (Configs.Generic.DEBUG_LOGGING.getBooleanValue())
            {
                Litematica.logger.info("Removing the schematic world...");
            }

            world = null;
        }
        else
        {
            if (Configs.Generic.DEBUG_LOGGING.getBooleanValue())
            {
                Litematica.logger.info("(Re-)creating the schematic world...");
            }

            // Note: The dimension used here must have no skylight, because the custom Chunks don't have those arrays
            world = createSchematicWorld();

            if (Configs.Generic.DEBUG_LOGGING.getBooleanValue())
            {
                Litematica.logger.info("Schematic world created: {}", world);
            }
        }

        LitematicaRenderer.getInstance().onSchematicWorldChanged(world);
    }
}
