package me.broswen.frisk;

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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Frisk extends JavaPlugin{
	public static Frisk plugin;
	public final MyPlayerListener playerListener = new MyPlayerListener();
	public static Economy econ = null;
	
	@Override
	public void onEnable(){
		loadConfiguration();
		getServer().getPluginManager().registerEvents(this.playerListener, this);
		if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
	}
	
	@Override
	public void onDisable(){
	}

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
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		PluginDescriptionFile pdfFile = this.getDescription();
			
		if(cmd.getName().equalsIgnoreCase("frisk")){
			if(args.length == 1){
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					if(getConfig().getBoolean("friskenabled") == true){
						Player targetPlayer = player.getServer().getPlayer(args [0]);
						if(sender.hasPermission("frisk.frisk")){
							if(targetPlayer == null){
								sender.sendMessage(ChatColor.RED + "FRISKING FAILED! That player is not online");
							}else{
								if(targetPlayer.hasPermission("frisk.exempt")){
									sender.sendMessage(ChatColor.RED + "FRISKING FAILED: That player may not be frisked!");
								}else{
									if(targetPlayer != sender){
										Player senderSender = (Player)sender;
										if (senderSender.getLocation().distance(targetPlayer.getLocation()) <= 10) {
											if(hasItem(targetPlayer, Material.INK_SACK, (short) 3) || hasItem(targetPlayer, Material.INK_SACK, (short) 2) || targetPlayer.getInventory().contains(Material.PUMPKIN_SEEDS) || targetPlayer.getInventory().contains(Material.MELON_SEEDS) || targetPlayer.getInventory().contains(Material.WHEAT) || targetPlayer.getInventory().contains(Material.SUGAR) || targetPlayer.getInventory().contains(Material.NETHER_STALK)){
												targetPlayer.teleport(new Location(Bukkit.getWorld("world"), -569, 75, -702));
												sender.sendMessage(ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " had drugs! You have recieved" + ChatColor.GREEN + " $1000!");
												EconomyResponse r = econ.depositPlayer(sender.getName(), 1000.0);
												targetPlayer.sendMessage(ChatColor.BLUE + sender.getName() + ChatColor.WHITE + " caught you with drugs!");
												Bukkit.broadcastMessage(ChatColor.BLUE + sender.getName() + ChatColor.WHITE + " caught " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " with drugs! he/she was sent to jail!");
												Bukkit.broadcastMessage(ChatColor.BLUE + sender.getName() + ChatColor.WHITE + " recieved $1000 for jailing " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + "!");
											}else if(targetPlayer.getInventory().contains(Material.PUMPKIN) || targetPlayer.getInventory().contains(Material.SUGAR_CANE)){
												targetPlayer.teleport(new Location(Bukkit.getWorld("world"), -569, 75, -702));
												sender.sendMessage(ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + "That player had paraphernalia! You have recieved" + ChatColor.GREEN + " $500!");
												EconomyResponse r = econ.depositPlayer(sender.getName(), 500.0);
												targetPlayer.sendMessage(ChatColor.BLUE + sender.getName() + ChatColor.WHITE + " caught you with paraphernalia!");
												Bukkit.broadcastMessage(ChatColor.BLUE + sender.getName() + ChatColor.WHITE + " caught " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + " with paraphernalia! he/she was sent to jail!");
												Bukkit.broadcastMessage(ChatColor.BLUE + sender.getName() + ChatColor.WHITE + " recieved" + ChatColor.GREEN + " $500" + ChatColor.WHITE + " for jailing " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + "!");
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
						sender.sendMessage(ChatColor.RED + "FRISKING FAILED! Frisk is not enabled");
					}
				}else{
					getLogger().info("You must be a player to use that command!");
				}
			}else if(args.length > 1){
				String friskoneonly = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("friskoneonly"));
				sender.sendMessage(friskoneonly);
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
		return false;
	}
	
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
	
	public void loadConfiguration(){
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}