package org.golde.bukkit.auiypartnertools.handlers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;
import org.golde.bukkit.auiypartnertools.CustomItem;
import org.golde.bukkit.auiypartnertools.Main;

public class GrapplingHookHandler2 implements Listener {

	
	HashMap<UUID, FishHook> hooks = new HashMap<UUID, FishHook>();
    HashMap<UUID, HookState> states = new HashMap<UUID, HookState>();
    
    public GrapplingHookHandler2() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
               reelPlayers();
            }
        }, 1L, 1L);
	}
    
    private void reelPlayers() {
        for (final World world : Main.getInstance().getServer().getWorlds()) {
            for (final Player player : world.getPlayers()) {
                final UUID id = player.getUniqueId();
                if (this.states.containsKey(id) && this.states.get(id) == HookState.AIR) {
                    final FishHook hook = this.hooks.get(id);
                    if (this.inGround(hook)) {
                        if (!player.isSneaking() || !hook.isValid()) {
                            continue;
                        }
                        final Vector pull = hook.getLocation().subtract(player.getLocation()).toVector().normalize().multiply(0.05).add(new Vector(0.0, 0.04, 0.0));
                        player.setVelocity(player.getVelocity().add(pull));
                        if (pull.getY() <= 0.0) {
                            continue;
                        }
                        player.setFallDistance(0.0f);
                    }
                    else {
                        this.lineOfSight(player, hook);
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void eventFish(final PlayerFishEvent e) {
    	
    	if(!CustomItem.isEqual(e.getPlayer().getItemInHand(), CustomItem.BETA_GRAPPLING_HOOK)) {
    		return;
    	}
    	
        final UUID id = e.getPlayer().getUniqueId();
        final FishHook hook = e.getHook();
        if (e.getState() == PlayerFishEvent.State.FISHING) {
            this.hooks.put(id, hook);
            this.states.put(id, HookState.AIR);
        }
        else if (e.getState() == PlayerFishEvent.State.FAILED_ATTEMPT || e.getState() == PlayerFishEvent.State.IN_GROUND) {
            this.hooks.remove(id);
            this.states.put(id, HookState.NONE);
        }
    }
    
    private boolean inGround(final FishHook hook) {
        return hook.isOnGround() || hook.getWorld().getBlockAt(hook.getLocation()).getType().isSolid() || hook.getWorld().getBlockAt(hook.getLocation()).getRelative(BlockFace.DOWN).getType().isSolid();
    }
    
    private void lineOfSight(final Player player, final FishHook hook) {
        final Vector dir = hook.getLocation().subtract(player.getEyeLocation().subtract(new Vector(0.0, 0.001, 0.0))).toVector().normalize();
        final Location pos = player.getEyeLocation();
        final World world = player.getWorld();
        int x = 0;
        int y = -1;
        int z = 0;
        dir.multiply(0.1);
        while (pos.distance(hook.getLocation()) > 0.5) {
            pos.add(dir);
            final int newx = pos.getBlockX();
            final int newy = pos.getBlockY();
            final int newz = pos.getBlockZ();
            if (newx != x || newy != y || newz != z) {
                x = newx;
                y = newy;
                z = newz;
                final Block block = world.getBlockAt(pos);
                if (!block.getType().isSolid()) {
                    continue;
                }
                pos.setY(y + 1.0);
                hook.teleport(pos);
                hook.setVelocity(new Vector(0, 0, 0));
            }
        }
    }
    
    public enum HookState
    {
        NONE("NONE", 0), 
        AIR("AIR", 1), 
        REELING("REELING", 2);
        
        private HookState(final String s, final int n) {
        }
    }

}
