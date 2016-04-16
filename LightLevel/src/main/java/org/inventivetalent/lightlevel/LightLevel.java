package org.inventivetalent.lightlevel;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.pluginannotations.PluginAnnotations;
import org.inventivetalent.pluginannotations.config.ConfigValue;
import org.inventivetalent.scriptconfig.ScriptConfigProvider;
import org.inventivetalent.scriptconfig.api.ScriptConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LightLevel extends JavaPlugin implements Listener {

	@ConfigValue(path = "radius") public static int       radius = 8;
	public static                               Set<UUID> tasks  = new HashSet<>();

	public static ScriptConfig scriptConfig;
	public static int[] particleColors = new int[16];

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);

		saveDefaultConfig();
		PluginAnnotations.CONFIG.loadValues(this, this);

		try {
			File scriptFile = new File(getDataFolder(), "script-config.js");
			if (!scriptFile.exists()) {
				scriptFile.createNewFile();
				saveResource("script-config.js", true);
			}
			scriptConfig = ScriptConfigProvider.create(this).load(scriptFile);

			for (int i = 0; i < 16; i++) {
				particleColors[i] = (int) scriptConfig.callFunction("particleColor", i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!player.isSneaking()) { return; }
		if (player.getInventory().getItemInHand() == null || player.getItemInHand().getType() != Material.TORCH) { return; }
		if (!player.hasPermission("lightlevel.use")) { return; }
		if (tasks.contains(player.getUniqueId())) { return; }

		new ParticleTask(player).runTaskTimer(this, 10, 10);
		tasks.add(player.getUniqueId());
	}
}
