package org.golde.bukkit.auiypartnertools.handlers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.golde.bukkit.auiypartnertools.CustomItem;

public class Auto3Handler extends AutoHandlerBase {

	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent e) {
		if(CustomItem.isEqual(e.getItemInHand(), CustomItem.AUTO_3X3)) {
			breakBlockWithNoDrops(e.getBlockPlaced());
			buildCage(e.getBlockPlaced().getLocation(), 7, 3, true, Material.STONE);
		}
	}
	
}
