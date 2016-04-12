package com.mulescraft.lottery;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandParser implements CommandExecutor {
	Lottery lotto;
	CommandParser(Lottery lottery){
		this.lotto=lottery;
	}//end CommandParser
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("lottery")&&sender.hasPermission("lottery.lottery")){  // If the player typed /crate then do
			if (!(sender instanceof Player)){
				if(args[0].equalsIgnoreCase("stop")&&sender.hasPermission("lottery.stop")){
					if(lotto.isActive){//stop the lotto, it is active
						lotto.lotTime.stopLottery();
						TicketManager tman = new TicketManager(lotto);
						tman.lotteryEnded();
					}else{
						sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.notStpdAlrdyStpdMsg)));//already stopped message! Send it!
					}//
				}else if(args[0].equalsIgnoreCase("start")&&sender.hasPermission("lottery.start")){
					//force start the lottery timer
					if(!lotto.isActive){//start if not active
						lotto.lotTime = new LotteryTimer(lotto,lotto.getConfig().getInt(lotto.lotteryRndTime));
					}else{
						sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.notStrtAlrdyStrtdMsg)));//send could not start message.
					}
				}else{
				sender.sendMessage("This command can only be run by a player.");//sorry console.
				}
			}//end console check.
			else{
				if(args!=null && args.length>0){
					if(args[0].equalsIgnoreCase("stop")&&sender.hasPermission("lottery.stop")){
						if(lotto.isActive){//stop the lotto, it is active
							lotto.lotTime.stopLottery();
							TicketManager tman = new TicketManager(lotto);
							tman.lotteryEnded();
						}else{
							sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.notStpdAlrdyStpdMsg)));//already stopped message! Send it!
						}//
						//end lottery timer complete stop.
					}else if(args[0].equalsIgnoreCase("start")&&sender.hasPermission("lottery.start")){
						//force start the lottery timer
						if(!lotto.isActive){//start if not active
							lotto.lotTime = new LotteryTimer(lotto,lotto.getConfig().getInt(lotto.lotteryRndTime));
						}else{
							sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.notStrtAlrdyStrtdMsg)));//send could not start message.
						}
					}else if(args[0].equalsIgnoreCase("buy")&&sender.hasPermission("lottery.buy")){//if you want to buy
						buyCommand(sender,cmd,label,args);
					}else if(args[0].equalsIgnoreCase("refund")&&sender.hasPermission("lottery.refund")){//if you want to refund your ticket. 
						refundCommand(sender,cmd,label,args);
					}else if(args[0].equalsIgnoreCase("time")&&sender.hasPermission("lottery.time")){
						if(lotto.isActive){
							lotto.lotTime.printRemainingTime(sender);
						}else{
							sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.noActiveLotteryMsg)));
						}//end else no time to show
					}else if(args[0].equalsIgnoreCase("stats")&&sender.hasPermission("lottery.stats")){
						statsCommand(sender,cmd,label,args);
					}else if(args[0].equalsIgnoreCase("history")&&sender.hasPermission("lottery.history")){
						TicketManager tman = new TicketManager(lotto);
						tman.printLotteryHistory(sender);//print that history!
					}else if(args[0].equalsIgnoreCase("current")&&sender.hasPermission("lottery.current")){
						if(lotto.isActive){
							TicketManager tman = new TicketManager(lotto);
							tman.printActiveTickets(sender);//print that active info!!
						}else{
							sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.noActiveLotteryMsg)));//lotto not running right now message
						}//end else
					}else{
						printCommandMenu(sender);
					}//end else command unknown. Print menu to help them.
				}//end if there is an argument.
				else{
					printCommandMenu(sender);//There are no arguments. So /lottery menu display all commands.
				}//end else no argument was provided.
			}//end else sender is a player
			return true;
		}//end if the command was lottery
		return false;

	}//end onCommand

	private void printCommandMenu(CommandSender sender){
		if(sender.hasPermission("lottery.lottery")){
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.commandMenuTitleMsg)));
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.commandMenuRangeMsg).replaceAll("%range%",Integer.toString(lotto.getConfig().getInt(lotto.ticketRange)))));
		}//end lottery header
		if(sender.hasPermission("lottery.stop")){
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.commandMenuStopMsg)));
		}//end loc
		if(sender.hasPermission("lottery.start")){
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.commandMenuStartMsg)));
		}//end give
		if(sender.hasPermission("lottery.buy")){
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.commandMenuBuyMsg).replaceAll("%range%",Integer.toString(lotto.getConfig().getInt(lotto.ticketRange)))));
		}//end open
		if(sender.hasPermission("lottery.refund")){
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.commandMenuRefundMsg)));
		}//end open
		if(sender.hasPermission("lottery.time")){
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.commandMenuTimeMsg)));
		}//end open
		if(sender.hasPermission("lottery.stats")){
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.commandMenuStatsMsg)));
		}//end open
		if(sender.hasPermission("lottery.history")){
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.commandMenuHistoryMsg).replaceAll("%historyrange%",Integer.toString(lotto.getConfig().getInt(lotto.historyRange)))));
		}//end open
		if(sender.hasPermission("lottery.current")){
			sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.commandMenuCurrentMsg)));
		}
	}//end printCommandMenu

	private String replaceVars(int num2Sub,double amt2Sub,String message){//this subs in the values from config %% replacements.
		message = message.replaceAll("%luckynumber%", Integer.toString(num2Sub));
		message = message.replaceAll("%amountbet%", Double.toString(amt2Sub));
		return message;
	}//end replaceVars()

	private Player getPlayer(CommandSender sender){
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.getName().equalsIgnoreCase(sender.getName())){
				return player;
			}//end if right player!

		}//end for all players
		return null;
	}//end getPlayer

	private void buyCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(lotto.isActive){
			Numbers number = new Numbers(lotto);
			if(args.length>=3){
				if(StringUtils.isNumeric(args[1])&&StringUtils.isNumeric(args[2])&&number.isValidNumber(Integer.parseInt(args[1]),getPlayer(sender))&&Double.parseDouble(args[2])>0){//is number.
					Player player = getPlayer(sender);
					if(lotto.econ.getBalance(player)>=Double.parseDouble(args[2])){//can you afford this ticket?
						if(number.isValidNumber(Integer.parseInt(args[1]),player)){
							//INSERT HERE, IF TICKET IS NOT ALREADY ACTIVE!
							//create ticket in ActiveTickets.yml 
							TicketManager tman = new TicketManager(lotto);
							if(lotto.getConfig().getInt(lotto.maxBetAmount)>=Integer.parseInt(args[2])||lotto.getConfig().getInt(lotto.maxBetAmount)==-1){//if under max bet amount per ticket.
								if(lotto.atData.getInt(player.getUniqueId().toString()+".TotalActiveTickets")<lotto.getConfig().getInt(lotto.maxTicketsPerPlayer)){
									if(tman.addTicket(player,Integer.parseInt(args[1]),Double.parseDouble(args[2]))){
										lotto.econ.withdrawPlayer(player, Double.parseDouble(args[2]));//purchased!
										if(lotto.getConfig().getBoolean(lotto.soundsEnabled))
											player.getWorld().playSound(player.getLocation(), Sound.valueOf(lotto.getConfig().getString(lotto.soundOnBuy)),100,0);
										String message = lotto.getConfig().getString(lotto.betAcceptedMsg);
										message = replaceVars(Integer.parseInt(args[1]),Double.parseDouble(args[2]),message);
										message = lotto.subColors(message);//replace any colors.
										sender.sendMessage(message);
									}else{//That ticket is already active MSG.
										sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.alreadyActiveTicketMsg).replaceAll("%number%",args[1])));
									}//end else already active ticket msg.
								}else{//Sorry you are at your max tickets per lottery
									sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.maxTicketsPerPlayerMsg).replaceAll("%maxnumber%",Integer.toString(lotto.getConfig().getInt(lotto.maxTicketsPerPlayer)))));
								}
							}else{
								sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.maxBetAmountExceededMsg).replaceAll("%amount%", Integer.toString(lotto.getConfig().getInt(lotto.maxBetAmount)))));//Max Bet Amount Msg
							}
						}//end else is a valid number.
					}else{
						sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.notEnoughMoneyMsg)));//Not enough money to place the bet message!
					}//end else not enough money message
				}//end else missing Buy Arguments.
			}else{//invalid arguments.
				String message = lotto.getConfig().getString(lotto.invalidNumberArguments);//there is an invalid number problem. Send message
				sender.sendMessage(lotto.subColors(message));
			}//end else invalid arguments
		}else{//Lottery is inactive!
			String message = lotto.getConfig().getString(lotto.noActiveLotteryMsg);//send inactive lottery msg.
			sender.sendMessage(lotto.subColors(message));
		}//end else inactive lottery
	}//end buyCommand()

	private void refundCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(lotto.isActive){
			if(args.length>=2){
				//see if they have ActiveTicket
				Player player = getPlayer(sender);
				//PlayerData pdata = new PlayerData(player, lotto);
				TicketManager tman = new TicketManager(lotto);
				//
				if(StringUtils.isNumeric(args[1])){//if refund Number is a number
					tman.refundTicket(player, Integer.parseInt(args[1]));
					//if(!tman.refundTicket(player)){//if the refund was run
					//	sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.noBet2RefundMsg)));//send the no bet to refund message.
					//}
				}else{//not a number for refund.
					sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.refundSyntaxErrorMsg)));//not a valid Number msg.
				}//end else not a valid number
			}else{//Not enough args for Refund
				sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.refundSyntaxErrorMsg)));
			}//end else not enough args
		}else{//inactive lottery! No refund available.
			String message = lotto.getConfig().getString(lotto.noActiveLotteryMsg);//send inactive lottery msg.
			sender.sendMessage(lotto.subColors(message));
		}//end else lotto is inactive msg.
	}//end refundCommand

	private void statsCommand(CommandSender sender, Command cmd, String label, String[] args){
		//shows stat options, like best all time winner, worst lost. etc.
		Player player = getPlayer(sender);
		if(args.length>=2&&args[1].equalsIgnoreCase("self")){
			PlayerData pdata = new PlayerData(player,lotto);
			pdata.printPlayerStats();
		}else if(args.length>=2&&args[1].equalsIgnoreCase("server")){
			ServerStats sstats = new ServerStats(player,lotto);
			sstats.printServerStats();
		}else{//end else if get server stats
			String message = lotto.getConfig().getString(lotto.missingSelfServerMsg);//missing self/server message.
			sender.sendMessage(lotto.subColors(message));
		}//end else missing self/server message
	}//end statsCommand
}//end CommandParser
