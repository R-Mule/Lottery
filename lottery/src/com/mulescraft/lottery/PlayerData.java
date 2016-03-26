package com.mulescraft.lottery;

import org.bukkit.entity.Player;

public class PlayerData {
	Player player;//the player who is playing.
	int lottoNum;//the number the player picked.
	double bidAmt;//the bid amount the player made.
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
	
	public void setLottoNum(int lotNum){
		lottoNum = lotNum;
	}//end setLottoNum
	
	public int getLottoNumber(){//returns -1 if they have no number.
		return -1;//add content later
	}//end getNumber()
	
	public void setBidAmt(double bid){
		bidAmt=bid;
	}//end setBidAmt()
	
	public double getBidAmt(){
		return bidAmt;
	}//end getBidAmt()
	
	public void addWin(double amtWon){
		if(amtWon>lotto.pData.getDouble(player.getUniqueId().toString()+".BiggestWin")){//if biggest win yet. Set it!
			lotto.pData.set(player.getUniqueId().toString()+".BiggestWin", amtWon);
		}//end if biggest win yet.
		totalWins = lotto.pData.getInt(player.getUniqueId().toString()+".TotalWins");//get totalWins so we can add one.
		lotto.pData.set(player.getUniqueId().toString()+".TotalWins", totalWins+1);//add one.
		totalAmtWon = lotto.pData.getInt(player.getUniqueId().toString()+".TotalAmountWon");//get totalAmtWon so we can add to it.
		lotto.pData.set(player.getUniqueId().toString()+".TotalAmountWon", totalAmtWon+amtWon);//add to it
		lotto.pData.save();//modified PlayerData.yml. Save it!
	}//end addWin()
	
	public void addLoss(double amtLost){
		if(amtLost>lotto.pData.getDouble(player.getUniqueId().toString()+".BiggestLoss")){//if biggest loss yet. Set it!
			lotto.pData.set(player.getUniqueId().toString()+".BiggestLoss", amtLost);
		}//end if biggest win yet.
		totalLosses = lotto.pData.getInt(player.getUniqueId().toString()+".TotalLosses");//get totalLosses so we can add one.
		lotto.pData.set(player.getUniqueId().toString()+".TotalLosses", totalLosses+1);//add one.
		totalAmtLost = lotto.pData.getInt(player.getUniqueId().toString()+".TotalAmountLost");//get totalAmtLost so we can add to it.
		lotto.pData.set(player.getUniqueId().toString()+".TotalAmountLost", totalAmtLost+amtLost);//add to it
		lotto.pData.save();//modified PlayerData.yml. Save it!
	}//end addLoss()
	
	public int getTotalWins(){
		return lotto.pData.getInt(player.getUniqueId().toString()+".TotalWins");//should return 0 if never won.
	}//end getTotalWins()
	
	public int getTotalLosses(){
		return lotto.pData.getInt(player.getUniqueId().toString()+".TotalLosses");//should return 0 if never lost.
	}//end getTotalLosses()
	
	public double getBiggestWin(){
		return lotto.pData.getDouble(player.getUniqueId().toString()+".BiggestWin");//should return 0 if never won.
	}//end getBiggestWin()
	
	public double getBiggestLoss(){
		return lotto.pData.getDouble(player.getUniqueId().toString()+".BiggestLoss");//should return 0 if never lost.
	}//end getBiggestLoss()
	
	public double getTotalAmtWon(){
		return lotto.pData.getDouble(player.getUniqueId().toString()+".TotalAmountWon");//should return 0 if never won.
	}//end getTotalAmtWon
	
	public double getTotalAmtLost(){
		return lotto.pData.getDouble(player.getUniqueId().toString()+".TotalAmountLost");//should return 0 if never lost.
	}//end getTotalAmtLost
	
	
}//end PlayerData class
