package org.golde.bukkit.auiypartnertools.handlers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;
import org.golde.bukkit.auiypartnertools.CustomItem;
import org.golde.bukkit.auiypartnertools.Main;

public class GrapplingHookHandler implements Listener {

	private Map<Integer, Integer> noFallEntities = new HashMap<Integer, Integer>();
	
	double hookThreshold = 0.25;
	double hForceMult = 0.3;
	double hForceMax = 7.5;
	double vForceMult = 0.7;
	double vForceBonus = 0.5;
	double vForceMax = 0.9;
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerFish(PlayerFishEvent event) {
		Vector vector3;
		Entity entity;
		Block block;
		Player player;
		double d;
		
		if (event.getState().equals(PlayerFishEvent.State.IN_GROUND) || event.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT)) {
			entity = event.getHook();
			block = entity.getWorld().getBlockAt(entity.getLocation().add(0.0, -hookThreshold, 0.0));
			
			if (!block.isEmpty() && !block.isLiquid()) {
				player = event.getPlayer();
				
				if(!CustomItem.isEqual(player.getItemInHand(), CustomItem.GRAPPLING_HOOK)) {
					return;
				}
				
				vector3 = entity.getLocation().subtract(player.getLocation()).toVector();
				
				if (vector3.getY() < 0.0)
					vector3.setY(0.0);
				
				vector3.setX(vector3.getX() * hForceMult);
				vector3.setY(vector3.getY() * vForceMult + vForceBonus);
				vector3.setZ(vector3.getZ() * hForceMult);
				
				d = hForceMax * hForceMax;
				if (vector3.clone().setY(0.0).lengthSquared() > d) {
					d = d / vector3.lengthSquared();
					vector3.setX(vector3.getX() * d);
					vector3.setZ(vector3.getZ() * d);
				}
				
				if (vector3.getY() > vForceMax)
					vector3.setY(vForceMax);
				
				player.setVelocity(vector3);
				addNoFall(player, 100);
			}
		}
	}
	
	private void addNoFall(final Entity e, final int ticks) {
        if (this.noFallEntities.containsKey(e.getEntityId())) {
            Bukkit.getServer().getScheduler().cancelTask((int)this.noFallEntities.get(e.getEntityId()));
        }
        final int taskId = Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (noFallEntities.containsKey(e.getEntityId())) {
                    noFallEntities.remove(e.getEntityId());
                }
            }
        }, (long)ticks);
        this.noFallEntities.put(e.getEntityId(), taskId);
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(final EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (this.noFallEntities.containsKey(event.getEntity().getEntityId())) {
                event.setCancelled(true);
            }
        }
    }

}
