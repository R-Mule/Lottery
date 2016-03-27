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
	public void printServerStats(){
		player.sendMessage("Total Won: "+lotto.lhData.getString(lotto.serverStats+".TotalWon"));//All server money ever won.
		player.sendMessage("Total Lost: "+lotto.lhData.getString(lotto.serverStats+".TotalLost"));//All money ever lost.
		player.sendMessage("Biggest Win All Time: "+lotto.lhData.getString(lotto.serverStats+".BiggestWin.Amount")+" By: "+ lotto.lhData.getString(lotto.serverStats+".BiggestWin.Player"));//should print 0 if never a winner.//SHOULD STORE PLAYER NAME AND AMOUNT!
		player.sendMessage("Biggest Loss All Time: "+lotto.lhData.getString(lotto.serverStats+".BiggestLoss.Amount")+" By: "+lotto.lhData.getString(lotto.serverStats+".BiggestLoss.Player"));//should print 0 if never a loser.//SHOULD STORE PLAYER NAME AND AMOUNT!
	}//end printServerStats
	
	public void printHistory(){//this MIGHT print an X amount of last lottery outcomes. When read from config.
		
	}//end printHistory()
}//end class ServerStats
