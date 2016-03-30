package com.mulescraft.lottery;

import java.util.Random;

import org.bukkit.entity.Player;

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

	public boolean isValidNumber(int number,Player p){
		if(number>0&&number<=lotto.getConfig().getInt(lotto.ticketRange))
			return true;
		
		String msg = lotto.getConfig().getString(lotto.notValidNumMsg);
		String range=Integer.toString(lotto.getConfig().getInt(lotto.ticketRange));
		msg = msg.replaceAll("%range%", range);
		p.sendMessage(lotto.subColors(msg));//NOT VALID NUMBER ERROR MSG
			return false;
	}//end isValidNumber

}//end RandomGenerator
