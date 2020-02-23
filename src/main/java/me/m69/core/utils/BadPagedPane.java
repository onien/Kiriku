package me.m69.core.utils;

import me.m69.core.Main;
import me.m69.core.player.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * A bad paged pane
 */
public class BadPagedPane implements InventoryHolder {

    private Inventory inventory;

    private SortedMap<Integer, Page> pages = new TreeMap<>();
    private int currentIndex;
    private int pageSize;

    @SuppressWarnings("WeakerAccess")
    protected BadButton controlBack;
    @SuppressWarnings("WeakerAccess")
    protected BadButton controlNext;
    protected BadButton controlClose;
    protected BadButton controlTag;

    /**
     * @param pageSize The page size. inventory rows - 2
     */
    public BadPagedPane(int pageSize, int rows, String title) {
        Objects.requireNonNull(title, "title can not be null!");
        if (rows < 3) {
            throw new IllegalArgumentException("Rows must be >= 3, got " + rows);
        }
        if (rows > 6) {
            throw new IllegalArgumentException("Rows must be <= 6, got " + rows);
        }

        if (pageSize > 6) {
            throw new IllegalArgumentException("Page size must be <= 6, got" + pageSize);
        }

        this.pageSize = pageSize;
        inventory = Bukkit.createInventory(this, rows * 9, color(title));

        pages.put(0, new Page(pageSize));
    }

    /**
     * @param button The button to add
     */
    public void addButton(BadButton button) {
        for (Entry<Integer, Page> entry : pages.entrySet()) {
            if (entry.getValue().addButton(button)) {
                if (entry.getKey() == currentIndex) {
                    reRender();
                }
                return;
            }
        }
        Page page = new Page(pageSize);
        page.addButton(button);
        pages.put(pages.lastKey() + 1, page);
        
        reRender();
    }

    /**
     * @param button The Button to remove
     */
    @SuppressWarnings("unused")
    public void removeButton(BadButton button) {
        for (Iterator<Entry<Integer, Page>> iterator = pages.entrySet().iterator(); iterator.hasNext(); ) {
            Entry<Integer, Page> entry = iterator.next();
            if (entry.getValue().removeButton(button)) {

                // we may need to delete the page
                if (entry.getValue().isEmpty()) {
                    // we have more than one page, so delete it
                    if (pages.size() > 1) {
                        ;
                    }
                    // the currentIndex now points to a page that does not exist. Correct it.
                    if (currentIndex >= pages.size()) {
                        currentIndex--;
                    }
                }
                // if we modified the current one, re-render
                // if we deleted the current page, re-render too
                if (entry.getKey() >= currentIndex) {
                    reRender();
                }
                return;
            }
        }
    }

    /**
     * @return The amount of pages
     */
    @SuppressWarnings("WeakerAccess")
    public int getPageAmount() {
        return pages.size();
    }

    /**
     * @return The number of the current page (1 based)
     */
    @SuppressWarnings("WeakerAccess")
    public int getCurrentPage() {
        return currentIndex + 1;
    }

    /**
     * @param index The index of the new page
     */
    @SuppressWarnings("WeakerAccess")
    public void selectPage(int index) {
        if (index < 0 || index >= getPageAmount()) {
            throw new IllegalArgumentException(
                    "Index out of bounds: " + index + " [" + 0 + " " + getPageAmount() + ")"
            );
        }
        if (index == currentIndex) {
            return;
        }

        currentIndex = index;
        reRender();
    }

    /**
     * Renders the inventory again
     */
    @SuppressWarnings("WeakerAccess")
    public void reRender() {
        inventory.clear();
        pages.get(currentIndex).render(inventory);

        controlBack = null;
        controlNext = null;
        controlClose = null;
        controlTag = null;
        createControls(inventory);
    }

