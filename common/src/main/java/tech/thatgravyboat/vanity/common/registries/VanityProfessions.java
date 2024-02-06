package tech.thatgravyboat.vanity.common.registries;

import com.google.common.collect.ImmutableSet;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import tech.thatgravyboat.vanity.common.Vanity;

import java.util.HashSet;

public class VanityProfessions {

    public static final ResourcefulRegistry<PoiType> POIS = ResourcefulRegistries.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, Vanity.MOD_ID);
    public static final ResourcefulRegistry<VillagerProfession> PROFESSIONS = ResourcefulRegistries.create(BuiltInRegistries.VILLAGER_PROFESSION, Vanity.MOD_ID);

    public static final RegistryEntry<PoiType> STYLIST_POI = POIS.register("stylist", () -> new PoiType(new HashSet<>(VanityBlocks.STYLING_TABLE.get().getStateDefinition().getPossibleStates()), 1, 1));
    public static final RegistryEntry<VillagerProfession> STYLIST = PROFESSIONS.register("stylist", () -> new VillagerProfession(Vanity.MOD_ID + ":stylist", poi -> poi.is(STYLIST_POI.getId()), poi -> poi.is(STYLIST_POI.getId()), ImmutableSet.of(), ImmutableSet.of(), VanitySounds.TAKE_RESULT_STYLING_TABLE.get()));
}
