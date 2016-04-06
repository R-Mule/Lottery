package com.mulescraft.lottery;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Lottery extends JavaPlugin implements Listener {

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
	
	//Sound Section
	public String soundsEnabled ="Sounds.Enabled"; 
	public String soundOnWin = "Sounds.SOUND_ON_WIN";
	public String soundOnBuy = "Sounds.SOUND_ON_BUY";

	//Countdown Section
	public String countDownAnnounceEnabled="Countdown.Enabled";
	public String countDownAnnounceHr = "Countdown.Hrs";
	public String countDownAnnounceMin = "Countdown.Min";
	public String countDownAnnounceSec = "Countdown.Sec";
	List<Integer> minList = new ArrayList<Integer>();
	List<Integer> hrsList = new ArrayList<Integer>();
	List<Integer> secList = new ArrayList<Integer>();
	
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
	public String alreadyActiveTicketMsg = "ALREADY_ACTIVE_TICKET_MSG";
	public String noBetOnThatNumberMsg = "NO_BET_ON_THAT_NUMBER_MSG";
	public String refundSyntaxErrorMsg = "REFUND_SYNTAX_ERROR_MSG";
	
	public LotteryTimer lotTime;
	//locale? Might change this later, since each message is going to be custom.
	public String localeHr = "Locale.Hour";
	public String localeMin = "Locale.Minute";
	public String localeSec = "Locale.Second";

	//Config File with StatsAndData
	ConfigFile pData = new ConfigFile(this,"PlayerData.yml");
	ConfigFile lhData = new ConfigFile(this,"LottoHistory.yml");
	ConfigFile atData = new ConfigFile(this,"ActiveTickets.yml");
	ConfigFile mqData = new ConfigFile(this,"MessageQue.yml");

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
		Server server = this.getServer();
		server.getPluginManager().registerEvents(this, this);
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
		
		//Sound Section
		getConfig().addDefault(soundsEnabled, true);
		getConfig().addDefault(soundOnWin, "ENTITY_FIREWORK_LARGE_BLAST");
		getConfig().addDefault(soundOnBuy, "BLOCK_NOTE_HARP");
		
		//Countdown Section
		getConfig().addDefault(countDownAnnounceEnabled, true);
		hrsList.add(1);
		minList.add(45);
		minList.add(30);
		minList.add(15);
		minList.add(5);
		minList.add(1);
		secList.add(30);
		secList.add(10);
		secList.add(9);
		secList.add(8);
		secList.add(7);
		secList.add(6);
		secList.add(5);
		secList.add(4);
		secList.add(3);
		secList.add(2);
		secList.add(1);
		getConfig().addDefault(countDownAnnounceHr, hrsList);
		getConfig().addDefault(countDownAnnounceMin, minList);
		getConfig().addDefault(countDownAnnounceSec, secList);	
		
		//custom ChatMessages
		getConfig().addDefault(notEnoughMoneyMsg,"&2Sorry, but you do not have enough money to place that bet.");
		getConfig().addDefault(alreadyPlacedBetMsg,"&2Sorry, your bet for this lottery has already been placed.");
		getConfig().addDefault(noBet2RefundMsg,"&2You currently have no active bets to be refunded.");
		getConfig().addDefault(refundBeforeBetAgainMsg,"&2You must run &6/lottery refund&2 before betting again.");
		getConfig().addDefault(betRefundedMsg, "&2Your bet of &6$%amountbet% &2on number &6%luckynumber% &2has been refunded.");
		getConfig().addDefault(betAcceptedMsg,"&2Your bet of &6$%amountbet% &2on number &6%luckynumber% &2has been accepted.");
		getConfig().addDefault(noActiveLotteryMsg, "&2There is not a lottery running to bet, get refunds, or time remaining on currently.");
		getConfig().addDefault(missingSelfServerMsg, "&2You need to use &6/lottery stats self &2or &6/lottery stats server &2.");
		getConfig().addDefault(invalidNumberArguments,"&2The values entered are not numbers. Please use &6/lottery buy <luckynumber> <amount> &2with no <>");
		getConfig().addDefault(missingBuyArguments, "&2Command Usage: &6/buy <luckynumber> <amount>");
		getConfig().addDefault(youWonMessage, "&2CONGRATULATIONS! YOU JUST WON &6$%amount% &2AT THE LOTTERY!");
		getConfig().addDefault(youLostMessage, "&2Sorry, your ticket did not win, you lost&6 $%amount% &2at the lottery.");
		getConfig().addDefault(notStrtAlrdyStrtdMsg, "&2Cannot start the lottery. It is already running.");
		getConfig().addDefault(notStpdAlrdyStpdMsg, "&2Cannot stop the lottery. It is already stopped.");
		getConfig().addDefault(lotteryEndServerMsg, "&2The Lottery has ended. The winning number was: &6%winningnumber%");
		getConfig().addDefault(showHistoryMsg, "&6%number% &2most recent Lottery Results");
		getConfig().addDefault(noHistoryMsg, "&2There are no lottery results recorded yet.");
		getConfig().addDefault(notValidNumMsg, "&2Please enter a number between&6 1 &2and&6 %range%");
		getConfig().addDefault(activeTicketsMsg, "&2~Current Tickets For Lottery Number:&6 %number% &2~");
		getConfig().addDefault(noActiveTicketsMsg, "&2There are currently no active tickets to display.");
		getConfig().addDefault(startMsg, "&2The Lottery has started! Use &6/lottery&2 to play!");
		getConfig().addDefault(alreadyActiveTicketMsg, "&2A ticket with lucky number &6%number% &2already has a bet on it from you. Try another number?");
		getConfig().addDefault(noBetOnThatNumberMsg,"&2There is currently no bet on number &6%number% &2by you.");
		getConfig().addDefault(refundSyntaxErrorMsg,"&2Please use &6/refund <luckyNumber> &2where <luckyNumber> is the number you bet on. Try &6/lottery current&2 to see active bets.");
		getConfig().options().copyDefaults(true);
		saveConfig();
	}//end loadDefaultConfigVars

	@Override
	public void onDisable(){
		//saveConfig();
	}//end onDisable()

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {//check the message que. See what we have waiting.
		TicketManager tman = new TicketManager(this);
		tman.checkPrintMessageInQue(e.getPlayer());
	}//end onPlayerJoin event

}//end Lottery Class
