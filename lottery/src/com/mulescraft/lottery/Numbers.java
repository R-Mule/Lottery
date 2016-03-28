package com.mulescraft.lottery;

import java.util.Random;

public class Numbers {
	Lottery lotto;
	public Numbers(Lottery lotto){
		this.lotto=lotto;
	}//end RandomGenerator

	public int findRandom(int range){
		Random r= new Random();
	    int randomNum = r.nextInt((range - 1) + 1) + 1;
		return randomNum;
	}//end findRandom()

	public boolean isValidNumber(int number){
		if(number>0&&number<=lotto.getConfig().getInt(lotto.ticketRange))
			return true;
		
			return false;
	}//end isValidNumber

}//end RandomGenerator
