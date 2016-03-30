package com.mulescraft.lottery;


import java.util.logging.Logger;

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
	boolean isActive = false;//wether or not the lotto is currently running.

	//Config Vars
	public String autoStartOpt = "Auto Start Lottery Timer";
	public String lotteryRndTime = "Lottery Round Time in Minutes";
	public String winningsAmplifier = "Amount to Amplifty Winnings By";
	public String ticketRange = "TicketMaxPickValue";
	public String serverStats = "ServerAllTimeStats";
	public String historyRange ="History Range to Show on Command History";
	public String announceLotteryStart = "Announce Lottery Start";
	
	//Config Messages
	public String notEnoughMoneyMsg = "NOT_ENOUGH_MONEY_MESSAGE";
	public String noBet2RefundMsg = "NO_BET_TO_REFUND_MESSAGE";
	public String refundBeforeBetAgainMsg = "REFUND_BEFORE_BET_AGAIN_MESSAGE";
	public String betRefundedMsg = "BET_REFUNDED_MESSAGE";
	public String betAcceptedMsg = "BET_ACCEPTED_MESSAGE";
	public String noActiveLotteryMsg = "NO_ACTIVE_LOTTERY_MESSAGE";
	public String missingSelfServerMsg = "MISSING_SELF_SERVER_MESSAGE";
	public String alreadyPlacedBetMsg = "ALREADY_PLACED_BET_MESSAGE";
	public String invalidNumberArguments = "INVALID_NUMBER_ARGUMENTS";
	public String missingBuyArguments="MISSING_ARGUMENTS_FOR_BUY_COMMAND";
	public String youWonMessage="WON_LOTTERY_MESSAGE";
	public String youLostMessage = "LOST_LOTTERY_MESSAGE";
	public String notStrtAlrdyStrtdMsg="NOT_START_ALREADY_STARTED_MSG";
	public String notStpdAlrdyStpdMsg="NOT_STOP_ALREADY_STOPPED_MSG";
	public String lotteryEndServerMsg="LOTTERY_ENDED_SERVER_MSG";
	public String noHistoryMsg="NO_HISTORY_TO_SHOW_MSG";
	public String showHistoryMsg="SHOW_HISTORY_MSG";
	public String notValidNumMsg="NOT_VALID_LOTTO_NUMBER_MSG";
	public String activeTicketsMsg = "ACTIVE_TICKETS_MSG";
	public String noActiveTicketsMsg = "NO_ACTIVE_TICKETS_MSG";
	public String startMsg="LOTTERY_START_MESSAGE";
	
	public LotteryTimer lotTime;
	//locale? Might change this later, since each message is going to be custom.
	public String localeHr = "Locale.Hour";
	public String localeMin = "Locale.Minute";
	public String localeSec = "Locale.Second";

	//Config File with StatsAndData
	ConfigFile pData = new ConfigFile(this,"PlayerData.yml");
	ConfigFile lhData = new ConfigFile(this,"LottoHistory.yml");
	ConfigFile atData = new ConfigFile(this,"ActiveTickets.yml");

	@Override
	public void onEnable(){
		if (!setupEconomy() ) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		if (getConfig().get("pingInterval") != null) {
			this.before = true;
		}
		Documentation doc = new Documentation();
		
		getConfig().options().header(doc.getDocumentation());
		listener = new CommandParser(this);
		this.getCommand("lottery").setExecutor(listener);
		loadDefaultConfigVars();   
		console.sendMessage(ChatColor.BLUE+"[Lottery] Config Loaded");
	    lotTime = new LotteryTimer(this,getConfig().getInt(lotteryRndTime));
	}//end onEnable()

	//used to add color in chats msgs.
	public String subColors(String message){

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

		//vars used for lottery
		getConfig().addDefault(autoStartOpt, true);
		getConfig().addDefault(lotteryRndTime, 60);
		getConfig().addDefault(winningsAmplifier,10.0);
		getConfig().addDefault(ticketRange, 9);
		getConfig().addDefault(historyRange, 5);
		getConfig().addDefault(announceLotteryStart, true);
		//custom ChatMessages
		getConfig().addDefault(notEnoughMoneyMsg,"&2Sorry, but you do not have enough money to place that bet.");
		getConfig().addDefault(alreadyPlacedBetMsg,"&2Sorry, your bet for this lottery has already been placed.");
		getConfig().addDefault(noBet2RefundMsg,"&2You currently have no active bets to be refunded.");
		getConfig().addDefault(refundBeforeBetAgainMsg,"&2You must run &6/lottery refund&2 before betting again.");
		getConfig().addDefault(betRefundedMsg, "&2Your bet of %amountbet% on number %luckynumber% has been refunded.");
		getConfig().addDefault(betAcceptedMsg,"&2Your bet of %amountbet% on number %luckynumber% has been accepted.");
		getConfig().addDefault(noActiveLotteryMsg, "&2There is not a lottery running to bet, get refunds, or time remaining on currently.");
		getConfig().addDefault(missingSelfServerMsg, "&2You need to use &6/lottery stats self &2or &6/lottery stats server &2.");
		getConfig().addDefault(invalidNumberArguments,"&2The values entered are not numbers. Please use &6/lottery buy <luckynumber> <amount> &2with no <>");
		getConfig().addDefault(missingBuyArguments, "&2Command Usage: &6/buy <luckynumber> <amount>");
		getConfig().addDefault(youWonMessage, "&2CONGRATULATIONS! YOU JUST WON $%amount% AT THE LOTTERY!");
		getConfig().addDefault(youLostMessage, "&2Sorry, your ticket did not win, you lost $%amount% at the lottery.");
		getConfig().addDefault(notStrtAlrdyStrtdMsg, "&2Cannot start the lottery. It is already running.");
		getConfig().addDefault(notStpdAlrdyStpdMsg, "&2Cannot stop the lottery. It is already stopped.");
		getConfig().addDefault(lotteryEndServerMsg, "&2The Lottery has ended. The winning number was: %winningnumber%");
		getConfig().addDefault(showHistoryMsg, "&2%number% most recent Lottery Results");
		getConfig().addDefault(noHistoryMsg, "&2There are no lottery results recorded yet.");
		getConfig().addDefault(notValidNumMsg, "&2Please enter a number between 1 and %range%");
		getConfig().addDefault(activeTicketsMsg, "&2~Current Tickets For Lottery Number: %number% ~");
		getConfig().addDefault(noActiveTicketsMsg, "&2There are currently no active tickets to display.");
		getConfig().addDefault(startMsg, "&2The Lottery has started! Use &6/lottery&2 to play!");
		getConfig().options().copyDefaults(true);
		saveConfig();
	}//end loadDefaultConfigVars

	@Override
	public void onDisable(){
		//saveConfig();
	}//end onDisable()



}//end Lottery Class
