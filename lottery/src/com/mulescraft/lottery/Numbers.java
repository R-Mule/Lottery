package com.mulescraft.lottery;

import java.util.Random;

public class Numbers {
	public Numbers(){
		
	}//end RandomGenerator
	
	private int findRandom(int range){
		Random r= new Random();
		int i = r.nextInt(range+1);
		return i;
	}//end findRandom()
}//end RandomGenerator
