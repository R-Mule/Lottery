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
		if(lotto.getConfig().getBoolean(lotto.announceLotteryStart));
		Bukkit.broadcastMessage(lotto.subColors(lotto.getConfig().getString(lotto.startMsg)));
		lotto.isActive=true;
		tman = new TicketManager(this.lotto);
	}

	private void runLotteryTimer(){

		lotteryTaskId = scheduler.scheduleSyncRepeatingTask(lotto, new Runnable() {
			@Override
			public void run() {
				lotto.atData.save();
				lotto.atData.reload();
				lotto.lhData.save();
				lotto.lhData.reload();
				lotto.mqData.save();
				lotto.mqData.reload();
				lotto.pData.save();
				lotto.pData.reload();
				tman.lotteryEnded();

			}//end run
		}, ticks2Run, ticks2Run);//repeat every  min2Run minutes. Repeating every min2Run minutes.
	}

	private void startCountdownTimer(){

		cntDownTimerTaskId = scheduler.scheduleSyncRepeatingTask(lotto, new Runnable() {
			@Override
			public void run() {
				if(secondCntr==0){
					secondCntr=min2Run*60;
				}
				if(lotto.getConfig().getBoolean(lotto.countDownAnnounceEnabled)){
					if((lotto.getConfig().getList(lotto.countDownAnnounceHr).contains(secondCntr/3600)&&secondCntr%3600==0)||(lotto.getConfig().getList(lotto.countDownAnnounceMin).contains(secondCntr/60)&&secondCntr%60==0)||lotto.getConfig().getList(lotto.countDownAnnounceSec).contains(secondCntr)){
						broadcastRemainingTime();
					}
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
			String message = lotto.getConfig().getString(lotto.daysHrsMinsSecRemain).replaceAll("%days%", Integer.toString(days));
			message = message.replaceAll("%hours%", Integer.toString(hours));
			message = message.replaceAll("%minutes%", Integer.toString(minutes));
			message = message.replaceAll("%seconds%", Integer.toString(seconds));
			sender.sendMessage(lotto.subColors(message));
		}else if(hours!=0){
			String message = lotto.getConfig().getString(lotto.hrsMinsSecRemain).replaceAll("%hours%", Integer.toString(hours));
			message = message.replaceAll("%minutes%", Integer.toString(minutes));
			message = message.replaceAll("%seconds%", Integer.toString(seconds));
			sender.sendMessage(lotto.subColors(message));
		}
		else if(minutes!=0){
			String message = lotto.getConfig().getString(lotto.minsSecRemain).replaceAll("%minutes%", Integer.toString(minutes));
			message = message.replaceAll("%seconds%", Integer.toString(seconds));
			sender.sendMessage(lotto.subColors(message));
		}else if(seconds!=0){
			String message = lotto.getConfig().getString(lotto.secRemain).replaceAll("%seconds%", Integer.toString(seconds));
			sender.sendMessage(lotto.subColors(message));
		}
	}//end printRemainingTime()


	public void broadcastRemainingTime(){
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
			String message = lotto.getConfig().getString(lotto.daysHrsMinsSecRemain).replaceAll("%days%", Integer.toString(days));
			message = message.replaceAll("%hours%", Integer.toString(hours));
			message = message.replaceAll("%minutes%", Integer.toString(minutes));
			message = message.replaceAll("%seconds%", Integer.toString(seconds));
			Bukkit.broadcastMessage(lotto.subColors(message));
		}else if(hours!=0){
			String message = lotto.getConfig().getString(lotto.hrsMinsSecRemain).replaceAll("%hours%", Integer.toString(hours));
			message = message.replaceAll("%minutes%", Integer.toString(minutes));
			message = message.replaceAll("%seconds%", Integer.toString(seconds));
			Bukkit.broadcastMessage(lotto.subColors(message));
		}
		else if(minutes!=0){
			String message = lotto.getConfig().getString(lotto.minsSecRemain).replaceAll("%minutes%", Integer.toString(minutes));
			message = message.replaceAll("%seconds%", Integer.toString(seconds));
			Bukkit.broadcastMessage(lotto.subColors(message));
		}else if(seconds!=0){
			String message = lotto.getConfig().getString(lotto.secRemain).replaceAll("%seconds%", Integer.toString(seconds));
			Bukkit.broadcastMessage(lotto.subColors(message));
		}
	}//end printRemainingTime()
}//end class LotteryTimer
