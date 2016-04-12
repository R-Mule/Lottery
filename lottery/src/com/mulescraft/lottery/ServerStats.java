package com.mulescraft.lottery;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ServerStats {
	public OfflinePlayer offPlayer;
	public Player player;
	public Lottery lotto;

	public ServerStats(Player p,Lottery lotto){
		player=p;
		this.lotto = lotto;
	}//end Constructor
	public ServerStats(OfflinePlayer p,Lottery lotto){
		offPlayer=p;
		this.lotto = lotto;
	}//end Constructor

	//DO SET FUNCTIONS FOR ALL TIME BESTS! COMPARES!
	public void checkBiggestWin(double amount){
		double biggestWin = lotto.lhData.getDouble(lotto.serverStats+".BiggestWin.Amount");
		if(biggestWin<amount){
			lotto.lhData.set(lotto.serverStats+".BiggestWin.Amount",amount);
			lotto.lhData.set(lotto.serverStats+".BiggestWin.Player",offPlayer.getName());
			lotto.lhData.save();
		}//end update new biggest winner
	}//end checkBiggestWin()

	public void checkBiggestLoss(double amount){
		double biggestLoss = lotto.lhData.getDouble(lotto.serverStats+".BiggestLoss.Amount");
		if(biggestLoss<amount){
			lotto.lhData.set(lotto.serverStats+".BiggestLoss.Amount",amount);
			lotto.lhData.set(lotto.serverStats+".BiggestLoss.Player",offPlayer.getName());
			lotto.lhData.save();
		}//end update new biggest loss
	}//end checkBiggestLoss()

	public void addWin(double amount){
		double currentAmount = lotto.lhData.getDouble(lotto.serverStats+".TotalWon");
		lotto.lhData.set(lotto.serverStats+".TotalWon", currentAmount+amount);
		lotto.lhData.save();
	}//end addWin()

	public void addLoss(double amount){
		double currentAmount = lotto.lhData.getDouble(lotto.serverStats+".TotalLost");
		lotto.lhData.set(lotto.serverStats+".TotalLost", currentAmount+amount);
		lotto.lhData.save();
	}//end addLoss()

	public void checkMostWins(int playerTotalWins){
		double mostWins = lotto.lhData.getInt(lotto.serverStats+".MostWins.Number");
		if(mostWins<playerTotalWins){
			lotto.lhData.set(lotto.serverStats+".MostWins.Number",playerTotalWins);
			lotto.lhData.set(lotto.serverStats+".MostWins.Player",offPlayer.getName());
			lotto.lhData.save();
		}//end update new most wins
	}//end checkMostWins

	public void checkMostLosses(int playerTotalLosses){
		double mostLosses = lotto.lhData.getInt(lotto.serverStats+".MostLosses.Number");
		if(mostLosses<playerTotalLosses){
			lotto.lhData.set(lotto.serverStats+".MostLosses.Number",playerTotalLosses);
			lotto.lhData.set(lotto.serverStats+".MostLosses.Player",offPlayer.getName());
			lotto.lhData.save();
		}//end update new most losses
	}//end checkMostLosses()

	public void printServerStats(){//cannot be called when OFFLINEPLAYER is used!
		//Total Won and Total Lost
		if(lotto.lhData.getDouble(lotto.serverStats+".TotalWon")==0){
			player.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.sStatsTotalWon).replaceAll("%wins%", "0")));
		}else{
			player.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.sStatsTotalWon).replaceAll("%wins%", Double.toString(lotto.lhData.getDouble(lotto.serverStats+".TotalWon")))));
		}
		if(lotto.lhData.getDouble(lotto.serverStats+".TotalLost")==0){
			player.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.sStatsTotalLost).replaceAll("%losses%","0")));
		}else{
			player.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.sStatsTotalLost).replaceAll("%losses%", Double.toString(lotto.lhData.getDouble(lotto.serverStats+".TotalLost")))));
		}
		
		String message;
		//BiggestWin
		if(lotto.lhData.getDouble(lotto.serverStats+".BiggestWin.Amount")==0){
		    message = lotto.getConfig().getString(lotto.sStatsBiggestWin).replaceAll("%wins%", "0");
			player.sendMessage(lotto.subColors(message.replaceAll("%player%", "NONE")));
		}else{
		    message = lotto.getConfig().getString(lotto.sStatsBiggestWin).replaceAll("%wins%", Double.toString(lotto.lhData.getDouble(lotto.serverStats+".BiggestWin.Amount")));
			player.sendMessage(lotto.subColors(message.replaceAll("%player%", lotto.lhData.getString(lotto.serverStats+".BiggestWin.Player"))));
		}

		//BiggestLoss
		if(lotto.lhData.getDouble(lotto.serverStats+".BiggestLoss.Amount")==0){
			message = lotto.getConfig().getString(lotto.sStatsBiggestLoss).replaceAll("%losses%", "0");
			player.sendMessage(lotto.subColors(message.replaceAll("%player%", "NONE")));
		}else{
			message = lotto.getConfig().getString(lotto.sStatsBiggestLoss).replaceAll("%losses%", Double.toString(lotto.lhData.getDouble(lotto.serverStats+".BiggestLoss.Amount")));
			player.sendMessage(lotto.subColors(message.replaceAll("%player%", lotto.lhData.getString(lotto.serverStats+".BiggestLoss.Player"))));
		}
		
		//Most Wins
		if(lotto.lhData.getDouble(lotto.serverStats+".MostWins.Number")!=0){
		message = lotto.getConfig().getString(lotto.sStatsMostWins).replaceAll("%wins%", Double.toString(lotto.lhData.getDouble(lotto.serverStats+".MostWins.Number")));
		player.sendMessage(lotto.subColors(message.replaceAll("%player%", lotto.lhData.getString(lotto.serverStats+".MostWins.Player"))));
		}else{
			message = lotto.getConfig().getString(lotto.sStatsMostWins).replaceAll("%wins%", "0");
			player.sendMessage(lotto.subColors(message.replaceAll("%player%", "NONE")));
		}
		//Most Losses
		if(lotto.lhData.getDouble(lotto.serverStats+".MostWins.Number")!=0){
		message = lotto.getConfig().getString(lotto.sStatsMostLosses).replaceAll("%losses%", Double.toString(lotto.lhData.getDouble(lotto.serverStats+".MostLosses.Number")));
		player.sendMessage(lotto.subColors(message.replaceAll("%player%", lotto.lhData.getString(lotto.serverStats+".MostLosses.Player"))));
		}else{
			message = lotto.getConfig().getString(lotto.sStatsMostLosses).replaceAll("%losses%", "0");
			player.sendMessage(lotto.subColors(message.replaceAll("%player%","NONE")));
		}
	}//end printServerStats

}//end class ServerStats
