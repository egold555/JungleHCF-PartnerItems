package org.golde.bukkit.auiypartnertools.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.golde.bukkit.auiypartnertools.CustomItem;
import org.golde.bukkit.auiypartnertools.Main;

public class CmdPitems implements CommandExecutor, TabCompleter {

	private static CmdPitems instance = new CmdPitems();
	public static CmdPitems getInstance() {
		return instance;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("pitems")){
			
			// /pitems <player_name> <partner_item> <amount> [extras]

			if(!sender.isOp() || !sender.hasPermission("partneritems.give")) {
				sender.sendMessage(getDenyMessage("Have permission?"));
				return true;
			}
			
			if(args.length < 3) {
				sender.sendMessage(ChatColor.RED + "/pitems <player_name> <partner_item> <amount> [extras]");
				return true;
			}

			Player p = Bukkit.getPlayer(args[0]);
			String item = args[1];
			int amount = Integer.parseInt(args[2]);
			Material mat = Material.STONE;
			if(args.length == 4) {
				mat = getMaterial(args[3]);
			}
			
			if(p == null || !p.isOnline()) {
				sender.sendMessage(ChatColor.RED + "Player isn't online.");
				return true;
			}

			if(item.equalsIgnoreCase("DECOY_PEARL")) {
				p.getInventory().addItem(CustomItem.DECOY_PEARL.getItemStack(amount));
			}
			else if(item.equalsIgnoreCase("GRAPPLING_HOOK")) {
				p.getInventory().addItem(CustomItem.GRAPPLING_HOOK.getItemStack(amount));
			}
			else if(item.equalsIgnoreCase("AUTO_1X1")) {
				p.getInventory().addItem(CustomItem.AUTO_1X1.getItemStack(amount, mat));
			}
			else if(item.equalsIgnoreCase("AUTO_3X3")) {
				p.getInventory().addItem(CustomItem.AUTO_3X3.getItemStack(amount, mat));
			}
			else if(item.equalsIgnoreCase("HOMING_BOW")) {
				p.getInventory().addItem(CustomItem.HOMING_BOW.getItemStack(amount));
			}
			p.updateInventory();
			return true;
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		List<String> toReturn = new ArrayList<String>();
		
		if(cmd.getName().equalsIgnoreCase("pitems")){
			if(args.length == 1) {
				
				for(Player p : Bukkit.getOnlinePlayers()) {
					toReturn.add(p.getName());
				}
				return toReturn;
			}
			else if(args.length == 2) {
				return getTypes(args[1]);
			}
			else if(args.length == 3) {
				for(int i = 1; i <= 64; i++) {
					toReturn.add(String.valueOf(i));
				}
				return toReturn;
			}
			if(args.length == 4 && args[1].equalsIgnoreCase("AUTO_1X1") || args[1].equalsIgnoreCase("AUTO_3X3")){
				for(Material mat : Material.values()) {
					if(mat.isBlock() && mat.isSolid()) {
						toReturn.add(mat.name());
					}
				}
				return toReturn;
			}
			
		}
		return new ArrayList<String>();
	}
	
	private List<String> getTypes(String input) {
		System.out.println("input: " + input.length());
		if(input.isEmpty()) {
			return CustomItem.LIST_OF_ITEMS;
		}
		
		List<String> toReturn = new ArrayList<String>();
		
		for(String s : CustomItem.LIST_OF_ITEMS) {
			if(s.toUpperCase().startsWith(input.toUpperCase())) {
				toReturn.add(s.toUpperCase());
			}
		}
		
		return toReturn;
		
	}
	
	private Material getMaterial(String in) {
		if(in == null || in.isEmpty()) {
			return Material.STONE;
		}
		try {
			return Material.valueOf(in);
		}
		catch(Exception e) {
			return Material.STONE;
		}
	}
	
	private String getDenyMessage(String doing) {
		return Main.getInstance().color("&cJohny, Johny\nYes, Papa?\n" + doing + "\nYes, papa!\nTelling lies?\nYes, papa :(");
	}
	
}
