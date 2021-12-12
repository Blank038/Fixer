package com.blank038.fixer.bukkit.model.pixelmon.stats;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;

import java.util.Arrays;
import java.util.List;

/**
 * @author Blank038
 * @since 2021-05-18
 */
public enum FixStatsEnum {
    Zacian(EnumSpecies.Zacian, new int[]{92, 170, 115, 80, 115, 148}, Arrays.asList(EnumType.Fairy, EnumType.Steel)),
    Zamazenta(EnumSpecies.Zamazenta, new int[]{92, 130, 145, 80, 145, 128}, Arrays.asList(EnumType.Fighting, EnumType.Steel));

    private final EnumSpecies species;
    private final int[] array;
    private final List<EnumType> types;

    FixStatsEnum(EnumSpecies species, int[] array, List<EnumType> types) {
        this.species = species;
        this.array = array;
        this.types = types;
    }

    public EnumSpecies getSpecies() {
        return this.species;
    }

    public int getStats(int index) {
        return this.array[index];
    }

    public List<EnumType> getTypes() {
        return this.types;
    }
}
