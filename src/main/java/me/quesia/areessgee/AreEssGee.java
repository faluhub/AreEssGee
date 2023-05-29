package me.quesia.areessgee;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AreEssGee {
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("areessgee").orElseThrow(RuntimeException::new);
    public static final String MOD_NAME = MOD_CONTAINER.getMetadata().getName();
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    private static long SEED;
    private static Random RANDOM;
    public static final List<StructureFeature<?>> PLACED_STRUCTURES = new ArrayList<>();
    public static final List<ChunkPos> USED_QUADRANTS = new ArrayList<>();

    public static final int ANTI_BASALT_REGION_SIZE = SpeedrunConfig.getIntValue("ANTI_BASALT_REGION_SIZE", 120);
    public static final String ANTI_BASALT_REPLACEMENT = SpeedrunConfig.getStringValue("ANTI_BASALT_REPLACEMENT", "nether_wastes");
    public static final List<String> AFFECTED_STRUCTURES = SpeedrunConfig.getStringArrayValue("AFFECTED_STRUCTURES", "bastion_remnant", "fortress");
    public static final int AFFECTED_STRUCTURE_RANDOM_OFFSET = SpeedrunConfig.getIntValue("AFFECTED_STRUCTURE_RANDOM_OFFSET", 3);
    public static final int AFFECTED_STRUCTURE_RANDOM_BOUND = SpeedrunConfig.getIntValue("AFFECTED_STRUCTURE_RANDOM_BOUND", 4);
    public static final int MAX_GLOBAL_STRONGHOLD_ROOM_LIMIT = SpeedrunConfig.getIntValue("MAX_GLOBAL_STRONGHOLD_ROOM_LIMIT", 10);
    public static final float FLINT_MINIMUM_VALUE = SpeedrunConfig.getFloatValue("FLINT_MINIMUM_VALUE", 0.6F);
    public static final float ROD_MINIMUM_VALUE = SpeedrunConfig.getFloatValue("ROD_MINIMUM_VALUE", 0.4F);
    public static final float BURIED_TREASURE_RARITY = SpeedrunConfig.getFloatValue("BURIED_TREASURE_RARITY", 0.07F);
    public static final float OCEAN_CANYON_RARITY = SpeedrunConfig.getFloatValue("OCEAN_CANYON_RARITY", 0.04F);
    public static final float TREASURE_BASTION_GOLD_BLOCK_RARITY = SpeedrunConfig.getFloatValue("TREASURE_BASTION_GOLD_BLOCK_RARITY", 0.2F);
    public static final float EYE_ODDS = SpeedrunConfig.getFloatValue("EYE_ODDS", 0.8F);
    public static final boolean GUARANTEE_EYE_DROPS = SpeedrunConfig.getBooleanValue("GUARANTEE_EYE_DROPS", true);
    public static final boolean ADD_BASALT_BASTIONS = SpeedrunConfig.getBooleanValue("ADD_BASALT_BASTIONS", true);

    public static void log(Object msg) {
        LOGGER.log(Level.INFO, msg);
    }

    public static Chunk calculateChunk(Chunk chunk) {
        Chunk chunk1 = calculateChunk(chunk.getPos());
        return chunk1 == null ? chunk : chunk1;
    }

    public static ChunkPos calculateQuadrant(int x, int z) {
        int mx = x < 0 ? x / -x : 1;
        int mz = z < 0 ? z / -z : 1;
        return new ChunkPos(mx, mz);
    }

    public static ChunkPos calculateChunkPos(int x, int z) {
        if (RANDOM == null) { return new ChunkPos(x, z); }
        ChunkPos quadrant = calculateQuadrant(x, z);
        if (USED_QUADRANTS.contains(quadrant)) {
            boolean b = new Random(SEED).nextBoolean();
            quadrant = new ChunkPos(quadrant.x * (b ? 1 : -1), quadrant.z * (b ? -1 : 1));
        }
        int cx = quadrant.x * (RANDOM.nextInt(AFFECTED_STRUCTURE_RANDOM_BOUND) + AFFECTED_STRUCTURE_RANDOM_OFFSET);
        int cz = quadrant.z * (RANDOM.nextInt(AFFECTED_STRUCTURE_RANDOM_BOUND) + AFFECTED_STRUCTURE_RANDOM_OFFSET);
        return new ChunkPos(cx, cz);
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
        PLACED_STRUCTURES.clear();
        USED_QUADRANTS.clear();

        if (SEED != seed) {
            SEED = seed;
            RANDOM = new Random(seed);
        }
    }
}
