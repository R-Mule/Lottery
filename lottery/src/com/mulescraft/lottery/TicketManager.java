package com.mulescraft.lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketManager {
	Lottery lotto;
	List<String> activeUUIDS = new ArrayList<String>();
	int winningNum=0;
	int lotteryNum=0;

	public TicketManager(Lottery lotto){
		this.lotto = lotto;
	}//end TicketManager Ctor.

	public boolean addTicket(Player player,int luckyNumber, double amount){
		int totalActTkts = lotto.atData.getInt(player.getUniqueId().toString()+".TotalActiveTickets");
		//NEED TO SEE IF TICKET CAN BE ADDED!
		for(int i=1;i<=totalActTkts;i++){
			if(lotto.atData.getInt(player.getUniqueId().toString()+"."+i+".LuckyNumber")==luckyNumber){
				return false; //CANNOT ADD THE TICKET. IT ALREADY EXISTS!
			}
		}
			totalActTkts++;
			lotto.atData.set(player.getUniqueId().toString()+"."+ (totalActTkts)+".LuckyNumber",luckyNumber);
			lotto.atData.set(player.getUniqueId().toString()+"."+ (totalActTkts)+".BetAmount",amount);
			activeUUIDS = lotto.atData.getStringList("Active UUIDS");//get the list before overwriting it
			if(totalActTkts==1){//we only needed to add the UUID one time.
				activeUUIDS.add(player.getUniqueId().toString());//and uuid to it
				lotto.atData.set("Active UUIDS", activeUUIDS);//set the new list of valid UUIDS back.
			}
			lotto.atData.set(player.getUniqueId().toString()+".TotalActiveTickets",totalActTkts);//Added new ticket.
			lotto.atData.save();
			lotto.atData.reload();
		return true;//Ticket Added!
	}//end addTicket()

	public boolean refundTicket(Player player){
		//NEED TO SEE IF TICKET CAN BE REFUNDED
		/*
		if(lotto.atData.getString(player.getUniqueId().toString()+".LuckyNumber")!=null){
			PlayerData pdata = new PlayerData(player,lotto);
			String message = lotto.getConfig().getString(lotto.betRefundedMsg);//get the betRefundedMessage from config.
			message = lotto.subColors(message);//sub colors in from config.
			message = replaceVars(pdata.getLottoNumber(),pdata.getBetAmt(),message);//replace %% messages with amounts.
			player.sendMessage(message);//ticket refunded message
			lotto.econ.depositPlayer(player, pdata.getBetAmt());//Actually REFUND their money....
			lotto.atData.set(player.getUniqueId().toString(), null);
			activeUUIDS =  lotto.atData.getStringList("Active UUIDS");//get the list before overwriting it
			activeUUIDS.remove(player.getUniqueId().toString());
			lotto.atData.set("Active UUIDS", activeUUIDS);//set the new list of valid UUIDS back.
			lotto.atData.save();
			return true;
		}//end if there is a ticket to refund
		return false;//found no ticket
		*/
		return false;//this needs rewritten
	}//end refundTicket()

	
	public void lotteryEnded(){//if this is called. Then the lottery has ended. Award all tickets who won , and update stats with win/loss.
		//Time to get a random number!
		List<String> winners = new ArrayList<String>();
		Numbers rand = new Numbers(lotto);
		winningNum = rand.findRandom(lotto.getConfig().getInt(lotto.ticketRange));
		lotteryNum = lotto.lhData.getInt("TotalLotteriesPlayed");
		lotteryNum++;//add one since this one just ended.
		lotto.lhData.set("TotalLotteriesPlayed", lotteryNum);//store it!
		lotto.lhData.save();//attempt to fix the missing lottery win.
		lotto.lhData.reload();
		lotto.atData.save();
		lotto.atData.reload();
		lotto.mqData.save();
		lotto.mqData.reload();
		Bukkit.broadcastMessage(lotto.subColors(lotto.getConfig().getString(lotto.lotteryEndServerMsg).replaceAll("%winningnumber%", Integer.toString(winningNum))));//winning number server msg
		activeUUIDS =  lotto.atData.getStringList("Active UUIDS");//get the list before overwriting it
		for(String uuid:activeUUIDS){//for all active tickets.
			UUID playerUUID = UUID.fromString(uuid);//get the UUID of the betting player
			OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);//get OfflinePlayer from the UUID
			PlayerData pData = new PlayerData(player,lotto);//send OfflinePlayer to pData
			ServerStats stats= new ServerStats(player,lotto);//send OfflinePlayer to stats
			for(int ticket=1;ticket<=lotto.atData.getInt(uuid+".TotalActiveTickets");ticket++){
				if(lotto.atData.getInt(uuid+"."+ticket+".LuckyNumber")==winningNum){//IF LUCKY NUMBER MATCHES WINNER WINNER WINNER WINNER
					winConditions(pData,stats,player,playerUUID,winners,ticket);
				}else{//LOSER LOSER LOSER LOSER
					loseConditions(pData,stats,player,playerUUID,ticket);
				}//end add loss
			}
			//now time to remove the UUID from active list!
			lotto.atData.set(uuid, null);
		}//end for all the active UUIDS in the arraylist!
		activeUUIDS =  lotto.atData.getStringList("Active UUIDS");//get the list before overwriting it
		activeUUIDS.clear();
		lotto.atData.set("Active UUIDS", activeUUIDS);//Time for new beginnings!
		lotto.atData.save();//finished doing modification
		lotto.atData.reload();
		lotto.lhData.set(lotteryNum+".WinningNumber",winningNum);
		lotto.lhData.set(lotteryNum+".Winners", winners);
		lotto.lhData.save();//save our winners!
		lotto.lhData.reload();
		winners.clear();

	}//end lotteryEnded()

	public void loseConditions(PlayerData pData,ServerStats stats, OfflinePlayer player,UUID playerUUID,int ticket){
		double amtLost = lotto.atData.getDouble(player.getUniqueId().toString()+"."+ticket+".BetAmount");
		pData.addLoss(amtLost);
		stats.checkMostLosses(pData.getTotalLosses(playerUUID));
		stats.checkBiggestLoss(pData.getBiggestLoss(playerUUID));
		stats.addLoss(amtLost);
		boolean messageSent = false;//was the person online to receive the message?
		//loss message! :( If the player is online to receive it.
		for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
			if(onlinePlayer.getUniqueId().equals(playerUUID)){
				onlinePlayer.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.youLostMessage).replaceAll("%amount%",Double.toString(lotto.atData.getDouble(player.getUniqueId().toString()+"."+ticket+".BetAmount")))));
				messageSent = true;
			}//end if same player
		}//end for if the player is online lets tell them they lost.
		if(!messageSent){
			storeMessageInQue(true,playerUUID,amtLost,lotto.atData.getInt(player.getUniqueId().toString()+"."+ticket+".LuckyNumber"),winningNum);
		}//end if message was not sent
	}//end loseConditions()

	public void winConditions(PlayerData pData,ServerStats stats, OfflinePlayer player,UUID playerUUID,List<String>winners,int ticket){
		double amtWon = lotto.atData.getDouble(player.getUniqueId().toString()+"."+ticket+".BetAmount");
		amtWon = amtWon * lotto.getConfig().getDouble(lotto.winningsAmplifier);
		pData.addWin(amtWon);//send bet amount to addWin with OfflinePlayer
		stats.checkMostWins(pData.getTotalWins(playerUUID));
		stats.checkBiggestWin(pData.getBiggestWin(playerUUID));
		stats.addWin(amtWon);
		winners.add(player.getPlayer().getDisplayName());//this will be stored in the lottery history
		lotto.econ.depositPlayer(player, amtWon);//award them their money!
		boolean messageSent = false;//was the person online to receive the message?
		//reward message! :) if the player is online to receive it.
		for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
			if(onlinePlayer.getUniqueId().equals(playerUUID)){
				onlinePlayer.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.youWonMessage).replaceAll("%amount%",Double.toString(amtWon))));
				if(lotto.getConfig().getBoolean(lotto.soundsEnabled))
					onlinePlayer.getWorld().playSound(onlinePlayer.getLocation(), Sound.valueOf(lotto.getConfig().getString(lotto.soundOnWin)),100,0);
				messageSent=true;
			}//end if same player
		}//end for if the player is online lets tell them they won!
		if(!messageSent){
			storeMessageInQue(true,playerUUID,amtWon,lotto.atData.getInt(player.getUniqueId().toString()+"."+ticket+".LuckyNumber"+".LuckyNumber"),winningNum);
		}//end if message was not sent
	}//end winConditions()
	
	public void printLotteryHistory(CommandSender sender){//uses number to print from config.
		int range = lotto.getConfig().getInt(lotto.historyRange);
		if(lotto.lhData.getInt("TotalLotteriesPlayed")!=0){
			int i=0;
			if(range>=lotto.lhData.getInt("TotalLotteriesPlayed")){//if your range is larger than or same as number of lotteries played thus far, start from 1.
				i=1;
				range = lotto.lhData.getInt("TotalLotteriesPlayed");
			}else{
				i=lotto.lhData.getInt("TotalLotteriesPlayed")-range;
			}
			
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.showHistoryMsg).replaceAll("%number%",Integer.toString(range) )));
			while(i<=lotto.lhData.getInt("TotalLotteriesPlayed")){

				sender.sendMessage("Lottery Number: "+i+" Winning Number: "+lotto.lhData.getInt(Integer.toString(i)+".WinningNumber"));
				
				List<String> winners = lotto.lhData.getStringList(Integer.toString(i)+".Winners");
				if(winners.isEmpty()){
					sender.sendMessage("Winners: None");
				}
				else{
					sender.sendMessage("Winners: ");
					for(String player:winners){//for all the winners
						sender.sendMessage(lotto.subColors(player));
					}//end for all the winners.
				}
				
				i++;
			}//end while the range

		}else{
			Bukkit.broadcastMessage(lotto.subColors(lotto.getConfig().getString(lotto.noHistoryMsg)));//There are no recorded lotteries msg.
		}//end else no histories
	}//end printLotteryHistory

	private String replaceVars(int num2Sub,double amt2Sub,String message){//this subs in the values from config %% replacements.
		message = message.replaceAll("%luckynumber%", Integer.toString(num2Sub));
		message = message.replaceAll("%amountbet%", Double.toString(amt2Sub));
		return message;
	}//end replaceVars()

	public void printActiveTickets(CommandSender sender){
		activeUUIDS =  lotto.atData.getStringList("Active UUIDS");//lets get all the active UUIDs.
		if(!activeUUIDS.isEmpty()){
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.activeTicketsMsg)).replaceAll("%number%",Integer.toString(lotto.lhData.getInt("TotalLotteriesPlayed")+1)));
			sender.sendMessage("Player : Number : Bet");
			for(String uuid : activeUUIDS){
				UUID pUUID = UUID.fromString(uuid);
				OfflinePlayer player = Bukkit.getOfflinePlayer(pUUID);
				for(int ticket=1;ticket<=lotto.atData.getInt(uuid+".TotalActiveTickets");ticket++){
					sender.sendMessage(player.getName()+" : "+lotto.atData.getInt(uuid+"."+ticket+".LuckyNumber")+" : "+lotto.atData.getDouble(uuid+"."+ticket+".BetAmount"));
				}//end for all tickets for your UUID
			}//end for active uuid find right one
		}else{
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.noActiveTicketsMsg)));
		}//end else no active lottery
	}//end printActiveTickets()
	
	public void storeMessageInQue(boolean win,UUID uuid,double amount,int yourNumber,int winningNumber){
		//I put 1 in for now, this will allow it to store multiple tickets later if needed.
		lotto.mqData.set(uuid.toString()+".1.Win", win);
		lotto.mqData.set(uuid.toString()+".1.Amount", amount);
		lotto.mqData.set(uuid.toString()+".1.LuckyNumber", yourNumber);
		lotto.mqData.set(uuid.toString()+".1.WinningNumber", winningNumber);
		lotto.mqData.save();
	}//end storeMessageInQue()
	
	public void checkPrintMessageInQue(Player player){
		if(lotto.mqData.getString(player.getUniqueId().toString())!=null){
			//send message about win or loss.
			if(lotto.mqData.getBoolean(player.getUniqueId().toString()+"1.Win")){//if they won message
				player.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.youWonMessage).replaceAll("%amount%",Double.toString(lotto.mqData.getDouble(player.getUniqueId().toString()+".1.Amount")))));
				if(lotto.getConfig().getBoolean(lotto.soundsEnabled))
				player.getWorld().playSound(player.getLocation(), Sound.valueOf(lotto.getConfig().getString(lotto.soundOnWin)) ,100,0);
			}else{//they lost.
				player.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.youLostMessage).replaceAll("%amount%",Double.toString(lotto.mqData.getDouble(player.getUniqueId().toString()+".1.Amount")))));
			}
			lotto.mqData.set(player.getUniqueId().toString(),null);
			lotto.mqData.save();
		}//end if not null 
	}//end checkPrintMessageInQue()
	
	
	public boolean isTicketAlreadyActive(){//determines if the ticket is already active or not.
		return true;
	}
}//end TicketManager Class
