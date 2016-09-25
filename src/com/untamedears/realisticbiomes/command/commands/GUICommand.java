package com.untamedears.realisticbiomes.command.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.untamedears.realisticbiomes.GrowthMap;
import com.untamedears.realisticbiomes.gui.RealisticBiomesGUI;

import vg.civcraft.mc.civmodcore.command.PlayerCommand;

public class GUICommand extends PlayerCommand {

	private GrowthMap growthMap;

	public GUICommand(GrowthMap growthMap) {
		super("RBGUI");
		setDescription("Opens the RealisticBiomes GUI");
		setUsage("/rb");
		setArguments(0, 0);
		setIdentifier("rb");
		this.growthMap = growthMap;
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.AQUA + "The technology is not there yet");
			return true;
		}
		
		Player player = (Player) sender;
		Location location = player.getLocation();
		RealisticBiomesGUI gui = new RealisticBiomesGUI(player, location.getBlock().getBiome(), this.growthMap);
		gui.showScreen();
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender arg0, String[] arg1) {
		return null;
	}

}
