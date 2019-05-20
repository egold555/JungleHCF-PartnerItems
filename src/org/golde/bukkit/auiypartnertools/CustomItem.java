package org.golde.bukkit.auiypartnertools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItem {
	
	public static final List<String> LIST_OF_ITEMS;
	
	static {
		String[] items = new String[]{"DECOY_PEARL", "GRAPPLING_HOOK", "HOMING_BOW", "AUTO_1X1", "AUTO_3X3"};
		Arrays.sort(items);
		LIST_OF_ITEMS = Arrays.asList(items);
	}
	
	private static final String PARTNER_SMH = "&5&lsmhroyal";
	private static final String PARTNER_3TIPS = "&1&l3Tips";
	private static final String PARTNER_BETA = "&4&l**BETA**";
	
	public static final CustomItem DECOY_PEARL = new CustomItem(Material.ENDER_PEARL, "&bDecoy Pearl", PARTNER_SMH);
	public static final CustomItem GRAPPLING_HOOK = new CustomItem(Material.FISHING_ROD, "&bGrappling Hook", PARTNER_SMH);
	public static final CustomItem HOMING_BOW = new CustomItem(Material.BOW, "&bHoming Bow", PARTNER_SMH);
	
	public static final AutoXxX AUTO_1X1 = new AutoXxX(Material.GOLD_PLATE, "&bAuto1x1 &7(1 x 1 - 2 High)");
	public static final AutoXxX AUTO_3X3 = new AutoXxX(Material.SAPLING, "&bAutoBox &7(3 x 3 - 2 High)");

	
	
	protected final ItemStack is;
	protected CustomItem(Material mat, String name, String partner) {
		is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		
		im.setDisplayName(Main.getInstance().color(name));
		im.setLore(Arrays.asList(new String[] {Main.getInstance().color("&7&oPartner Item &8- " + partner) }));
		
		is.setItemMeta(im);
	}
	
	public ItemStack getItemStack(int amount) {
		ItemStack clone = this.is.clone();
		clone.setAmount(amount);
		return clone;
	}
	
	private static boolean isEqual(ItemStack first, ItemStack second) {
		if(first != null && second != null) {
			if(first.getType().equals(second.getType())) {
				if(first.getItemMeta() != null && second.getItemMeta() != null) {
					if(first.getItemMeta().getDisplayName() != null && second.getItemMeta().getDisplayName() != null) {
						if(first.getItemMeta().getDisplayName().equals(second.getItemMeta().getDisplayName())) {
							return true;
						}
					}
					
				}
			}
		}
		return false;
	}

	public static boolean isEqual(ItemStack inHand, CustomItem second) {
		return isEqual(inHand, second.is);
	}
	
	
	public static class AutoXxX extends CustomItem {
		
		protected AutoXxX(Material mat, String name) {
			super(mat, name, PARTNER_3TIPS);
		}
		
		@Override
		public ItemStack getItemStack(int amount) {
			return getItemStack(amount, Material.STONE);
		}
		
		public ItemStack getItemStack(int amount, Material place) {
			ItemStack clone = this.is.clone();
			clone.setAmount(amount);
			ItemMeta im = clone.getItemMeta();
			im.setLore(Arrays.asList(new String[] {ChatColor.GRAY + "Block: " + ChatColor.GOLD + place.name()}));
			clone.setItemMeta(im);
			return clone;
		}
		
		public static Material getMaterialToPlace(ItemStack is) {
			ItemMeta im = is.getItemMeta();
			String lore = im.getLore().get(0);
			lore = ChatColor.stripColor(lore);
			lore = lore.replace("Block: ", "");
			try {
				return Material.valueOf(lore);
			}
			catch(Exception e) {
				return Material.STONE;
			}
		}
		
	}
	
}
