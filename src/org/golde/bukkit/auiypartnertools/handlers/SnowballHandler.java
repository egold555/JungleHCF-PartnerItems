package org.golde.bukkit.auiypartnertools.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.golde.bukkit.auiypartnertools.CustomItem;
import org.golde.bukkit.auiypartnertools.Main;

public class SnowballHandler implements Listener {

	private List<UUID> fakeSnowballs = new ArrayList<UUID>();
	
	@EventHandler
	public void pojectileThrowEvent(final ProjectileLaunchEvent e) {
		if(e.getEntityType() == EntityType.SNOWBALL) {
			if(e.getEntity().getShooter() instanceof Player) {
				final Player shooter = (Player)e.getEntity().getShooter();
				ItemStack inHand = shooter.getItemInHand();
				if(CustomItem.isEqual(inHand, CustomItem.SWAP_SNOWBALL)) {
					fakeSnowballs.add(e.getEntity().getUniqueId());
				}
			}
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if(e.getEntityType() == EntityType.SNOWBALL) {
			if(e.getHitEntity() != null && e.getHitEntity() instanceof Player) {
				if(fakeSnowballs.contains(e.getEntity().getUniqueId())) {
					Player thrower = (Player) e.getEntity().getShooter();
					Player hit = (Player) e.getHitEntity();
					doSwap(thrower, hit);
				}
			}
		}
	}
	
	private void doSwap(Player thrower, Player hit) {
		final Location throwerLoc = thrower.getLocation();
		final Location hitLoc = hit.getLocation();
		final World world = throwerLoc.getWorld();
		
		thrower.teleport(hitLoc,  TeleportCause.PLUGIN);
		hit.teleport(throwerLoc,  TeleportCause.PLUGIN);
		
		
		world.playSound(hitLoc, Sound.PORTAL_TRAVEL, 0.2f, 1);
		world.playSound(throwerLoc, Sound.PORTAL_TRAVEL, 0.2f, 1);
		
		for(int i = 0; i < 3; i++) {
			world.playEffect(hitLoc.add(0, i, 0), Effect.ENDER_SIGNAL, 0);
			world.playEffect(throwerLoc.add(0, i, 0), Effect.ENDER_SIGNAL, 0);
		}
		
		thrower.sendMessage(Main.getInstance().color("&bYou and &e" + hit.getName() + " &bhave swapped places!"));
		hit.sendMessage(Main.getInstance().color("&bYou and &e" + thrower.getName() + " &bhave swapped places!"));
		
		
	}
	
}
