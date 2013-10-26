package me.broswen.frisk;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MyPlayerListener implements Listener{
	public static Frisk plugin;
	public static Economy econ = null;
	
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		Entity targetPlayer = event.getRightClicked();
		Player player = event.getPlayer();
		this.plugin = Frisk.plugin;
		
		ItemStack friskStick = new ItemStack(Material.STICK);
		ItemMeta friskStickMeta = friskStick.getItemMeta();
		friskStickMeta.setDisplayName(ChatColor.RESET.AQUA + "Frisk Stick");
		friskStick.setItemMeta(friskStickMeta);
			
			if(player.getItemInHand().equals(friskStick)){
				
				if(targetPlayer instanceof Player){
					String targetPlayerName = ((HumanEntity) targetPlayer).getName();
					
					if(player.hasPermission("frisk.friskstick")){
						Bukkit.dispatchCommand(player, "frisk " + targetPlayerName);
					}
				}
				
			}
		}
	}
