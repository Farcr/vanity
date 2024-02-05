package tech.thatgravyboat.vanity.common.menu.provider;

import com.teamresourceful.resourcefullib.common.menu.ContentMenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.thatgravyboat.vanity.common.block.StylingTableBlockEntity;
import tech.thatgravyboat.vanity.common.handler.concept.ServerConceptArtManager;
import tech.thatgravyboat.vanity.common.handler.unlockables.UnlockableSaveHandler;
import tech.thatgravyboat.vanity.common.item.ConceptArtHelper;
import tech.thatgravyboat.vanity.common.menu.StylingMenu;
import tech.thatgravyboat.vanity.common.menu.content.StylingMenuContent;
import tech.thatgravyboat.vanity.common.util.ConstantComponents;

import java.util.ArrayList;
import java.util.List;

public class StylingProvider implements ContentMenuProvider<StylingMenuContent> {

    private final StylingTableBlockEntity entity;

    public StylingProvider(StylingTableBlockEntity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ConstantComponents.CONTAINER_TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        if (this.entity.getLevel() == null) return null;
        return new StylingMenu(
                i, inventory,
                ContainerLevelAccess.create(this.entity.getLevel(), this.entity.getBlockPos()),
                ServerConceptArtManager.INSTANCE, getConceptsForPlayer(player)
        );
    }

    @Override
    public StylingMenuContent createContent(ServerPlayer player) {
        return new StylingMenuContent(this.entity.getBlockPos(), getConceptsForPlayer(player));
    }

    private List<ResourceLocation> getConceptsForPlayer(Player player) {
        List<ResourceLocation> concepts = new ArrayList<>(UnlockableSaveHandler.getUnlockables(player.level(), player.getUUID()));
        for (ItemStack item : this.entity.items()) {
            ResourceLocation id = ConceptArtHelper.getArtId(item);
            if (id == null) continue;
            concepts.add(id);
        }
        concepts.addAll(ServerConceptArtManager.INSTANCE.getDefaultConceptArt().keySet());
        return concepts;
    }
}