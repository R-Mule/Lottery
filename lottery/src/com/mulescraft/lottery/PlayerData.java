package com.mulescraft.lottery;

import org.bukkit.entity.Player;

public class PlayerData {
	Player player;//the player who is playing.
	int lottoNum;//the number the player picked.
	double bidAmt;//the bid amount the player made.
	
	PlayerData(Player p){
		player=p;
	}//end Constructor
	
	
	public int getNumber(){//returns -1 if they have no number.
		return -1;//add content later
	}//end getNumber()
	
	public void setBidAmt(double bid){
		bidAmt=bid;
	}//end setBidAmt()
	
	public double getBidAmt(){
		return bidAmt;
	}//end getBidAmt()
	
	public void setLottoNum(int lotNum){
		lottoNum = lotNum;
	}//end setLottoNum
	
}//end PlayerData class
