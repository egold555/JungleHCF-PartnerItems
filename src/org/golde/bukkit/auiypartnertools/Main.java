package org.golde.bukkit.auiypartnertools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.golde.bukkit.auiypartnertools.cmds.CmdPitems;
import org.golde.bukkit.auiypartnertools.handlers.AutoXHandler;
import org.golde.bukkit.auiypartnertools.handlers.DecoyPerlHandler;
import org.golde.bukkit.auiypartnertools.handlers.GrapplingHookHandler;
import org.golde.bukkit.auiypartnertools.handlers.HomingBowHandler;

public class Main extends JavaPlugin implements Listener{

	private static Main instance;

	@Override
	public void onEnable() {
		instance = this;
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new DecoyPerlHandler(), this);
		Bukkit.getPluginManager().registerEvents(new GrapplingHookHandler(), this);
		Bukkit.getPluginManager().registerEvents(new AutoXHandler(), this);

		Bukkit.getPluginManager().registerEvents(new HomingBowHandler(), this);
		
		//getCommand("piconsole").setExecutor(new CmdPIConsole());
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