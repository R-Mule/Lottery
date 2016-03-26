package com.mulescraft.lottery;

import java.util.Random;

public class Numbers {
	Lottery lotto;
	public Numbers(Lottery lotto){
		this.lotto=lotto;
	}//end RandomGenerator
	
	private int findRandom(int range){
		Random r= new Random();
		int i = r.nextInt(range+1);
		return i;
	}//end findRandom()
	
	public boolean isValidNumber(int number){
		
		return true;
	}//end isValidNumber
	
}//end RandomGenerator
