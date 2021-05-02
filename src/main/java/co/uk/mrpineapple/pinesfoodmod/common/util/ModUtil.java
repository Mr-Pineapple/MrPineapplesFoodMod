package co.uk.mrpineapple.pinesfoodmod.common.util;

import co.uk.mrpineapple.pinesfoodmod.core.PinesFoodMod;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ModUtil {
    @Nonnull
    public static ResourceLocation mergeID(String path) {
        return new ResourceLocation(PinesFoodMod.ID, path);
    }
}
