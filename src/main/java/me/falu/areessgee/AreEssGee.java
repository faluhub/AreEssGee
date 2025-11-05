package me.falu.areessgee;

import com.google.common.collect.Lists;
import me.falu.areessgee.owner.StructureFeatureOwner;
import me.falu.areessgee.owner.TreasureDataOwner;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AreEssGee {
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("areessgee").orElseThrow(RuntimeException::new);
    public static final String MOD_NAME = MOD_CONTAINER.getMetadata().getName();
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final List<String> PLACED_STRUCTURES = new ArrayList<>();
    public static final List<ChunkPos> USED_QUADRANTS = new ArrayList<>();
    public static final List<String> AFFECTED_STRUCTURES = Lists.newArrayList("bastion_remnant", "fortress");
    public static AreEssGeeConfig CONFIG;

    public static void log(Object msg) {
        LOGGER.log(Level.INFO, msg);
    }

    public static void debug(Object msg) {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
            return;
        }

        log(msg);
    }

    public static ChunkPos calculateQuadrant(int x, int z) {
        return new ChunkPos(x < 0 ? -1 : 1, z < 0 ? -1 : 1);
    }

    public static ChunkPos calculateChunkPos(int x, int z, ChunkRandom random) {
        ChunkPos quadrant = calculateQuadrant(x, z);
        while (USED_QUADRANTS.contains(quadrant)) {
            quadrant = new ChunkPos(quadrant.x * (random.nextBoolean() ? 1 : -1), quadrant.z * (random.nextBoolean() ? -1 : 1));
        }

        int cx = quadrant.x * (random.nextInt(CONFIG.randomBound) + CONFIG.randomOffset);
        int cz = quadrant.z * (random.nextInt(CONFIG.randomBound) + CONFIG.randomOffset);
        return new ChunkPos(cx, cz);
    }

    public static void onWorldCreate() {
        StructurePoolBasedGenerator.init();
        ((TreasureDataOwner) StructurePoolBasedGenerator.REGISTRY).areessgee$resetTreasureData();

        Biome basalt = Biomes.BASALT_DELTAS;
        ((StructureFeatureOwner) basalt).areessgee$removeStructureFeature(DefaultBiomeFeatures.BASTION_REMNANT);
        if (CONFIG.basaltBastions) {
            basalt.addStructureFeature(DefaultBiomeFeatures.BASTION_REMNANT);
        }

        PLACED_STRUCTURES.clear();
        USED_QUADRANTS.clear();
    }
}
