package tech.thatgravyboat.vanity.tests;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import tech.thatgravyboat.vanity.common.registries.ModItems;
import tech.thatgravyboat.vanity.common.registries.ModProfessions;

public class VillagerTests implements FabricGameTest {

    @GameTest(template = EMPTY_STRUCTURE)
    public void testDataDrivenTrades(GameTestHelper helper) {
        Villager villager = helper.spawnWithNoFreeWill(EntityType.VILLAGER, 0, 2, 0);
        villager.setVillagerData(new VillagerData(
            VillagerType.PLAINS,
            ModProfessions.STYLIST.get(),
            1
        ));

        MerchantOffers offers = villager.getOffers();
        helper.assertTrue(!offers.isEmpty(), "Villager has no offers");
        for (MerchantOffer offer : offers) {
            if (!offer.getBaseCostA().is(ModItems.DESIGN.get())) {
                // Means that there was a trade that was from the data driven system
                helper.succeed();
                return;
            }
        }
        helper.fail("Test trade not found");
    }
}
