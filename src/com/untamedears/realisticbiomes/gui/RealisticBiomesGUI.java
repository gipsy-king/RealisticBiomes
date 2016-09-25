package com.untamedears.realisticbiomes.gui;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

import com.untamedears.realisticbiomes.GrowthConfig;
import com.untamedears.realisticbiomes.GrowthMap;
import com.untamedears.realisticbiomes.utils.MaterialAliases;
import com.untamedears.realisticbiomes.utils.PlayerMessages;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.DecorationStack;
import vg.civcraft.mc.civmodcore.inventorygui.IClickable;
import vg.civcraft.mc.civmodcore.inventorygui.MultiPageView;
import vg.civcraft.mc.civmodcore.itemHandling.ISUtils;;

public class RealisticBiomesGUI {

	private Player player;
	private GrowthMap growthMap;
	private Biome biome;

	public RealisticBiomesGUI(Player player, Biome biome, GrowthMap growthMap) {
		this.player = player;
		this.biome = biome;
		this.growthMap = growthMap;
	}

	public void showScreen() {
		MultiPageView view = new MultiPageView(player, constructClickables(), "Growth in " + biome, true);
		view.showScreen();
	}

	private List<IClickable> constructClickables() {
		List <IClickable> clicks = new LinkedList<IClickable>();

		for(Object key: this.growthMap.keySet()) {
			ItemStack is = null;
			GrowthConfig growth = null;

			if (key instanceof Material) {
				Material material = (Material)key;
				Material alias = MaterialAliases.getItemFromBlock(material);
				growth =  this.growthMap.get(material);

				if (alias == null) {
					alias = material;
				}
				
				is = new ItemStack(alias);
				if (alias == Material.INK_SACK) {
					is.setDurability((short) 3);
				}

			} else if (key instanceof TreeType) {
				TreeType treeType = (TreeType)key;
				growth = this.growthMap.get(treeType);

				short saplingData = MaterialAliases.getSaplingData(treeType);
				if (saplingData != -1) {
					is = new ItemStack(Material.SAPLING, 1, saplingData);
				}

			} else if (key instanceof EntityType) {
				EntityType entity = (EntityType)key;
				growth = this.growthMap.get(entity);

				is = new SpawnEgg(entity).toItemStack(); // adding lore remove the custom spawnegg appearance
				ISUtils.setName(is, ChatColor.WHITE + entity.toString());
			}

			if (is != null && growth != null) {
				double rate = growth.getBiomeRate(this.biome);
				if (Double.isInfinite(rate)) {
					rate = 0.0;
				}

				// apply max fertilizer bonus
				rate *= (1.0 + (growth.getSoilMaxLayers() * (10 * growth.getSoilBonusPerLevel()) / 10));
				
				
				int amount;
				if (growth.isPersistent()) {
					rate = (1.0/(rate*(60.0*60.0/*seconds per hour*/)));	
					amount = Double.isInfinite(rate) ? 0 : (int) Math.min(1000, Math.ceil(rate));
					is.setAmount(amount);
					ISUtils.addLore(is, PlayerMessages.hoursToMaturity(rate, true));

				} else {
					amount = (int) (rate * 100);
					is.setAmount(amount);
					ISUtils.addLore(is, PlayerMessages.spawnRate(rate, true, key instanceof EntityType));
				}

				if (amount != 0) {
					final GrowthConfig growthRef = growth;
					final ItemStack isRef = is;
					clicks.add(new Clickable(is) {
						
						@Override
						public void clicked(Player p) {
							PlantGUI gui = new PlantGUI(p, biome, isRef, growthRef, RealisticBiomesGUI.this);
							gui.showScreen();
						}
					});
				} else {
					clicks.add(new DecorationStack(is));
				}
			}
		}
		
		clicks.sort(new Comparator<IClickable>() {
			@SuppressWarnings("deprecation")
			@Override
			public int compare(IClickable o1, IClickable o2) {
				return o1.getItemStack().getTypeId() - o2.getItemStack().getTypeId();
			}
		});
		
		return clicks;
	}

}
