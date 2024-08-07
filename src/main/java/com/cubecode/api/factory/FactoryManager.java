package com.cubecode.api.factory;

import com.cubecode.CubeCode;
import com.cubecode.api.utils.DirectoryManager;
import com.cubecode.utils.CubeRegistry;
import com.cubecode.utils.FactoryUtils;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FactoryManager extends DirectoryManager {
    private final List<String> elementsToRemove = new ArrayList<>();
    private List<String> elements = new ArrayList<>();

    public FactoryManager(File directory) {
        super(directory);

        this.elements = this.readFilesToString(this.getFiles());
    }

    public FactoryManager() {
        super();
    }

    public List<String> getElements() {
        return this.elements;
    }

    public void register(List<String> elements) {

    }

    public void register() {
        this.register(this.getElements());
    }

    public void unregister(List<String> elements) {
        if (elements == null)
            return;
    }

    public void unregister() {
        this.unregister(this.getElementsToRemove());
    }

    public List<String> getElementsToRemove() {
        return this.elementsToRemove;
    }

    public void addElementToRemove(String element) {
        this.elementsToRemove.add(element);
    }

    public void updateElementToRemove() {
        List<String> jsonElements = this.readFilesToString();
        List<String> elements = new ArrayList<>(this.getElements());

        this.getElements().forEach(element -> {
            if (jsonElements.contains(element)) {
                elements.remove(element);
            }
        });

        elements.forEach(this::addElementToRemove);
    }

    public <V, T extends V> void registerElement(Registry<V> registry, String id, T cubeElement) {
        if (FactoryUtils.isValidRegistriesId(id)) {
            CubeRegistry<V> cubeRegistry = (CubeRegistry<V>) registry;

            V existingElement = registry.get(new Identifier(CubeCode.MOD_ID, id));
            int rawId = cubeRegistry.getNextId();

            if (cubeElement.getClass().isInstance(existingElement)) {
                rawId = registry.getRawId(existingElement) == -1 ? cubeRegistry.getNextId() : registry.getRawId(existingElement);
            }

            cubeRegistry.set(rawId, registry, new Identifier(CubeCode.MOD_ID, id), cubeElement);
        }
    }
}
