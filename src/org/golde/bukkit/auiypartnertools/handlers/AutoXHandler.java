package org.golde.bukkit.auiypartnertools.handlers;

import java.util.HashMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.golde.bukkit.auiypartnertools.CustomItem;

public class AutoXHandler implements Listener {

	private HashMap<Location, Material> blocksToPress = new HashMap<Location, Material>();
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockPlaceEvent(BlockPlaceEvent e) {
		if(!e.canBuild()) {
			return;
		}
		if(e.isCancelled()) {
			return;
		}
		if(CustomItem.isEqual(e.getItemInHand(), CustomItem.AUTO_3X3)) {
			breakBlockWithNoDrops(e.getBlockPlaced());
			buildCage(e.getBlockPlaced().getLocation(), 7, 3, true, CustomItem.AutoXxX.getMaterialToPlace(e.getItemInHand()));
		}
		else if(CustomItem.isEqual(e.getItemInHand(), CustomItem.AUTO_1X1)) {
			blocksToPress.put(e.getBlockPlaced().getLocation(), CustomItem.AutoXxX.getMaterialToPlace(e.getItemInHand()));
		}
	}

	@EventHandler
	public void onPresurePlateStep(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.PHYSICAL)){
			if(e.getClickedBlock().getType() == Material.GOLD_PLATE){
				if(blocksToPress.containsKey(e.getClickedBlock().getLocation())) {
					breakBlockWithNoDrops(e.getClickedBlock());
					buildCage(e.getClickedBlock().getLocation(), 3, 2, true, blocksToPress.get(e.getClickedBlock().getLocation()));
				}
				
			}
		}
	}
	
	protected final void buildCage(Location entLoc, int sideLength, int height, boolean roof, Material mat) {

		// Let's make sure our preconditions were met.
		if(sideLength < 3 || sideLength % 2 == 0) {
			throw new IllegalArgumentException("You must enter an odd number greater than 3 for the side length");
		}
		else if(height == 0) {
			throw new IllegalArgumentException("Height must be greater than 0.");
		}

		int delta = (sideLength / 2);
		Location corner1 = new Location(entLoc.getWorld(), entLoc.getBlockX() + delta, entLoc.getBlockY() + 1, entLoc.getBlockZ() - delta);
		Location corner2 = new Location(entLoc.getWorld(), entLoc.getBlockX() - delta, entLoc.getBlockY() + 1, entLoc.getBlockZ() + delta);
		int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
		int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
		int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
		int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

		for(int x = minX; x <= maxX; x++) {
			for(int y = 0; y < height; y++) {
				for(int z = minZ; z <= maxZ; z++) {
					if((x == minX || x == maxX) || (z == minZ || z == maxZ)) {
						Block b = corner1.getWorld().getBlockAt(x, entLoc.getBlockY() + y, z);
						b.setType(mat);
					}

					if(y == height - 1 && roof) {
						Block b = corner1.getWorld().getBlockAt(x, entLoc.getBlockY() + y + 1, z);
						b.setType(mat);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	protected final void breakBlockWithNoDrops(Block block) {
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
		block.setType(Material.AIR);
	}
	
}
