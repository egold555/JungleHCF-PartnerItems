package org.golde.bukkit.auiypartnertools.handlers;

import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.golde.bukkit.auiypartnertools.CustomItem;
import org.golde.bukkit.auiypartnertools.Main;

public class HomingBowHandler implements Listener {

	@EventHandler
	public void eventArrowFired(final EntityShootBowEvent e) {
		
		if(!CustomItem.isEqual(e.getBow(), CustomItem.BETA_HOMING_BOW)){
			return;
		}
		
		if (e.getEntity() instanceof LivingEntity && e.getProjectile() instanceof Arrow) {
			final LivingEntity player = e.getEntity();
			double minAngle = 6.283185307179586;
			Entity minEntity = null;
			for (final Entity entity : player.getNearbyEntities(64.0, 64.0, 64.0)) {
				if (player.hasLineOfSight(entity) && entity instanceof LivingEntity && !entity.isDead()) {
					final Vector toTarget = entity.getLocation().toVector().clone().subtract(player.getLocation().toVector());
					final double angle = e.getProjectile().getVelocity().angle(toTarget);
					if (angle >= minAngle) {
						continue;
					}
					minAngle = angle;
					minEntity = entity;
				}
			}
			if (minEntity != null) {
				new HomingTask((Arrow)e.getProjectile(), (LivingEntity)minEntity);
			}
		}
	}

	private class HomingTask extends BukkitRunnable
	{
		private static final double MaxRotationAngle = 0.12;
//		private static final double TargetSpeed = 1.4;
		private Arrow arrow;
		private LivingEntity target;

		public HomingTask(final Arrow arrow, final LivingEntity target) {
			this.arrow = arrow;
			this.target = target;
			this.runTaskTimer(Main.getInstance(), 1L, 1L);
		}

		public void run() {
			final double speed = this.arrow.getVelocity().length();
			if (this.arrow.isOnGround() || this.arrow.isDead() || this.target.isDead()) {
				this.cancel();
				return;
			}
			final Vector toTarget = this.target.getLocation().clone().add(new Vector(0.0, 0.5, 0.0)).subtract(this.arrow.getLocation()).toVector();
			final Vector dirVelocity = this.arrow.getVelocity().clone().normalize();
			final Vector dirToTarget = toTarget.clone().normalize();
			final double angle = dirVelocity.angle(dirToTarget);
			double newSpeed = 0.9 * speed + 0.13999999999999999;
			if (this.target instanceof Player && this.arrow.getLocation().distance(this.target.getLocation()) < 8.0) {
				final Player player = (Player)this.target;
				if (player.isBlocking()) {
					newSpeed = speed * 0.6;
				}
			}
			Vector newVelocity;
			if (angle < MaxRotationAngle) {
				newVelocity = dirVelocity.clone().multiply(newSpeed);
			}
			else {
				final Vector newDir = dirVelocity.clone().multiply((angle - MaxRotationAngle) / angle).add(dirToTarget.clone().multiply(MaxRotationAngle / angle));
				newDir.normalize();
				newVelocity = newDir.clone().multiply(newSpeed);
			}
			this.arrow.setVelocity(newVelocity.add(new Vector(0.0, 0.03, 0.0)));
			this.arrow.getWorld().playEffect(this.arrow.getLocation(), Effect.SMOKE, 0);
		}
	}

}
