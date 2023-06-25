package me.quesia.areessgee;

import me.quesia.areessgee.config.ConfigValue;
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

    public static final ConfigValue<List<String>> AFFECTED_STRUCTURES = new ConfigValue<>("AFFECTED_STRUCTURES", List.of("bastion_remnant", "fortress"));
    public static final ConfigValue<String> ANTI_BASALT_REPLACEMENT = new ConfigValue<>("ANTI_BASALT_REPLACEMENT", "nether_wastes");
    public static final ConfigValue<Integer> ANTI_BASALT_REGION_SIZE = new ConfigValue<>("ANTI_BASALT_REGION_SIZE", 120);
    public static final ConfigValue<Integer> AFFECTED_STRUCTURE_RANDOM_OFFSET = new ConfigValue<>("AFFECTED_STRUCTURE_RANDOM_OFFSET", 3);
    public static final ConfigValue<Integer> AFFECTED_STRUCTURE_RANDOM_BOUND = new ConfigValue<>("AFFECTED_STRUCTURE_RANDOM_BOUND", 4);
    public static final ConfigValue<Integer> MAX_GLOBAL_STRONGHOLD_ROOM_LIMIT = new ConfigValue<>("MAX_GLOBAL_STRONGHOLD_ROOM_LIMIT", 10);
    public static final ConfigValue<Float> FLINT_MINIMUM_VALUE = new ConfigValue<>("FLINT_MINIMUM_VALUE", 0.6F);
    public static final ConfigValue<Float> ROD_MINIMUM_VALUE = new ConfigValue<>("ROD_MINIMUM_VALUE", 0.4F);
    public static final ConfigValue<Float> BURIED_TREASURE_RARITY = new ConfigValue<>("BURIED_TREASURE_RARITY", 0.07F);
    public static final ConfigValue<Float> OCEAN_CANYON_RARITY = new ConfigValue<>("OCEAN_CANYON_RARITY", 0.04F);
    public static final ConfigValue<Float> TREASURE_BASTION_GOLD_BLOCK_RARITY = new ConfigValue<>("TREASURE_BASTION_GOLD_BLOCK_RARITY", 0.2F);
    public static final ConfigValue<Float> PORTAL_EYE_ODDS = new ConfigValue<>("PORTAL_EYE_ODDS", 0.9F);
    public static final ConfigValue<Boolean> GUARANTEE_EYE_DROPS = new ConfigValue<>("GUARANTEE_EYE_DROPS", true);
    public static final ConfigValue<Boolean> ADD_BASALT_BASTIONS = new ConfigValue<>("ADD_BASALT_BASTIONS", true);

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
        int cx = quadrant.x * (RANDOM.nextInt(AFFECTED_STRUCTURE_RANDOM_BOUND.getValue()) + AFFECTED_STRUCTURE_RANDOM_OFFSET.getValue());
        int cz = quadrant.z * (RANDOM.nextInt(AFFECTED_STRUCTURE_RANDOM_BOUND.getValue()) + AFFECTED_STRUCTURE_RANDOM_OFFSET.getValue());
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