    /**
     * @param event The {@link InventoryClickEvent}
     */
    @SuppressWarnings("WeakerAccess")
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        // back item
        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null)return;
        if (event.getSlot() == inventory.getSize() - 8) {
            if (controlBack != null) {
                controlBack.onClick(event);
            }
            return;
        }
        // next item
        else if (event.getSlot() == inventory.getSize() - 2) {
            if (controlNext != null) {
                controlNext.onClick(event);
            }
            return;
        }
        // close inv
        else if (event.getSlot() == inventory.getSize() - 5) {
            if (controlClose != null) {
                controlClose.onClick(event);
            }
            return;
        }
        // clear tag
        else if (event.getSlot() == inventory.getSize() - 5) {
            if (controlClose != null) {
                controlClose.onClick(event);
            }
            return;
        }
        pages.get(currentIndex).handleClick(event);
    }

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Creates the controls
     *
     * @param inventory The inventory
     */
    @SuppressWarnings("WeakerAccess")
    protected void createControls(Inventory inventory) {
        // create separator
        fillRow(
                inventory.getSize() / 9 - 2,
                getItemStack(Material.STAINED_GLASS_PANE, 15, "&7 "),
                inventory
        );

        if (getCurrentPage() > 1) {
            String name = String.format(
                    Locale.ROOT,
                    "&e« Previous Page",
                    getCurrentPage() - 1, getPageAmount()
            );
            String lore = String.format(
                    Locale.ROOT,
                    "&7go to previous page",
                    getCurrentPage() - 1
            );
            ItemStack itemStack = getItemStack(Material.ARROW, 0, name, lore);
            controlBack = new BadButton(itemStack, event -> selectPage(currentIndex - 1));
            inventory.setItem(inventory.getSize() - 8, itemStack);
        }

        if (getCurrentPage() < getPageAmount()) {
            String name = String.format(
                    Locale.ROOT,
                    "&eNext Page »",
                    getCurrentPage() + 1, getPageAmount()
            );
            String lore = String.format(
                    Locale.ROOT,
                    "&7go to next page",
                    getCurrentPage() + 1
            );
            ItemStack itemStack = getItemStack(Material.ARROW, 0, name, lore);
            controlNext = new BadButton(itemStack, event -> selectPage(getCurrentPage()));
            inventory.setItem(inventory.getSize() - 2, itemStack);
        }

        boolean isTagMenu = inventory.getTitle().equalsIgnoreCase(Messages.CC("&6Tags"));

        String name = String.format(
                Locale.ROOT,
                isTagMenu ? "&cClear tag":"&cClose Menu",
                getCurrentPage(), getPageAmount()
        );
        String lore = String.format(
                Locale.ROOT,
                isTagMenu ? "&7Click to clear your tag": "&7Click to close menu",
                getCurrentPage(), getPageAmount()
        );
        ItemStack itemStack = getItemStack(Material.REDSTONE_BLOCK, 0, name, lore);
        inventory.setItem(inventory.getSize() - 5, itemStack);

        if (isTagMenu) {
            controlClose = new BadButton(itemStack, event -> new BukkitRunnable() {
                @Override
                public void run() {
                    Player p = (Player) event.getWhoClicked();
                    Main.getSqlManager().setTag(p.getUniqueId(), "None");
                    PlayerProfile.setTag(p, null);
                    p.sendMessage(Messages.CC("&aSuccessfully cleared your tag!"));
                    p.getOpenInventory().close();
                }
            }.runTaskLater(Main.getInstance(), 1));
        }else {
            controlClose = new BadButton(itemStack, event -> event.getWhoClicked().closeInventory());
        }
    }

    private void fillRow(int rowIndex, ItemStack itemStack, Inventory inventory) {
        int yMod = rowIndex * 9;
        for (int i = 0; i < 9; i++) {
            int slot = yMod + i;
            inventory.setItem(slot, itemStack);
        }
    }

    /**
     * @param type The {@link Material} of the {@link ItemStack}
     * @param durability The durability
     * @param name The name. May be null.
     * @param lore The lore. May be null.
     *
     * @return The item
     */
    @SuppressWarnings("WeakerAccess")
    protected ItemStack getItemStack(Material type, int durability, String name, String... lore) {
        ItemStack itemStack = new ItemStack(type, 1, (short) durability);

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (name != null) {
            itemMeta.setDisplayName(color(name));
        }
        if (lore != null && lore.length != 0) {
            itemMeta.setLore(Arrays.stream(lore).map(this::color).collect(Collectors.toList()));
        }
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @SuppressWarnings("WeakerAccess")
    protected String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    /**
     * @param player The {@link Player} to open it for
     */
    public void open(Player player) {
        reRender();
        player.openInventory(getInventory());
    }


    private static class Page {
        private List<BadButton> buttons = new ArrayList<>();
        private int maxSize;

        Page(int maxSize) {
            this.maxSize = maxSize;
        }

        /**
         * @param event The click event
         */
        void handleClick(InventoryClickEvent event) {
            // user clicked in his own inventory. Silently drop it
            if (event.getRawSlot() > event.getInventory().getSize()) {
                return;
            }

            if (event.getSlot() >= buttons.size()) {
                return;
            }
            BadButton button = buttons.get(event.getSlot());
            button.onClick(event);
        }

        /**
         * @return True if there is space left
         */
        boolean hasSpace() {
            return buttons.size() < maxSize * 9;
        }

        /**
         * @param button The {@link BadButton} to add
         *
         * @return True if the button was added, false if there was no space
         */
        boolean addButton(BadButton button) {
            if (!hasSpace()) {
                return false;
            }
            buttons.add(button);

            return true;
        }

        /**
         * @param button The {@link BadButton} to remove
         *
         * @return True if the button was removed
         */
        boolean removeButton(BadButton button) {
            return buttons.remove(button);
        }

        /**
         * @param inventory The inventory to render in
         */
        void render(Inventory inventory) {
            for (int i = 0; i < buttons.size(); i++) {
                BadButton button = buttons.get(i);

                inventory.setItem(i, button.getItemStack());
            }
        }

        /**
         * @return True if this page is empty
         */
        boolean isEmpty() {
            return buttons.isEmpty();
        }
    }

}
