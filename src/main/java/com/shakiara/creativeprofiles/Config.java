package com.shakiara.creativeprofiles;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> CREATIVE_WORLD = BUILDER
            .comment("Nombre del mundo creativo creado con Multiworld.")
            .define("creativeWorld", "creative");

    public static final ModConfigSpec.ConfigValue<String> SURVIVAL_WORLD = BUILDER
            .comment("Mundo principal de survival.")
            .define("survivalWorld", "minecraft:overworld");

    public static final ModConfigSpec SPEC = BUILDER.build();
}