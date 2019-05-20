package org.golde.bukkit.auiypartnertools.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EnderpearlLandEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.bukkit.auiypartnertools.CustomItem;
import org.golde.bukkit.auiypartnertools.Main;

public class DecoyPerlHandler implements Listener {

	private List<UUID> fakeEnderPerls = new ArrayList<UUID>();
	private final Map<String, Long> lastThrow = new HashMap<String, Long>();
    private static final Set<Material> interactables = new HashSet<Material>(Arrays.asList(Material.ANVIL, Material.COMMAND, Material.BED, Material.BEACON, Material.BED_BLOCK, Material.BREWING_STAND, Material.BURNING_FURNACE, Material.CAKE_BLOCK, Material.CHEST, Material.DIODE, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DISPENSER, Material.DROPPER, Material.ENCHANTMENT_TABLE, Material.ENDER_CHEST, Material.FENCE_GATE, Material.FENCE_GATE, Material.FURNACE, Material.HOPPER, Material.IRON_DOOR, Material.IRON_DOOR_BLOCK, Material.ITEM_FRAME, Material.LEVER, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.STONE_BUTTON, Material.TRAP_DOOR, Material.TRAPPED_CHEST, Material.WOODEN_DOOR, Material.WOOD_BUTTON, Material.WOOD_DOOR, Material.WORKBENCH));
	
    
    @EventHandler()
    public void onPlayerUseEP(final PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getItem() == null || event.getItem().getType() != Material.ENDER_PEARL) {
            return;
        }
        if (event.getClickedBlock() != null && !event.isCancelled() && !event.getPlayer().isSneaking()) {
            final Material clickedMat = event.getClickedBlock().getType();
            if (interactables.contains(clickedMat)) {
                return;
            }
        }
        
        if(!CustomItem.isEqual(event.getItem(), CustomItem.DECOY_PEARL)) {
        	return;
        }
      
        final Player player = event.getPlayer();
        final Long now = System.currentTimeMillis();
        if (this.validthrow(player, now)) {
        	this.lastThrow.put(player.getName(), now);
        }
        else {
            event.setCancelled(true);
            player.updateInventory();
        }
    }
    
    private boolean validthrow(final Player player, final long throwTime) {
        final Long lastPlayerPearl = this.lastThrow.get(player.getName());
        if (lastPlayerPearl == null || throwTime - lastPlayerPearl >= 3020) {
            return true;
        }
        return false;
    }
    
	@SuppressWarnings("deprecation")
	@EventHandler
	public void pojectileThrowEvent(final ProjectileLaunchEvent e) {
		if(e.getEntityType() == EntityType.ENDER_PEARL) {
			if(e.getEntity().getShooter() instanceof Player) {
				final Player shooter = (Player)e.getEntity().getShooter();
				ItemStack inHand = shooter.getItemInHand();
				if(CustomItem.isEqual(inHand, CustomItem.DECOY_PEARL)) {
					fakeEnderPerls.add(e.getEntity().getUniqueId());
					new BukkitRunnable() {

						@Override
						public void run() {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timer set Enderpearl " + shooter.getName() + " 3s");
						}
						
					}.runTask(Main.getInstance());
				}
			}
		}
	}
	
	@EventHandler
	public void enderPerlLandEvent(EnderpearlLandEvent e) {
		if(fakeEnderPerls.contains(e.getEntity().getUniqueId())) {
			e.setCancelled(true);
		}
	}
	
}
