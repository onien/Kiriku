package me.m69.core.tag;

import me.m69.core.Main;

import java.util.ArrayList;
import java.util.List;

public class TagManager {

    private List<Tag> tags;

    public TagManager() {
        this.tags = new ArrayList<>();
    }

    public void loadTags() {
        for(String t : Main.tagConfig.getConfigurationSection("Tags").getKeys(false)) {
            String name = t;
            String display = Main.tagConfig.getString("Tags." + t + ".display");

            Tag tag = new Tag(name, display);

            if (tag == null) {
                Main.getInstance().getServer().getConsoleSender().sendMessage("§cFailed to load tag " + tag.getName() + "...");
                return;
            }
            tags.add(tag);
            Main.getInstance().getServer().getConsoleSender().sendMessage("§aSuccessfully loaded tag " + tag.getName() + "!");
        }
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Tag getByName(String name) {
        for (Tag tag : Main.getTagManager().getTags()) {
            if (tag.getName().equals(name)) {
                return tag;
            }
        }
        return null;
    }
}
