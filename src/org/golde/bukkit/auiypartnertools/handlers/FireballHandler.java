package org.golde.bukkit.auiypartnertools.handlers;

import org.bukkit.GameMode;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.golde.bukkit.auiypartnertools.CustomItem;

public class FireballHandler implements Listener {

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}

		if(!CustomItem.isEqual(e.getItem(), CustomItem.FIREBALL)) {
			return;
		}
		
		final Fireball f = (Fireball)e.getPlayer().launchProjectile((Class)Fireball.class);
		f.setIsIncendiary(true);
		f.setYield(0);
		
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			final int amount = e.getPlayer().getInventory().getItemInHand().getAmount() - 1;
			final ItemStack charge = CustomItem.FIREBALL.getItemStack(amount);
			e.getPlayer().getInventory().setItemInHand(charge);
		}
		
		
	}

}
