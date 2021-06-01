package co.uk.mrpineapple.pinesfoodmod.common.event;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraft.world.gen.feature.template.StructureProcessorList;

import java.util.ArrayList;
import java.util.List;

public class Jigsaw {
    public static void registerJigsaw(MinecraftServer server, ResourceLocation poolLocation, ResourceLocation tagLocation, int weight) {
        DynamicRegistries manager = server.registryAccess();
        MutableRegistry<JigsawPattern> pools = manager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        JigsawPattern pool = pools.get(poolLocation);

        StructureProcessorList processorList = manager.registryOrThrow(Registry.PROCESSOR_LIST_REGISTRY).getOptional(poolLocation).orElse(ProcessorLists.EMPTY);
        List<JigsawPiece> jigsawPieceList = pool.templates;

        JigsawPiece jigsawPiece = JigsawPiece.legacy(tagLocation.toString(), processorList).apply(JigsawPattern.PlacementBehaviour.RIGID);
        for(int i = 0; i < weight; i++) {
            jigsawPieceList.add(jigsawPiece);
        }

        List<Pair<JigsawPiece, Integer>> elementCounts = new ArrayList(pool.rawTemplates);

        jigsawPieceList.addAll(pool.templates);
        elementCounts.addAll(pool.rawTemplates);

        pool.templates = jigsawPieceList;
        pool.rawTemplates = elementCounts;
    }
}
