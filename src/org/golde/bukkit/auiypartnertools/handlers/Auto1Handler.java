package org.golde.bukkit.auiypartnertools.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.golde.bukkit.auiypartnertools.CustomItem;

public class Auto1Handler extends AutoHandlerBase {

	List<Location> blocksToPress = new ArrayList<Location>();
	
	@EventHandler
	public void blockPlaceEvent(BlockPlaceEvent e) {
		if(CustomItem.isEqual(e.getItemInHand(), CustomItem.AUTO_1X1)) {
			blocksToPress.add(e.getBlockPlaced().getLocation());
		}
	}
	
	@EventHandler
	public void onPresurePlateStep(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.PHYSICAL)){
			if(e.getClickedBlock().getType() == Material.GOLD_PLATE){
				breakBlockWithNoDrops(e.getClickedBlock());
				buildCage(e.getClickedBlock().getLocation(), 3, 2, true, Material.STONE);
			}
		}
	}
	
}
