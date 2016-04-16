package org.inventivetalent.lightlevel;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.particle.ParticleEffect;

import java.awt.*;
import java.util.Collections;

public class ParticleTask extends BukkitRunnable {

	Player player;

	public ParticleTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		if (!player.isOnline()) {
			cancel();
			return;
		}
		if (!player.isSneaking()) {
			cancel();
			return;
		}

		for (int x = -LightLevel.radius; x < LightLevel.radius; x++) {
			for (int z = -LightLevel.radius; z < LightLevel.radius; z++) {
				for (int y = -LightLevel.radius; y < LightLevel.radius; y++) {
					Location location = player.getLocation().add(x, y, z);
					if (location.getBlock().getType() == Material.AIR || !location.getBlock().getType().isSolid()) { continue; }
					if (location.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
						location = location.getBlock().getLocation().add(.5, 1, .5);
						int lightLevel = location.getBlock().getLightLevel();

						Color color = new Color(LightLevel.particleColors[lightLevel]);

						ParticleEffect.REDSTONE.sendColor(Collections.singleton(player), location.getX(), location.getY(), location.getZ(), color);
					}
				}
			}
		}

	}

	@Override
	public synchronized void cancel() throws IllegalStateException {
		super.cancel();
		LightLevel.tasks.remove(player.getUniqueId());
	}
}
