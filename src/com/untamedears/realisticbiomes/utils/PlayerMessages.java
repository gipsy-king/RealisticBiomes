package com.untamedears.realisticbiomes.utils;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;

public class PlayerMessages {
	public static String percent(double multiplier) {
		return new DecimalFormat("#0").format(multiplier * 100) + "%";
	}

	public static String hoursToMaturity(double rate, boolean colored) {
		String message = colored
				? (Double.isInfinite(rate) ? ChatColor.DARK_RED : ChatColor.GREEN).toString()
				: "";
		if (Double.isInfinite(rate)) {
			message += "Does not grow";
		} else {
			message += hours(rate) + " hours to maturity";
		}
		return message;
	}

	public static String spawnRate(double rate, boolean colored, boolean isEntity) {
		String message = colored
				? (rate > 0.0 ? ChatColor.GREEN : ChatColor.DARK_RED).toString()
				: "";
		
		String noun = isEntity ? "breed" : "growth";
		if (rate == 0) {
			message += "No " + noun;
		} else {
			message += percent(rate) + " " + noun + " chance";
		}
		return message;
	}

	public static String hours(double rate) {
		return new DecimalFormat("#0.00").format(rate);
	}
}
