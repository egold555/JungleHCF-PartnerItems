package org.golde.bukkit.auiypartnertools;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItem {
	
	private static final String PARTNER_SMH = "&5&lsmhroyal";
	private static final String PARTNER_3TIPS = "&1&l3Tips";
	private static final String PARTNER_BETA = "&4&l**BETA**";
	
	public static final CustomItem DECOIL_PERL = new CustomItem(Material.ENDER_PEARL, "&bDecoy Pearl", PARTNER_SMH);
	public static final CustomItem GRAPPLING_HOOK = new CustomItem(Material.FISHING_ROD, "&bGrappling Hook", PARTNER_SMH);
	
	public static final CustomItem AUTO_1X1 = new CustomItem(Material.GOLD_PLATE, "&bAuto1x1 &7(1 x 1 - 2 High)", PARTNER_3TIPS);
	public static final CustomItem AUTO_3X3 = new CustomItem(Material.SAPLING, "&bAutoBox &7(3 x 3 - 2 High)", PARTNER_3TIPS);
	
	public static final CustomItem BETA_GRAPPLING_HOOK = new CustomItem(Material.FISHING_ROD, "&bBeta Grappling Hook", PARTNER_BETA);
	public static final CustomItem BETA_HOMING_BOW = new CustomItem(Material.BOW, "&bHoming Bow", PARTNER_BETA);
	
	private final ItemStack is;
	private CustomItem(Material mat, String name, String partner) {
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
	
}
