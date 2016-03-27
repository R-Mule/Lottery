package com.mulescraft.lottery;

import org.bukkit.entity.Player;

public class ServerStats {
	public Player player;
	public Lottery lotto;
	
	ServerStats(Player p,Lottery lotto){
		player=p;
		this.lotto = lotto;
	}//end Constructor
	//DO SET FUNCTIONS FOR ALL TIME BESTS! COMPARES!
	public void checkBiggestWin(double amount){
		double biggestWin = lotto.lhData.getDouble(lotto.serverStats+".BiggestWin.Amount");
		if(biggestWin<amount){
			lotto.lhData.set(lotto.serverStats+".BiggestWin.Amount",amount);
			lotto.lhData.set(lotto.serverStats+".BiggestWin.Amount",player.getName());
			lotto.lhData.save();
		}//end update new biggest winner
	}//end checkBiggestWin()
	
	public void checkBiggestLoss(double amount){
		double biggestLoss = lotto.lhData.getDouble(lotto.serverStats+".BiggestLoss.Amount");
		if(biggestLoss<amount){
			lotto.lhData.set(lotto.serverStats+".BiggestLoss.Amount",amount);
			lotto.lhData.set(lotto.serverStats+".BiggestLoss.Amount",player.getName());
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
		double mostWins = lotto.lhData.getDouble(lotto.serverStats+".MostWins.Number");
		if(mostWins<playerTotalWins){
			lotto.lhData.set(lotto.serverStats+".MostWins.Number",playerTotalWins);
			lotto.lhData.set(lotto.serverStats+".MostWins.Player",player.getName());
			lotto.lhData.save();
		}//end update new most wins
	}//end checkMostWins
	
	public void checkMostLosses(int playerTotalLosses){
		double mostLosses = lotto.lhData.getDouble(lotto.serverStats+".MostLosses.Number");
		if(mostLosses<playerTotalLosses){
			lotto.lhData.set(lotto.serverStats+".MostLosses.Number",playerTotalLosses);
			lotto.lhData.set(lotto.serverStats+".MostLosses.Player",player.getName());
			lotto.lhData.save();
		}//end update new most losses
	}//end checkMostLosses()
	
	public void printServerStats(){
		player.sendMessage("Total Won: "+lotto.lhData.getDouble(lotto.serverStats+".TotalWon"));//All server money ever won.
		player.sendMessage("Total Lost: "+lotto.lhData.getDouble(lotto.serverStats+".TotalLost"));//All money ever lost.
		player.sendMessage("Biggest Win All Time: "+lotto.lhData.getDouble(lotto.serverStats+".BiggestWin.Amount")+" By: "+ lotto.lhData.getString(lotto.serverStats+".BiggestWin.Player"));//should print 0 if never a winner.
		player.sendMessage("Biggest Loss All Time: "+lotto.lhData.getDouble(lotto.serverStats+".BiggestLoss.Amount")+" By: "+lotto.lhData.getString(lotto.serverStats+".BiggestLoss.Player"));//should print 0 if never a loser.
		player.sendMessage("Most Wins All Time: "+lotto.lhData.getDouble(lotto.serverStats+".MostWins.Number")+" By: "+ lotto.lhData.getString(lotto.serverStats+".MostWins.Player"));//should print 0 if never a winner.
		player.sendMessage("Most Losses All Time: "+lotto.lhData.getDouble(lotto.serverStats+".MostLosses.Number")+" By: "+lotto.lhData.getString(lotto.serverStats+".MostLosses.Player"));//should print 0 if never a loss.
	}//end printServerStats
	
	public void printHistory(){//this MIGHT print an X amount of last lottery outcomes. When read from config.
		
	}//end printHistory()
}//end class ServerStats
