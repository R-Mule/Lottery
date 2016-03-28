package com.mulescraft.lottery;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitScheduler;

public class LotteryTimer {
	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	Lottery lotto;
	int min2Run;
	int ticks2Run;
	int lotteryTaskId;
	int timerTaskId;
	int cntDownTimerTaskId;
	int secondCntr;//how many SECONDS until lottery ends.  
	TicketManager tman;
	
	LotteryTimer(Lottery lotto,int min2Run){
		this.lotto = lotto;
		this.ticks2Run=min2Run*1200;
		secondCntr = min2Run*60;
		this.min2Run = min2Run;
		runLotteryTimer();
		startCountdownTimer();
		lotto.isActive=true;
		tman = new TicketManager(this.lotto);
	}
	
	private void runLotteryTimer(){
		
		lotteryTaskId = scheduler.scheduleSyncRepeatingTask(lotto, new Runnable() {
			@Override
			public void run() {
				
				// Do something
				tman.lotteryEnded();
				//Bukkit.broadcastMessage("BLASTOFF!");
				
			}//end run
		}, ticks2Run, ticks2Run);//repeat every  min2Run minutes. Repeating every min2Run minutes.
	}
	
	private void startCountdownTimer(){
		
		cntDownTimerTaskId = scheduler.scheduleSyncRepeatingTask(lotto, new Runnable() {
			@Override
			public void run() {
				
				// Do something
				//Bukkit.broadcastMessage(Integer.toString(secondCntr));
				if(secondCntr==0){
					secondCntr=min2Run*60;
				}
				secondCntr--;
			}//end run
		}, 0L, 20);//DELAY FIRST REPEAT SECOND
	}//end startCountDownTimer
	
	public void stopLottery(){
	    scheduler.cancelTask(lotteryTaskId);
	    scheduler.cancelTask(cntDownTimerTaskId);
	    lotto.isActive= false;
	}//end stopLottery()
	
	public void printRemainingTime(CommandSender sender){
		int days=0;
		int hours=0;
		int minutes=0;
		int seconds=0;
		int remaining=secondCntr;
		
		if(remaining>=86400){//if there is at least a day left.
			days = remaining/86400;
			remaining = remaining - days*86400; 
		}
		
		if(remaining>=3600){//if there is at least an hour left.
			hours = remaining/3600;
			remaining = remaining - hours*3600;
		}
		if(remaining>=60){
			minutes = remaining/60;
			remaining = remaining - minutes*60;
		}
		if(remaining<60){
			seconds = remaining;
		}
		
		//Begin Print!
		if(days!=0){
			sender.sendMessage(lotto.subColors("&2Time Until Lottery End: &c"+ days+"&2D&c "+hours+"&2H&c "+minutes+"&2M&c "+seconds+"&2S"));
		}else if(hours!=0){
			sender.sendMessage(lotto.subColors("&2Time Until Lottery End: &c"+hours+"&2H&c "+minutes+"&2M&c "+seconds+"&2S"));
		}
		else if(minutes!=0){
			sender.sendMessage(lotto.subColors("&2Time Until Lottery End: &c"+minutes+"&2M&c "+seconds+"&2S"));
		}else if(seconds!=0){
			sender.sendMessage(lotto.subColors("&2Time Until Lottery End: &c"+seconds+"&2S"));
		}
	}//end printRemainingTime()
}//end class LotteryTimer
