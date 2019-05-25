package org.golde.bukkit.auiypartnertools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.golde.bukkit.auiypartnertools.cmds.CmdPitems;
import org.golde.bukkit.auiypartnertools.handlers.AutoXHandler;
import org.golde.bukkit.auiypartnertools.handlers.DecoyPerlHandler;
import org.golde.bukkit.auiypartnertools.handlers.FireballHandler;
import org.golde.bukkit.auiypartnertools.handlers.GrapplingHookHandler;
import org.golde.bukkit.auiypartnertools.handlers.HomingBowHandler;
import org.golde.bukkit.auiypartnertools.handlers.SnowballHandler;

public class Main extends JavaPlugin {

	private static Main instance;

	@Override
	public void onEnable() {
		instance = this;

		Bukkit.getPluginManager().registerEvents(new DecoyPerlHandler(), this);
		Bukkit.getPluginManager().registerEvents(new GrapplingHookHandler(), this);
		Bukkit.getPluginManager().registerEvents(new AutoXHandler(), this);
		Bukkit.getPluginManager().registerEvents(new HomingBowHandler(), this);
		
		Bukkit.getPluginManager().registerEvents(new SnowballHandler(), this);
		//Bukkit.getPluginManager().registerEvents(new FireballHandler(), this);
		

		getCommand("pitems").setExecutor(CmdPitems.getInstance());
		getCommand("pitems").setTabCompleter(CmdPitems.getInstance());

	}

	public static Main getInstance() {
		return instance;
	}

	public String color(String in) {
		return ChatColor.translateAlternateColorCodes('&', in);
	}

}