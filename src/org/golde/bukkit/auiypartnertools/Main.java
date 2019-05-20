package org.golde.bukkit.auiypartnertools;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.golde.bukkit.auiypartnertools.handlers.Auto1Handler;
import org.golde.bukkit.auiypartnertools.handlers.Auto3Handler;
import org.golde.bukkit.auiypartnertools.handlers.DecoyPerlHandler;
import org.golde.bukkit.auiypartnertools.handlers.GrapplingHookHandler;
import org.golde.bukkit.auiypartnertools.handlers.GrapplingHookHandler2;
import org.golde.bukkit.auiypartnertools.handlers.HomingBowHandler;

public class Main extends JavaPlugin implements Listener{

	private static Main instance;

	@Override
	public void onEnable() {
		instance = this;
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new DecoyPerlHandler(), this);
		Bukkit.getPluginManager().registerEvents(new GrapplingHookHandler(), this);
		Bukkit.getPluginManager().registerEvents(new Auto1Handler(), this);
		Bukkit.getPluginManager().registerEvents(new Auto3Handler(), this);
		
		Bukkit.getPluginManager().registerEvents(new GrapplingHookHandler2(), this);
		Bukkit.getPluginManager().registerEvents(new HomingBowHandler(), this);
		
		//getCommand("piconsole").setExecutor(new CmdPIConsole());
		getCommand("partneritem").setExecutor(this);
		getCommand("partneritem").setTabCompleter(new TabCompleter() {

			@Override
			public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
				return Arrays.asList(new String[] {"DECOIL_PERL", "GRAPPLING_HOOK", "AUTO_1X1", "AUTO_3X3", "BETA_GRAPPLING_HOOK", "BETA_HOMING_BOW"});
			}
		});
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(cmd.getName().equalsIgnoreCase("partneritem")){

			if(!(sender instanceof Player)) {
				sender.sendMessage(color("&cJohny, Johny\nYes, Papa?\nAre you a Player?\nYes, papa!\nTelling lies?\nYes, papa :("));
				return true;
			}

			Player p = (Player)sender;
			
			if(!p.isOp() || !p.hasPermission("partneritems.give")) {
				p.sendMessage(color("&cJohny, Johny\nYes, Papa?\nHave permission?\nYes, papa!\nTelling lies?\nYes, papa :("));
				return true;
			}

			if(args.length != 1) {
				p.sendMessage(color("&c/partneritem <DECOIL_PERL | GRAPPLING_HOOK | AUTO_1X1 | AUTO_3X3 | BETA_GRAPPLING_HOOK | BETA_HOMING_BOW>"));
				return true;
			}

			

			if(args[0].equalsIgnoreCase("DECOIL_PERL")) {
				p.getInventory().addItem(CustomItem.DECOIL_PERL.getItemStack(64));
			}
			else if(args[0].equalsIgnoreCase("GRAPPLING_HOOK")) {
				p.getInventory().addItem(CustomItem.GRAPPLING_HOOK.getItemStack(1));
			}
			else if(args[0].equalsIgnoreCase("AUTO_1X1")) {
				p.getInventory().addItem(CustomItem.AUTO_1X1.getItemStack(64));
			}
			else if(args[0].equalsIgnoreCase("AUTO_3X3")) {
				p.getInventory().addItem(CustomItem.AUTO_3X3.getItemStack(64));
			}
			else if(args[0].equalsIgnoreCase("BETA_GRAPPLING_HOOK")) {
				p.getInventory().addItem(CustomItem.BETA_GRAPPLING_HOOK.getItemStack(1));
			}
			else if(args[0].equalsIgnoreCase("BETA_HOMING_BOW")) {
				p.getInventory().addItem(CustomItem.BETA_HOMING_BOW.getItemStack(1));
			}

			return true;
		}
		return true;
	}

	public static Main getInstance() {
		return instance;
	}

	public String color(String in) {
		return ChatColor.translateAlternateColorCodes('&', in);
	}

}