package com.mulescraft.lottery;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerData {
	OfflinePlayer offPlayer;//the player who won or lost
	Player player;//the player who is playing.
	int lottoNum;//the number the player picked.
	double betAmt;//the bet amount the player made.
	int totalWins;//total times player has won lottery
	int totalLosses;//total times player has lost lottery
	double biggestWin;//largest amount ever won by player
	double biggestLoss;//largest amount ever lost by player
	double totalAmtWon;//total amount ever won by player
	double totalAmtLost;//total amount ever lost by player
	Lottery lotto;//the reference to main, for config files.
	PlayerData(Player p,Lottery lotto){
		player=p;
		this.lotto = lotto;
	}//end Constructor

	PlayerData(OfflinePlayer p,Lottery lotto){
		this.lotto = lotto;
		this.offPlayer = p;
	}//end constructor

	public int getLottoNumber(){//returns -1 if they have no number.
		return lotto.atData.getInt(player.getUniqueId().toString()+".LuckyNumber");
	}//end getNumber()


	public double getBetAmt(){
		return lotto.atData.getDouble(player.getUniqueId().toString()+".BetAmount");
	}//end getBidAmt()

	public void addWin(double amtWon){
		if(amtWon>lotto.pData.getDouble(offPlayer.getUniqueId().toString()+".BiggestWin")){//if biggest win yet. Set it!
			lotto.pData.set(offPlayer.getUniqueId().toString()+".BiggestWin", amtWon);
		}//end if biggest win yet.
		totalWins = lotto.pData.getInt(offPlayer.getUniqueId().toString()+".TotalWins");//get totalWins so we can add one.
		lotto.pData.set(offPlayer.getUniqueId().toString()+".TotalWins", totalWins+1);//add one.
		totalAmtWon = lotto.pData.getDouble(offPlayer.getUniqueId().toString()+".TotalAmountWon");//get totalAmtWon so we can add to it.
		lotto.pData.set(offPlayer.getUniqueId().toString()+".TotalAmountWon", totalAmtWon+amtWon);//add to it
		lotto.pData.save();//modified offPlayerData.yml. Save it!
	}//end addWin()

	public void addLoss(double amtLost){
		if(amtLost>lotto.pData.getDouble(offPlayer.getUniqueId().toString()+".BiggestLoss")){//if biggest loss yet. Set it!
			lotto.pData.set(offPlayer.getUniqueId().toString()+".BiggestLoss", amtLost);
		}//end if biggest win yet.
		totalLosses = lotto.pData.getInt(offPlayer.getUniqueId().toString()+".TotalLosses");//get totalLosses so we can add one.
		lotto.pData.set(offPlayer.getUniqueId().toString()+".TotalLosses", totalLosses+1);//add one.
		totalAmtLost = lotto.pData.getDouble(offPlayer.getUniqueId().toString()+".TotalAmountLost");//get totalAmtLost so we can add to it.
		lotto.pData.set(offPlayer.getUniqueId().toString()+".TotalAmountLost", totalAmtLost+amtLost);//add to it
		lotto.pData.save();//modified PlayerData.yml. Save it!
	}//end addLoss()

	public int getTotalWins(){
		if(offPlayer==null)
			offPlayer = player;
		return lotto.pData.getInt(offPlayer.getUniqueId().toString()+".TotalWins");//should return 0 if never won.
	}//end getTotalWins()

	public int getTotalLosses(){
		if(offPlayer==null)
			offPlayer = player;
		return lotto.pData.getInt(offPlayer.getUniqueId().toString()+".TotalLosses");//should return 0 if never lost.
	}//end getTotalLosses()

	public double getBiggestWin(){
		if(offPlayer==null)
			offPlayer = player;
		return lotto.pData.getDouble(offPlayer.getUniqueId().toString()+".BiggestWin");//should return 0 if never won.
	}//end getBiggestWin()

	public double getBiggestLoss(){
		if(offPlayer==null)
			offPlayer = player;
		return lotto.pData.getDouble(offPlayer.getUniqueId().toString()+".BiggestLoss");//should return 0 if never lost.
	}//end getBiggestLoss()

	public double getTotalAmtWon(){
		if(offPlayer==null)
			offPlayer = player;
		return lotto.pData.getDouble(offPlayer.getUniqueId().toString()+".TotalAmountWon");//should return 0 if never won.
	}//end getTotalAmtWon

	public double getTotalAmtLost(){
		if(offPlayer==null)
			offPlayer = player;
		return lotto.pData.getDouble(offPlayer.getUniqueId().toString()+".TotalAmountLost");//should return 0 if never lost.
	}//end getTotalAmtLost

	public void printPlayerStats(){//CANNOT BE CALLED WHEN OFFLINE PLAYER CTOR IS USED!
		player.sendMessage("Your Total Wins: "+getTotalWins());
		player.sendMessage("Your Total Losses: "+getTotalLosses());
		player.sendMessage("Your Biggest Win: $"+getBiggestWin());
		player.sendMessage("Your Biggest Loss: $"+getBiggestLoss());
		player.sendMessage("Your Total Amount Won: $"+getTotalAmtWon());
		player.sendMessage("Your Total Amount Lost: $"+getTotalAmtLost());
	}//end printPlayerStats


}//end PlayerData class
