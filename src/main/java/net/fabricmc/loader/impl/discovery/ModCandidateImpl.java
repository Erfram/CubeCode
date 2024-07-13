package net.fabricmc.loader.impl.discovery;

import com.cubecode.CubeCode;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.impl.game.GameProvider;
import net.fabricmc.loader.impl.metadata.BuiltinModMetadata;
import net.fabricmc.loader.impl.metadata.DependencyOverrides;
import net.fabricmc.loader.impl.metadata.VersionOverrides;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ModCandidateImpl {

    @Nullable
    public static ModCandidate create(String folderPath) {
        try {
            Path path = Paths.get(folderPath);
            Path config = Paths.get(path + "/config");
            ModMetadata metadata = new BuiltinModMetadata.Builder("cubecode_dynamic", "1.0").build();
            return ModCandidate.createBuiltin(
                    new GameProvider.BuiltinMod(List.of(path), metadata),
                    new VersionOverrides(),
                    new DependencyOverrides(config)
            );
        } catch (Exception e) {
            CubeCode.LOGGER.info(e.getMessage());
        }
        return null;
    }

}
