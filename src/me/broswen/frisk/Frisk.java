package me.broswen.frisk;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Frisk extends JavaPlugin{
	public static Frisk plugin;
	public final MyPlayerListener playerListener = new MyPlayerListener();
	public static Economy econ = null;
	
	//when the plugin is enabled
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this.playerListener, this);
		
		loadConfig();
		
		if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
		if(getServer().getPluginManager().getPlugin("Essentials") == null){
			getLogger().severe("Plugin Disabled! Essentials plugin was not found!");
            getServer().getPluginManager().disablePlugin(this);
		}
		
		this.plugin = this;
	}
	
	//when the plugin is disabled
	@Override
	public void onDisable(){
	}

	//setting up the economy with vault
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	//the main command
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		PluginDescriptionFile pdfFile = this.getDescription();
			
		//check if the command is /frisk
		if(cmd.getName().equalsIgnoreCase("frisk")){
			
			//check the number of arguments
			if(args.length == 1){
				
				//checks if a player is using the command
				if(sender instanceof Player){
					Player player = (Player) sender;
						
						//sets the target player to the first argument
						Player targetPlayer = player.getServer().getPlayer(args [0]);
						
						//checks if the player has permission to frisk
						if(sender.hasPermission("frisk.frisk")){
							
							//checks if the target player is online
							if(targetPlayer == null){
								sender.sendMessage(ChatColor.RED + "FRISKING FAILED! That player is not online");
							}else{
								
								//checks if the target player is exempt from being frisked
								if(targetPlayer.hasPermission("frisk.exempt")){
									sender.sendMessage(ChatColor.RED + "FRISKING FAILED: That player may not be frisked!");
								}else{
									
									//checks if the target player is the command sender
									if(targetPlayer != sender){
										Player senderSender = (Player)sender;
										
										//checks if the target player is within 10 blocks
										if (senderSender.getLocation().distance(targetPlayer.getLocation()) <= 10) {
											
											//checks if the target player has the items
											if(hasItem(targetPlayer, Material.INK_SACK, (short) 3) || hasItem(targetPlayer, Material.INK_SACK, (short) 2) || targetPlayer.getInventory().contains(Material.PUMPKIN_SEEDS) || targetPlayer.getInventory().contains(Material.MELON_SEEDS) || targetPlayer.getInventory().contains(Material.WHEAT) || targetPlayer.getInventory().contains(Material.SUGAR) || targetPlayer.getInventory().contains(Material.NETHER_STALK)){
												
												//checks if the targetplayer is in a vehicle
												if(targetPlayer.isInsideVehicle()){
													targetPlayer.leaveVehicle();
												}
												
												String jailWorld = plugin.getConfig().getString("jailworld");
												Double jailx = plugin.getConfig().getDouble("jailx");
												Double jaily = plugin.getConfig().getDouble("jaily");
												Double jailz = plugin.getConfig().getDouble("jailz");
												int drugaward = plugin.getConfig().getInt("drugaward");
												
												String jailname = plugin.getConfig().getString("jailname");
												String jailtimeseconds = plugin.getConfig().getString("jailtimeseconds");
												Boolean usejailplugin = plugin.getConfig().getBoolean("usejailplugin");
												
												Location jailLocation = new Location(Bukkit.getWorld(jailWorld), jailx, jaily, jailz);
												
												if(usejailplugin){
													getServer().dispatchCommand(getServer().getConsoleSender(), "jail " + targetPlayer.getName() + " " + jailname + " " + jailtimeseconds + "s");
												}else{
													targetPlayer.teleport(jailLocation);
												}
												
												logToFile(player.getName() + " frisked and caught " + targetPlayer.getName() + " with drugs");
												
												player.sendMessage(ChatColor.RED + ((HumanEntity) targetPlayer).getName() + ChatColor.WHITE + " had drugs! You have recieved" + ChatColor.GREEN + " $" + drugaward + "!");
												EconomyResponse r = econ.depositPlayer(player.getName(), drugaward);
												((CommandSender) targetPlayer).sendMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " caught you with drugs!");
												Bukkit.broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " caught " + ChatColor.RED + ((HumanEntity) targetPlayer).getName() + ChatColor.WHITE + " with drugs! he/she was sent to jail!");
												Bukkit.broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " recieved $" + drugaward + " for jailing " + ChatColor.RED + ((HumanEntity) targetPlayer).getName() + ChatColor.WHITE + "!");
												
											}else if(targetPlayer.getInventory().contains(Material.PUMPKIN) || targetPlayer.getInventory().contains(Material.SUGAR_CANE)){
												
												//checks if the targetplayer is in a vehicle
												if(targetPlayer.isInsideVehicle()){
													targetPlayer.leaveVehicle();
												}
												
												String jailWorld = plugin.getConfig().getString("jailworld");
												Double jailx = plugin.getConfig().getDouble("jailx");
												Double jaily = plugin.getConfig().getDouble("jaily");
												Double jailz = plugin.getConfig().getDouble("jailz");
												int drugrelatedaward = plugin.getConfig().getInt("drugrelatedaward");
												
												String jailname = plugin.getConfig().getString("jailname");
												String jailtimeseconds = plugin.getConfig().getString("jailtimeseconds");
												Boolean usejailplugin = plugin.getConfig().getBoolean("usejailplugin");
												
												Location jailLocation = new Location(Bukkit.getWorld(jailWorld), jailx, jaily, jailz);
												
												if(usejailplugin){
													getServer().dispatchCommand(getServer().getConsoleSender(), "jail " + targetPlayer.getName() + " " + jailname + " " + jailtimeseconds + "s");
												}else{
													targetPlayer.teleport(jailLocation);
												}
												
												logToFile(player.getName() + " frisked and caught " + targetPlayer.getName() + " with paraphernalia");
												
												player.sendMessage(ChatColor.RED + ((HumanEntity) targetPlayer).getName() + ChatColor.WHITE + " had paraphernalia! You have recieved" + ChatColor.GREEN + " $" + drugrelatedaward + "!");
												EconomyResponse r = econ.depositPlayer(player.getName(), drugrelatedaward);
												((CommandSender) targetPlayer).sendMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " caught you with paraphernalia!");
												Bukkit.broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " caught " + ChatColor.RED + ((HumanEntity) targetPlayer).getName() + ChatColor.WHITE + " with paraphernalia! he/she was sent to jail!");
												Bukkit.broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " recieved" + ChatColor.GREEN + " $" + drugrelatedaward + ChatColor.WHITE + " for jailing " + ChatColor.RED + ((HumanEntity) targetPlayer).getName() + ChatColor.WHITE + "!");
											
											}else{
												sender.sendMessage(ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " doesn't have anything! Stop frisking innocent people!");
												player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 10, 0));
												player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
											}
										}else{
											sender.sendMessage(ChatColor.RED + "FRISKING FAILED! That player is out of range");
										}
									}else{
										sender.sendMessage(ChatColor.RED + "FRISKING FAILED! You may not frisk yourself");
									}
								}
							}
						}else{
							sender.sendMessage(ChatColor.GRAY + "You don't have permission!");
						}
				}else{
					getLogger().info("You must be a player to use that command!");
				}
				
			//checks the amount of arguments
			}else if(args.length > 1){
				sender.sendMessage(ChatColor.RED + "FRISKING FAILED! You may only frisk on player at a time!");
			}else{
				if(sender.hasPermission("frisk.about")){
					if(sender instanceof Player){
						Player player = (Player) sender;
						
						sender.sendMessage(ChatColor.GREEN + "######## FRISK ########");
						sender.sendMessage(ChatColor.WHITE + "Version: " + pdfFile.getVersion());
						sender.sendMessage(ChatColor.WHITE + "Author: " + pdfFile.getAuthors());
						sender.sendMessage(ChatColor.WHITE + "About: " + pdfFile.getDescription());
						sender.sendMessage(ChatColor.WHITE + "Type /frisk <player> to frisk someone");
						sender.sendMessage(ChatColor.GREEN + "######## FRISK ########");
					}else{
						getLogger().info("######## FRISK ########");
						getLogger().info("Version: " + pdfFile.getVersion());
						getLogger().info("Author: " + pdfFile.getAuthors());
						getLogger().info("About: " + pdfFile.getDescription());
						getLogger().info("Type /frisk <player> to frisk someone");
						getLogger().info("######## FRISK ########");
					}
				}else{
					sender.sendMessage(ChatColor.GRAY + "You don't have permission!");
				}
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("friskstick")){
			
			if(sender.hasPermission("frisk.friskstick.command")){
				
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					ItemStack friskStick = new ItemStack(Material.STICK);
					ItemMeta friskStickMeta = friskStick.getItemMeta();
					friskStickMeta.setDisplayName(ChatColor.RESET.AQUA + "Frisk Stick");
					friskStick.setItemMeta(friskStickMeta);
					
					player.sendMessage((ChatColor.RED + "FRISK: " + ChatColor.WHITE + "You recieved a Frisk Stick!"));
					
					player.getInventory().addItem(friskStick);
				}else{
					getLogger().info("You must be a player!");
				}
			}else{
				sender.sendMessage(ChatColor.GRAY + "You don't have permission!");
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("friskreload")){
			if(sender.hasPermission("frisk.reload")){
				reloadConfig();
				sender.sendMessage((ChatColor.RED + "FRISK: " + ChatColor.WHITE + "The config file was reloaded!"));
			}
		}
		
		return false;
	}
	
	//check is the player has an item
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
	
	public Economy getEconomy(Economy econ) {
        return econ;
    }

	public void loadConfig(){
		saveDefaultConfig();
		
		String drugrelatedaward = "drugrelatedaward";
		String drugaward = "drugaward";
		String jailx = "jailx";
		String jaily = "jaily";
		String jailz = "jailz";
		String jailworld = "jailworld";
		String jailname = "jailname";
		String jailtimeseconds = "jailtimeseconds";
		String usejailplugin = "usejailplugin";
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		getConfig().addDefault(usejailplugin, "false");
		getConfig().addDefault(jailtimeseconds, "120");
		getConfig().addDefault(jailname, "jail");
		getConfig().addDefault(drugrelatedaward, "500");
		getConfig().addDefault(drugaward, "1000");
		getConfig().addDefault(jailx, "10");
		getConfig().addDefault(jaily, "70");
		getConfig().addDefault(jailz, "10");
		getConfig().addDefault(jailworld, "world");
	}
	
	public void logToFile(String message){
		
		try{
			File dataFolder = getDataFolder();
			if(!dataFolder.exists()){
				dataFolder.mkdir();
			}
			
			File saveTo = new File(getDataFolder(), "log.txt");
			if(!saveTo.exists()){
				saveTo.createNewFile();
			}
			
			FileWriter fw = new FileWriter(saveTo, true);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.println(message);
			pw.flush();
			pw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}