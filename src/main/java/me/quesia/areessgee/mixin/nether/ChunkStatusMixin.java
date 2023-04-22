package me.quesia.areessgee.mixin.nether;

import com.mojang.datafixers.util.Either;
import me.quesia.areessgee.AreEssGee;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Mixin(ChunkStatus.class)
@SuppressWarnings("SameParameterValue")
public abstract class ChunkStatusMixin {
    @SuppressWarnings("SameReturnValue") @Shadow private static ChunkStatus register(String id, @Nullable ChunkStatus previous, int taskMargin, EnumSet<Heightmap.Type> heightMapTypes, ChunkStatus.ChunkType chunkType, ChunkStatus.GenerationTask task) { return null; }
    @Shadow @Final public static ChunkStatus EMPTY;
    @Shadow @Final private static EnumSet<Heightmap.Type> PRE_CARVER_HEIGHTMAPS;

    static {
        ChunkStatus ignored = register("fake_structure_starts", EMPTY, 0, PRE_CARVER_HEIGHTMAPS, ChunkStatus.ChunkType.field_12808, (ChunkStatus targetStatus, ServerWorld world, ChunkGenerator generator, StructureManager structureManager, ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> function, List<Chunk> surroundingChunks, Chunk chunk1) -> {
            Chunk chunk = AreEssGee.calculateChunk(chunk1);

            if (!chunk.getStatus().isAtLeast(targetStatus)) {
                if (world.getServer().getSaveProperties().getGeneratorOptions().shouldGenerateStructures()) {
                    generator.setStructureStarts(world.getStructureAccessor(), chunk, structureManager, world.getSeed());
                }
                if (chunk instanceof ProtoChunk) {
                    ((ProtoChunk)chunk).setStatus(targetStatus);
                }
            }
            return CompletableFuture.completedFuture(Either.left(chunk));
        });
    }
}
