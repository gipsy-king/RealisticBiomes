package com.untamedears.realisticbiomes.command;

import com.untamedears.realisticbiomes.GrowthMap;
import com.untamedears.realisticbiomes.command.commands.GUICommand;

import vg.civcraft.mc.civmodcore.command.CommandHandler;

public class RealisticBiomesCommandHandler extends CommandHandler {

	private GrowthMap growthMap;

	public RealisticBiomesCommandHandler(GrowthMap growthMap) {
		this.growthMap = growthMap;
	}

	@Override
	public void registerCommands() {
		addCommands(new GUICommand(growthMap));
	}

}
