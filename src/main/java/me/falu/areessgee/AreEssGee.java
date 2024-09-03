package me.falu.areessgee;

import com.google.common.collect.Lists;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AreEssGee {
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("areessgee").orElseThrow(RuntimeException::new);
    public static final String MOD_NAME = MOD_CONTAINER.getMetadata().getName();
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final List<StructureFeature<?>> PLACED_STRUCTURES = new ArrayList<>();
    public static final List<ChunkPos> USED_QUADRANTS = new ArrayList<>();
    public static final List<String> AFFECTED_STRUCTURES = Lists.newArrayList("bastion_remnant", "fortress");
    public static AreEssGeeConfig CONFIG;
    private static long SEED;
    private static Random RANDOM;

    public static void log(Object msg) {
        LOGGER.log(Level.INFO, msg);
    }

    public static ChunkPos calculateQuadrant(int x, int z) {
        return new ChunkPos(x < 0 ? -1 : 1, z < 0 ? -1 : 1);
    }

    public static ChunkPos calculateChunkPos(int x, int z) {
        if (RANDOM == null) {
            return new ChunkPos(x, z);
        }
        ChunkPos quadrant = calculateQuadrant(x, z);
        while (USED_QUADRANTS.contains(quadrant)) {
            quadrant = new ChunkPos(quadrant.x * (RANDOM.nextBoolean() ? 1 : -1), quadrant.z * (RANDOM.nextBoolean() ? -1 : 1));
        }
        int cx = quadrant.x * (RANDOM.nextInt(CONFIG.randomBound) + CONFIG.randomOffset);
        int cz = quadrant.z * (RANDOM.nextInt(CONFIG.randomBound) + CONFIG.randomOffset);
        return new ChunkPos(cx, cz);
    }

    public static Chunk calculateChunk(Chunk chunk) {
        Chunk chunk1 = calculateChunk(chunk.getPos());
        return chunk1 == null ? chunk : chunk1;
    }

    public static Chunk calculateChunk(ChunkPos chunkPos) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getServer() != null && client.player != null) {
            ServerWorld world = client.getServer().getWorld(client.player.world.getRegistryKey());
            if (world != null) {
                ChunkPos chunkPos1 = calculateChunkPos(chunkPos.x, chunkPos.z);
                return world.getChunkManager().getChunk(chunkPos1.x, chunkPos1.z, ChunkStatus.EMPTY, true);
            }
        }
        return null;
    }

    public static void onWorldCreate(long seed) {
        StructurePoolBasedGenerator.init();
        ((IStructurePoolRegistry) StructurePoolBasedGenerator.REGISTRY).areessgee$resetTreasureData();

        Biome basalt = Biomes.BASALT_DELTAS;
        ((IBiome) basalt).areessgee$removeStructureFeature(DefaultBiomeFeatures.BASTION_REMNANT);
        if (CONFIG.basaltBastions) {
            basalt.addStructureFeature(DefaultBiomeFeatures.BASTION_REMNANT);
        }

        PLACED_STRUCTURES.clear();
        USED_QUADRANTS.clear();

        if (SEED != seed) {
            SEED = seed;
            RANDOM = new Random(seed);
        }
    }
}
