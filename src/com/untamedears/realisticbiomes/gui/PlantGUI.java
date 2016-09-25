package com.untamedears.realisticbiomes.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.untamedears.realisticbiomes.GrowthConfig;
import com.untamedears.realisticbiomes.utils.PlayerMessages;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.inventorygui.DecorationStack;
import vg.civcraft.mc.civmodcore.inventorygui.IClickable;
import vg.civcraft.mc.civmodcore.itemHandling.ISUtils;

public class PlantGUI {

	private Player player;
	private GrowthConfig growth;
	private Biome biome;
	private ItemStack itemStack;
	private RealisticBiomesGUI parent;

	public PlantGUI(Player player, Biome biome, ItemStack itemStack, GrowthConfig growth, RealisticBiomesGUI parent) {
		this.player = player;
		this.biome = biome;
		this.itemStack = itemStack;
		this.growth = growth;
		this.parent = parent;
	}

	public void showScreen() {
		ClickableInventory ci = new ClickableInventory(36, this.growth.getName() + " in " + biome);
		
		int rowOffset = 0;
		
		if (growth.getNotFullSunlightMultiplier() > 1.0) {
			ItemStack is = new ItemStack(Material.STONE);
			ISUtils.setName(is, ChatColor.WHITE + "Darkness");
			ISUtils.addLore(is, ChatColor.GREEN + "Bonus when not in full sunlight: " + PlayerMessages.percent(growth.getNotFullSunlightMultiplier()));
			ci.setSlot(new DecorationStack(is), 4);
			rowOffset = 9;

		} else if (growth.getNotFullSunlightMultiplier() < 1.0) {
			ItemStack is = new ItemStack(Material.GLASS);
			ISUtils.setName(is, ChatColor.WHITE + "Sunlight");
			ISUtils.addLore(is, "Should not be blocked from sunlight");
			ISUtils.addLore(is, "Torches or light emitting blocks are not a substitute for sunlight");
			if (growth.isGreenhouseEnabled()) {
				ISUtils.addLore(is, "Will not be applied if greenhouse is active");
			}
			ISUtils.addLore(is, ChatColor.DARK_RED + "Base penalty when not in full sunlight: " + PlayerMessages.percent(growth.getNotFullSunlightMultiplier()));
			if (growth.needsSunLight()) {
				ISUtils.addLore(is, ChatColor.DARK_RED + "Additional strong penalty proportional to darkness");
			}
			ci.setSlot(new DecorationStack(is), 4);
			rowOffset = 9;
		}
		
		ISUtils.addLore(itemStack, "Under ideal conditions");
		ci.setSlot(new DecorationStack(itemStack), rowOffset + 4);
		
		if (growth.isGreenhouseEnabled()) {
			ItemStack is = new ItemStack(Material.GLOWSTONE);
			ISUtils.setName(is, ChatColor.WHITE + "Greenhouse");
			ISUtils.addLore(is, "Replaces the strong sunlight penalty for a smaller penalty");
			ISUtils.addLore(is, "Is worse than full sunlight but better than full darkness");
			ISUtils.addLore(is, "Must be placed directly next to or over the plant");
			ISUtils.addLore(is, ChatColor.GREEN + "Penalty: " + ChatColor.RED + PlayerMessages.percent(1.0 - growth.getGreenhouseRate()));
			ci.setSlot(new DecorationStack(is), rowOffset + 5);
		}
		
		rowOffset += 9;
		
		if (growth.getSoilMaxLayers() > 0) {
			ItemStack is = new ItemStack(growth.getSoilMaterial());
			if (growth.getSoilData() != -1) {
				is.setDurability(growth.getSoilData());
			}
			is.setAmount(growth.getSoilMaxLayers());
			
			ISUtils.addLore(is, "Use as fertilizer to grow at fastest rate");
			ISUtils.addLore(is, ChatColor.GREEN + "Up to " + growth.getSoilMaxLayers() + " layers");
			ISUtils.addLore(is, ChatColor.GREEN + "Starting at " + growth.getSoilLayerOffset() + " blocks below the plant");
			ISUtils.addLore(is, ChatColor.GREEN + "For a maximum bonus of " + PlayerMessages.percent(growth.getSoilBonusPerLevel() * growth.getSoilMaxLayers()));

			ci.setSlot(new DecorationStack(is), rowOffset + 4);
		}
		
		ci.setSlot(constructReturnClick(), 31);
		ci.showInventory(player);
	}


	private IClickable constructReturnClick() {
		ItemStack is = new ItemStack(Material.ARROW);
		ISUtils.setName(is, ChatColor.GOLD + "Go back to list");
		return new Clickable(is) {
			@Override
			public void clicked(Player p) {
				parent.showScreen();
			}
		};
	}
}
