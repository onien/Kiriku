package me.m69.core.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Listens for click events for the {@link BadPagedPane}
 */
public class BadPagedPaneListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof BadPagedPane) {
            ((BadPagedPane) holder).onClick(event);
        }else {
            return;
        }
    }
}
