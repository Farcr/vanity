package tech.thatgravyboat.vanity.common.handler.unlockables;

import com.mojang.logging.LogUtils;
import com.teamresourceful.resourcefullib.common.utils.SaveHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.*;

public class UnlockableSaveHandler extends SaveHandler {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String FILE_NAME = "vanity_unlockables";
    private static final UnlockableSaveHandler CLIENT = new UnlockableSaveHandler();

    private final Map<UUID, List<ResourceLocation>> unlockables = new HashMap<>();

    public static UnlockableSaveHandler get(Level level) {
        return SaveHandler.read(level, CLIENT, UnlockableSaveHandler::new, FILE_NAME);
    }

    public static boolean addUnlockable(Level level, UUID uuid, ResourceLocation id) {
        UnlockableSaveHandler handler = get(level);
        List<ResourceLocation> unlockables = handler.getUnlockables(uuid);
        if (unlockables.contains(id)) {
            return false;
        } else {
            unlockables.add(id);
            handler.setDirty();
            return true;
        }
    }

    public static void setUnlockables(Level level, UUID uuid, Collection<ResourceLocation> ids) {
        SaveHandler.handle(
            level,
            UnlockableSaveHandler::get,
            handler -> {
                List<ResourceLocation> unlockables = handler.getUnlockables(uuid);
                unlockables.clear();
                unlockables.addAll(ids);
            }
        );
    }

    public static List<ResourceLocation> getUnlockables(Level level, UUID uuid) {
        return get(level).getUnlockables(uuid);
    }

    public List<ResourceLocation> getUnlockables(UUID uuid) {
        if (!this.unlockables.containsKey(uuid)) {
            this.unlockables.put(uuid, new ArrayList<>());
        }
        return this.unlockables.get(uuid);
    }

    @Override
    public void loadData(CompoundTag tag) {
        for (String key : tag.getAllKeys()) {
            UUID uuid = UUID.fromString(key);
            List<ResourceLocation> unlockables = getUnlockables(uuid);
            for (Tag listEntry : tag.getList(key, Tag.TAG_STRING)) {
                ResourceLocation id = ResourceLocation.tryParse(listEntry.getAsString());
                if (id != null) {
                    unlockables.add(id);
                } else {
                    LOGGER.error("Failed to parse unlockable id: {}", listEntry.getAsString());
                }
            }
        }
    }

    @Override
    public void saveData(CompoundTag tag) {
        for (var entry : this.unlockables.entrySet()) {
            ListTag list = new ListTag();
            for (ResourceLocation id : entry.getValue()) {
                list.add(StringTag.valueOf(id.toString()));
            }
            tag.put(entry.getKey().toString(), list);
        }
    }
}
