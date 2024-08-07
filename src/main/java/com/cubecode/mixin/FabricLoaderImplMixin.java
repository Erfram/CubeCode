package com.cubecode.mixin;

import com.cubecode.CubeCode;
import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.ModContainerImpl;
import net.fabricmc.loader.impl.discovery.ModCandidate;
import net.fabricmc.loader.impl.discovery.ModCandidateImpl;
import net.fabricmc.loader.impl.discovery.ModResolutionException;
import net.fabricmc.loader.impl.entrypoint.EntrypointStorage;
import net.fabricmc.loader.impl.metadata.EntrypointMetadata;
import org.spongepowered.asm.mixin.*;

import java.util.Map;

@Mixin(FabricLoaderImpl.class)
public abstract class FabricLoaderImplMixin {
    @Shadow
    protected abstract void addMod(ModCandidate candidate) throws ModResolutionException;

    @Shadow @Final private EntrypointStorage entrypointStorage;

    @Shadow @Final private Map<String, LanguageAdapter> adapterMap;


    @Unique
    public void setupDynamic() {
        ModCandidate candidate = ModCandidateImpl.create("F:\\cubecode\\");
        if (candidate != null) {
            try {
                this.addMod(candidate);
            } catch (ModResolutionException e) {
                CubeCode.LOGGER.info("Failed to create dynamic folder!");
            }
        }
        ModContainerImpl mod = new ModContainerImpl(candidate);
        try {
            for (String in : mod.getInfo().getOldInitializers()) {
                String adapter = mod.getInfo().getOldStyleLanguageAdapter();
                entrypointStorage.addDeprecated(mod, adapter, in);
            }

            for (String key : mod.getInfo().getEntrypointKeys()) {
                for (EntrypointMetadata in : mod.getInfo().getEntrypoints(key)) {
                    entrypointStorage.add(mod, key, in, adapterMap);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to setup mod %s (%s)", mod.getInfo().getName(), mod.getOrigin()), e);
        }
    }

}
