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
	
	//Command Menu Messages
	public String commandMenuTitleMsg="COMMAND_MENU_TITLE_MSG";
	public String commandMenuRangeMsg="COMMAND_MENU_RANGE_MSG";
	public String commandMenuStopMsg="COMMAND_MENU_STOP_MSG";
	public String commandMenuStartMsg="COMMAND_MENU_START_MSG";
	public String commandMenuBuyMsg="COMMAND_MENU_BUY_MSG";
	public String commandMenuRefundMsg="COMMAND_MENU_REFUND_MSG";
	public String commandMenuTimeMsg="COMMAND_MENU_TIME_MSG";
	public String commandMenuStatsMsg="COMMAND_MENU_STATS_MSG";
	public String commandMenuHistoryMsg="COMMAND_MENU_HISTORY_MSG";
	public String commandMenuCurrentMsg="COMMAND_MENU_CURRENT_MSG";
	
	//Time Remaining Messages
	public String daysHrsMinsSecRemain="DAYS_HRS_MINS_SEC_REMAIN_MSG";
	public String hrsMinsSecRemain="HRS_MINS_SEC_REMAIN_MSG";
	public String minsSecRemain="MINS_SEC_REMAIN_MSG";
	public String secRemain="SEC_REMAIN_MSG";
	
	//Player Stats Messages
	public String pStatsTotalWinsMsg="PLAYER_STATS_TOTAL_WINS_MSG";
	public String pStatsTotalLossesMsg="PLAYER_STATS_TOTAL_LOSSES_MSG";
	public String pStatsBiggestWinMsg="PLAYER_STATS_BIGGEST_WIN_MSG";
	public String pStatsBiggestLossMsg="PLAYER_STATS_BIGGEST_LOSS_MSG";
	public String pStatsTotalAmtWonMsg="PLAYER_STATS_TOTAL_AMT_WON_MSG";
	public String pStatsTotalAmtLostMsg="PLAYER_STATS_TOTAL_AMT_LOST_MSG";

	//Server Stats Messages
	public String sStatsTotalWon="SERVER_STATS_TOTAL_WON_MSG";
	public String sStatsTotalLost="SERVER_STATS_TOTAL_LOST_MSG";
	public String sStatsBiggestWin="SERVER_STATS_BIGGEST_WIN_MSG";
	public String sStatsBiggestLoss="SERVER_STATS_BIGGEST_LOSS_MSG";
	public String sStatsMostWins="SERVER_STATS_MOST_WINS_MSG";
	public String sStatsMostLosses="SERVER_STATS_MOST_LOSSES_MSG";
	
	//current Active Tickets Messages
	public String currentTicketsHeader="CURRENT_TICKETS_HEADER";
	public String currentTicketsContent="CURRENT_TICKETS_CONTENT";
	
	//History Messages
	public String historyHeader="HISTORY_HEADER";
	public String historyWinnersTrue="HISTORY_WINNERS_TRUE";
	public String historyWinnersFalse="HISTORY_WINNERS_FALSE";
	
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
		getConfig().addDefault(winningsAmplifier,8.0);
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
		
		//comand menu messages
		getConfig().addDefault(commandMenuTitleMsg, "&2~~&6Lottery Commands&2~~");
		getConfig().addDefault(commandMenuRangeMsg, "&2Lucky Number Range:&6 1-%range%");
		getConfig().addDefault(commandMenuStartMsg, "&f/lottery start &2: starts a lottery forcefully.");
		getConfig().addDefault(commandMenuStopMsg, "&f/lottery stop &2: stops the lottery rewarding now.");
		getConfig().addDefault(commandMenuBuyMsg, "&f/lottery buy <number> <$> &2: buys <number> with <$> gambled Number Range: 1-%range%");
		getConfig().addDefault(commandMenuRefundMsg, "&f/lottery refund <luckyNumber> &2: returns your money, and removes your bet for <luckyNumber>");
		getConfig().addDefault(commandMenuTimeMsg, "&f/lottery time &2: shows remaining time until end of lottery");
		getConfig().addDefault(commandMenuStatsMsg, "&f/lottery stats <server/self> &2: shows categories of best all time stats");
		getConfig().addDefault(commandMenuHistoryMsg, "&f/lottery history &2: shows last %historyrange% lottery winners");
		getConfig().addDefault(commandMenuCurrentMsg, "&f/lottery current &2: shows active tickets for the current lottery");
		
		//countdown messages
		getConfig().addDefault(daysHrsMinsSecRemain, "&2Time Until Lottery End:&c %days%&2D &c%hours%&2H &c%minutes%&2M&c %seconds%&2S");
		getConfig().addDefault(hrsMinsSecRemain, "&2Time Until Lottery End:&c%hours%&2H &c%minutes%&2M&c %seconds%&2S");
		getConfig().addDefault(minsSecRemain, "&2Time Until Lottery End:&c%minutes%&2M&c %seconds%&2S");
		getConfig().addDefault(secRemain, "&2Time Until Lottery End:&c %seconds%&2S");
		
		//player Stats Messages
		getConfig().addDefault(pStatsTotalWinsMsg, "&6Your Total Wins:&2 %wins%");
		getConfig().addDefault(pStatsTotalLossesMsg, "&6Your Total Losses:&2 %losses%");
		getConfig().addDefault(pStatsBiggestWinMsg, "&6Your Biggest Win:&2 $%wins%");
		getConfig().addDefault(pStatsBiggestLossMsg, "&6Your BiggestLoss:&2 $%losses%");
		getConfig().addDefault(pStatsTotalAmtWonMsg, "&6Your Total Amount Won:&2 $%wins%");
		getConfig().addDefault(pStatsTotalAmtLostMsg, "&6Your Total Amount Lost:&2 $%losses%");
		
		//Server Stats Messages
		getConfig().addDefault(sStatsTotalWon, "&6Total Won:&2 $%wins%");
		getConfig().addDefault(sStatsTotalLost, "&6Total Lost:&2 $%losses%");
		getConfig().addDefault(sStatsBiggestWin, "&6Biggest Win All Time:&2 $%wins% &6By: &2%player%");
		getConfig().addDefault(sStatsBiggestLoss, "&6Biggest Loss All Time:&2 $%losses% &6By: &2%player%");
		getConfig().addDefault(sStatsMostWins, "&6Most Wins All Time:&2 %wins% &6By: &2%player%");
		getConfig().addDefault(sStatsMostLosses, "&6Most Losses All Time:&2 $%losses% &6By: &2%player%");		
		
		//Print Active Tickets Msg
		getConfig().addDefault(currentTicketsHeader, "&6Player &f: &bNumber &f: &2Bet &f: &aPossible Amount Won");
		getConfig().addDefault(currentTicketsContent, "&6%player% &f: &b%number% &f: &2$%bet% &f: &a%amount2win%");
		
		//Show History Msgs
		getConfig().addDefault(historyHeader, "&2Lottery Number: &6%number% &2Winning Number: &6%winnumber%");
		getConfig().addDefault(historyWinnersFalse, "&2Winners: &6None.");
		getConfig().addDefault(historyWinnersTrue, "&2Winners: ");
		
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
