package tech.thatgravyboat.vanity.tests;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
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
            ItemStack first = offer.getBaseCostA();
            ItemStack result = offer.getResult();

            if (first.is(Items.STICK) && result.is(Items.STONE)) {
                helper.assertTrue(offer.getMaxUses() == 1, "Test trade has incorrect max uses");
                helper.assertTrue(offer.getXp() == 1, "Test trade has incorrect xp");
                helper.assertTrue(offer.getPriceMultiplier() == 0.3f, "Test trade has incorrect price multiplier");
                helper.succeed();
                return;
            }
        }
        helper.fail("Test trade not found");
    }
}
