package com.ferrin.antigrief.protection;

import com.ferrin.antigrief.FerrinAntiGrief;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class NBTProtector {

    private final FerrinAntiGrief plugin;

    public NBTProtector(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    public boolean isIllegal(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        byte[] bytes = item.serializeAsBytes();
        if (bytes.length > plugin.getConfig().getInt("protection.nbt.max-serialized-bytes")) {
            return true;
        }

        if (item.getItemMeta() instanceof BookMeta book) {
            if (book.getPageCount() > plugin.getConfig().getInt("protection.nbt.max-book-pages")) {
                return true;
            }
            for (String page : book.getPages()) {
                if (page.length() > plugin.getConfig().getInt("protection.nbt.max-page-length")) {
                    return true;
                }
            }
        }
        
        String nbtString = item.toString();
        int depth = 0;
        int maxDepth = 0;
        for (char c : nbtString.toCharArray()) {
            if (c == '{' || c == '[') depth++;
            if (c == '}' || c == ']') depth--;
            maxDepth = Math.max(maxDepth, depth);
        }

        return maxDepth > plugin.getConfig().getInt("protection.nbt.max-depth");
    }
}