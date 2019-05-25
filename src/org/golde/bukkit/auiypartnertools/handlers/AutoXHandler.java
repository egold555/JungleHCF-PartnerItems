package org.golde.bukkit.auiypartnertools.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.bukkit.auiypartnertools.CustomItem;
import org.golde.bukkit.auiypartnertools.Main;

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

		final BlockPlacerBreaker blockPlacerBreaker = new BlockPlacerBreaker();
		
		for(int x = minX; x <= maxX; x++) {
			for(int y = 0; y < height; y++) {
				for(int z = minZ; z <= maxZ; z++) {
					if((x == minX || x == maxX) || (z == minZ || z == maxZ)) {
						Block b = corner1.getWorld().getBlockAt(x, entLoc.getBlockY() + y, z);
						if(b.getType() == Material.AIR) {
							//b.setType(mat);
							blockPlacerBreaker.addBlock(b, mat);
						}
					}

					if(y == height - 1 && roof) {
						Block b = corner1.getWorld().getBlockAt(x, entLoc.getBlockY() + y + 1, z);
						if(b.getType() == Material.AIR) {
							//b.setType(mat);
							blockPlacerBreaker.addBlock(b, mat);
						}
					}
				}
			}
		}
		blockPlacerBreaker.placeBlocks();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				blockPlacerBreaker.breakBlocks();
			}
		}.runTaskLater(Main.getInstance(), 20*5);
	}

	@SuppressWarnings("deprecation")
	protected final void breakBlockWithNoDrops(Block block) {
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
		block.setType(Material.AIR);
	}
	
	private class BlockPlacerBreaker {
		
		private class BlockMaterial {

			public final Material material;
			public final Block block;
			
			private BlockMaterial(Block block, Material material) {
				this.block = block;
				this.material = material;
			}
			
		}
		
		private int placedIndex = 0;
		private int breakIndex = 0;
		
		List<BlockMaterial> addedBlocks = new ArrayList<BlockMaterial>();
		
		public void addBlock(Block block, Material mat) {
			addedBlocks.add(new BlockMaterial(block, mat));
		}
		
		public void placeBlocks() {
			Collections.shuffle(addedBlocks);
			placedIndex = 0;
			new BukkitRunnable() {

				@Override
				public void run() {
					
					for(int add = 0; add < 9; add++) {
						if(placedIndex > addedBlocks.size()-1) {
							this.cancel();
							return;
						}
						
						BlockMaterial bm = addedBlocks.get(placedIndex);
						bm.block.setType(bm.material);
						placedIndex++;
					}
					
					
				}
				
			}.runTaskTimer(Main.getInstance(), 0, 1);
		}
		
		public void breakBlocks() {
			breakIndex = 0;
			new BukkitRunnable() {

				@Override
				public void run() {
					
					for(int add = 0; add < 9; add++) {
						if(breakIndex > addedBlocks.size()-1) {
							this.cancel();
							return;
						}
						
						BlockMaterial bm = addedBlocks.get(breakIndex);
						breakBlockWithNoDrops(bm.block);
						breakIndex++;
					}
					
				}
				
			}.runTaskTimer(Main.getInstance(), 0, 1);
		}
		
	}

}
