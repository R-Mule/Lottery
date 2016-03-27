package com.mulescraft.lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class TicketManager {
	Lottery lotto;
	List<String> activeUUIDS = new ArrayList<String>();
	public TicketManager(Lottery lotto){
		this.lotto = lotto;
	}//end TicketManager Ctor.

	public boolean addTicket(Player player,int luckyNumber, double amount){
		//NEED TO SEE IF TICKET CAN BE ADDED!
		if(lotto.atData.getString(player.getUniqueId().toString()+".LuckyNumber")!=null){//there is already a ticket!
			lotto.atData.set(player.getUniqueId().toString()+".LuckyNumber",luckyNumber);
			lotto.atData.set(player.getUniqueId().toString()+".BetAmount",amount);
			activeUUIDS = lotto.atData.getStringList("Active UUIDS");//get the list before overwriting it
			activeUUIDS.add(player.getUniqueId().toString());//and uuid to it
			lotto.atData.set("Active UUIDS", activeUUIDS);//set the new list of valid UUIDS back.
			lotto.atData.save();
			return true;
		}//end if the ticket can be added.
		return false;//found no ticket
	}//end addTicket()

	public boolean refundTicket(Player player){
		//NEED TO SEE IF TICKET CAN BE REFUNDED
		if(lotto.atData.getString(player.getUniqueId().toString()+".LuckyNumber")==null){
			lotto.atData.set(player.getUniqueId().toString(), null);
			activeUUIDS =  lotto.atData.getStringList("Active UUIDS");//get the list before overwriting it
			activeUUIDS.remove(player.getUniqueId().toString());
			lotto.atData.set("Active UUIDS", activeUUIDS);//set the new list of valid UUIDS back.
			lotto.atData.save();
			return true;
		}//end if there is a ticket to refund
		return false;//found no ticket.
	}//end refundTicket()

	@SuppressWarnings("unused")//REMOVE THIS LATER.... WHEN WE KNOW THE WINNING LOTTO NUMBER
	public void lotteryEnded(){//if this is called. Then the lottery has ended. Award all tickets who won , and update stats with win/loss.
		activeUUIDS =  lotto.atData.getStringList("Active UUIDS");//get the list before overwriting it
		for(String uuid:activeUUIDS){//for all active tickets.
			UUID playerUUID = UUID.fromString(uuid);
			OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
			PlayerData pData = new PlayerData(player,lotto);
			ServerStats stats= new ServerStats(player,lotto);
			if(true){//IF LUCKY NUMBER MATCHES //HERE IS NUMBER TO COMPARE lotto.atData.get(player.getUniqueId().toString()+".LuckyNumber");
				pData.addWin(lotto.atData.getDouble(player.getUniqueId().toString()+".BetAmount"));
				stats.checkMostWins(pData.getTotalWins());
				stats.checkBiggestWin(pData.getBiggestWin());
				double amount = lotto.getConfig().getDouble("winningsAmplifier")*lotto.atData.getDouble(player.getUniqueId().toString()+".BetAmount");
				lotto.econ.depositPlayer(player, amount);//award them their money!
				//reward message! :) if the player is online to receive it.
				for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
					if(onlinePlayer.getUniqueId().equals(playerUUID)){
						onlinePlayer.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.youWonMessage).replaceAll("%amount%",Double.toString(amount))));
					}//end if same player
				}//end for if the player is online lets tell them they won!
			}else{//loser
				pData.addLoss(lotto.atData.getDouble(player.getUniqueId().toString()+".BetAmount"));
				stats.checkMostLosses(pData.getTotalLosses());
				stats.checkBiggestLoss(pData.getBiggestLoss());
				//loss message! :( If the player is online to receive it.
				for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
					if(onlinePlayer.getUniqueId().equals(playerUUID)){
						onlinePlayer.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.youLostMessage).replaceAll("%amount%",Double.toString(lotto.atData.getDouble(player.getUniqueId().toString()+".BetAmount")))));
					}//end if same player
				}//end for if the player is online lets tell them they lost.
			}//end add loss
			//now time to remove the UUID from active list!
			lotto.atData.set(player.getUniqueId().toString(), null);
		}//end for all the active UUIDS in the arraylist!
		activeUUIDS =  lotto.atData.getStringList("Active UUIDS");//get the list before overwriting it
		activeUUIDS=null;
		lotto.atData.set("Active UUIDS", activeUUIDS);//Time for new beginnings!
		lotto.atData.save();//finished doing modification

		//NEED TO ADD LOTTERY HISTORY!!! DIDN'T DO THIS YET!
	}//end lotteryEnded()


}//end TicketManager Class
