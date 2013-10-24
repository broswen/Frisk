package me.broswen.frisk;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MyPlayerListener implements Listener{
	public static Frisk plugin;
	public static Economy econ = null;
	
	private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	/*
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event){
		Entity entity = event.getEntity();
		Player player = (Player) event.getDamager();
		
		if(entity instanceof Player){
			Player targetPlayer = (Player) entity;
			
			if(player.getItemInHand().getItemMeta().getDisplayName().equals("Frisk Stick") && player.getItemInHand().getType() == Material.STICK){
				
				event.setCancelled(true);
				
				if(player.hasPermission("frisk.friskstick")){
					
					if(!targetPlayer.hasPermission("frisk.exempt")){
						
						if(hasItem(targetPlayer, Material.INK_SACK, (short) 3) || hasItem(targetPlayer, Material.INK_SACK, (short) 2) || targetPlayer.getInventory().contains(Material.PUMPKIN_SEEDS) || targetPlayer.getInventory().contains(Material.MELON_SEEDS) || targetPlayer.getInventory().contains(Material.WHEAT) || targetPlayer.getInventory().contains(Material.SUGAR) || targetPlayer.getInventory().contains(Material.NETHER_STALK)){
							
							//checks if the targetplayer is in a vehicle
							if(targetPlayer.isInsideVehicle()){
								targetPlayer.leaveVehicle();
							}
							
							String jailWorld = plugin.getConfig().getString("jailworld");
							Double jailx = plugin.getConfig().getDouble("jailx");
							Double jaily = plugin.getConfig().getDouble("jaily");
							Double jailz = plugin.getConfig().getDouble("jailz");
							
							Location jailLocation = new Location(Bukkit.getWorld(jailWorld), jailx, jaily, jailz);
							
							targetPlayer.teleport(jailLocation);
							player.sendMessage(ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " had drugs! You have recieved" + ChatColor.GREEN + " $1000!");
							EconomyResponse r = econ.depositPlayer(player.getName(), 1000.0);
							targetPlayer.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " caught you with drugs!");
							Bukkit.broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " caught " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " with drugs! he/she was sent to jail!");
							Bukkit.broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " recieved $1000 for jailing " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + "!");
							
						}else if(targetPlayer.getInventory().contains(Material.PUMPKIN) || targetPlayer.getInventory().contains(Material.SUGAR_CANE)){
							
							//checks if the targetplayer is in a vehicle
							if(targetPlayer.isInsideVehicle()){
								targetPlayer.leaveVehicle();
							}
							
							String jailWorld = plugin.getConfig().getString("jailworld");
							Double jailx = plugin.getConfig().getDouble("jailx");
							Double jaily = plugin.getConfig().getDouble("jaily");
							Double jailz = plugin.getConfig().getDouble("jailz");
							
							Location jailLocation = new Location(Bukkit.getWorld(jailWorld), jailx, jaily, jailz);
							
							targetPlayer.teleport(jailLocation);
							player.sendMessage(ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + "That player had paraphernalia! You have recieved" + ChatColor.GREEN + " $500!");
							EconomyResponse r = econ.depositPlayer(player.getName(), 500.0);
							targetPlayer.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " caught you with paraphernalia!");
							Bukkit.broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " caught " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " with paraphernalia! he/she was sent to jail!");
							Bukkit.broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " recieved" + ChatColor.GREEN + " $500" + ChatColor.WHITE + " for jailing " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + "!");
						
						}else{
							player.sendMessage(ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " doesn't have anything! Stop frisking innocent people!");
							player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 10, 0));
							player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
						}
						
					}else{
						player.sendMessage(ChatColor.RED + "FRISKING FAILED: That player may not be frisked!");
					}
					
				}else{
					player.sendMessage(ChatColor.GRAY + "You are not allowed to use that!");
				}
				
			}
		}
	}
	*/
	
	
	public boolean hasItem(Player p, Material m, short s){
	    Inventory inv = p.getInventory();
	    for(ItemStack item : inv){
	        //This will return an NullPointerException if you do not have this if statement.
	        if(item != null){
	            if(item.getType() == m && item.getData().getData() == s){
	                return true;
	            }
	        }
	    }
	    return false;
	}
}
