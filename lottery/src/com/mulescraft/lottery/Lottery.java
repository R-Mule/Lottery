package com.mulescraft.lottery;


import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Lottery extends JavaPlugin {
	
	Server server = this.getServer();
	ConsoleCommandSender console = server.getConsoleSender();
	protected CommandParser listener;
	public Economy econ = null;
	boolean before = false;
	private static final Logger log = Logger.getLogger("Minecraft");
	
	//Config Vars
	public String autoStartOpt = "Auto Start Crate Timer";
	public String lotteryRndTime = "Lottery Round Time in Minutes";
	public String winningsAmplifier = "Amount to Amplifty Winnings By";
	//locale? Might change this later, since each message is going to be custom.
	public String localeHr = "Locale.Hour";
	public String localeMin = "Locale.Minute";
	public String localeSec = "Locale.Second";
	
	//Config File with StatsAndData
	ConfigFile pData = new ConfigFile(this,"PlayerData.yml");
	ConfigFile lData = new ConfigFile(this,"LottoData.yml");
	
@Override
public void onEnable(){
	if (!setupEconomy() ) {
        log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
        getServer().getPluginManager().disablePlugin(this);
        return;
    }
	getConfig().options().header("Help Line 1\nHelp Line 2\n");
	if (getConfig().get("pingInterval") != null) {
		this.before = true;
	}
	listener = new CommandParser(this);
	this.getCommand("lottery").setExecutor(listener);
    loadDefaultConfigVars();   
	console.sendMessage(ChatColor.BLUE+"[Lottery] Config Loaded");
	
}//end onEnable()

//used to add color in chats msgs.
public String replaceVars(String message){
	
	message = ChatColor.translateAlternateColorCodes('&', message);
	return message;
}//end replaceVars()

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
}//end setupEconomy	


private void loadDefaultConfigVars(){
	getConfig().addDefault(autoStartOpt, true);
	getConfig().addDefault(lotteryRndTime, 1);
	getConfig().addDefault(winningsAmplifier,10);
	

}//end loadDefaultConfigVars




}//end Lottery Class
