package com.shakiara.creativeprofiles.constants;

/**
 * Planned for v2.0.0.
 *
 * This class will centralize PersistentData keys during the v2.0.0
 * architecture refactor. v1.1.0 intentionally keeps the current
 * hardcoded key style to avoid changing storage structure during a
 * compatibility-focused release.
 */
public final class ProfileKeys {
    private ProfileKeys() {}

    public static final String CURRENT_PROFILE = "creativeprofiles_current_profile";

    public static final String SURVIVAL_INVENTORY = "creativeprofiles_survival_inventory";
    public static final String CREATIVE_INVENTORY = "creativeprofiles_creative_inventory";

    public static final String SURVIVAL_XP = "creativeprofiles_survival_xp";
    public static final String CREATIVE_XP = "creativeprofiles_creative_xp";

    public static final String SURVIVAL_ENDERCHEST = "creativeprofiles_survival_enderchest";
    public static final String CREATIVE_ENDERCHEST = "creativeprofiles_creative_enderchest";

    public static final String SURVIVAL_POSITION = "creativeprofiles_survival_position";
    public static final String CREATIVE_POSITION = "creativeprofiles_creative_position";

    public static final String SURVIVAL_CURIOS = "creativeprofiles_survival_curios";
    public static final String CREATIVE_CURIOS = "creativeprofiles_creative_curios";

    public static final String SURVIVAL_COSMETIC_ARMOR = "creativeprofiles_survival_cosmetic_armor";
    public static final String CREATIVE_COSMETIC_ARMOR = "creativeprofiles_creative_cosmetic_armor";

    public static final String INTERNAL_TELEPORT = "creativeprofiles_internal_teleport";
}