package com.mulescraft.lottery;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
				sender.sendMessage("This command can only be run by a player.");//sorry console.
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
					}else if(args[0].equalsIgnoreCase("reload")&&sender.hasPermission("lottery.reload")){
						//reload the config
						sender.sendMessage("RELOAD");
					}else if(args[0].equalsIgnoreCase("buy")&&sender.hasPermission("lottery.buy")){//if you want to buy
						buyCommand(sender,cmd,label,args);
					}else if(args[0].equalsIgnoreCase("refund")&&sender.hasPermission("lottery.refund")){//if you want to refund your ticket. 
						refundCommand(sender,cmd,label,args);
					}else if(args[0].equalsIgnoreCase("time")&&sender.hasPermission("lottery.time")){
						if(lotto.isActive){
						lotto.lotTime.printRemainingTime(sender);
						}else{
							sender.sendMessage(lotto.subColors(lotto.noActiveLotteryMsg));//lotto not running right now message
						}//end else no time to show
					}else if(args[0].equalsIgnoreCase("stats")&&sender.hasPermission("lottery.stats")){
						statsCommand(sender,cmd,label,args);
					}else if(args[0].equalsIgnoreCase("history")&&sender.hasPermission("lottery.history")){
						ServerStats sstats = new ServerStats(getPlayer(sender),lotto);
						sstats.printHistory();
						//shows last 10 lottery winners. Maybe make 10 config option? Recommend no more than 20.
						sender.sendMessage("HISTORY");
					}else{
						//command unknown, maybe print menu? /lottery help would trigger this.
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

		/*
		 * /lottery help
			/lottery stop (admin command, stop lottery)
			/lottery start (admin command, start lottery manual)
			/lottery reload (reload config file)
			/lottery buy <lucky number> <amount of money>
			/lottery refund returns bid and removes you from lottery
		 * /lottery check (top 10 number and number of player who buy it, and time to this round end)
			==== Round #56 Time to round end: 34 minutes= ======
			1#Number 13 54 players buy
			2#Number 76 34 players buy
			...............................
			10#Number 34 12 players buy

			/lottery history (10 recent round result number)
			==== Lottery history: 10 recent rounds=======
			#round 1345 15 (number win on this round)
			#round 1344 87
			#round 1343 90
			............
			#round 1335 08
			When player click their lottery (winner lottery paper), it shoud broadcast their name, money prize they take, lucky name, round.
		 */

	}//end onCommand

	private void printCommandMenu(CommandSender sender){
		if(sender.hasPermission("lottery.lottery")){
			sender.sendMessage(ChatColor.BLUE+"Lottery Commands");
		}//end lottery header
		if(sender.hasPermission("lottery.stop")){
			sender.sendMessage(ChatColor.WHITE+"/lottery stop"+ChatColor.GREEN+" : stops the lottery rewarding now");
		}//end loc
		if(sender.hasPermission("lottery.start")){
			sender.sendMessage(ChatColor.WHITE+"/lottery start"+ChatColor.GREEN+" : starts a lottery forcefully.");
		}//end give
		if(sender.hasPermission("lottery.reload")){
			sender.sendMessage(ChatColor.WHITE+"/lottery reload"+ChatColor.GREEN+" : reloads lottery config.yml");
		}//end spawn
		if(sender.hasPermission("lottery.buy")){
			sender.sendMessage(ChatColor.WHITE+"/lottery buy <number> <amount of money>"+ChatColor.GREEN+" : buys <number> with <amount of money> gambled");
		}//end open
		if(sender.hasPermission("lottery.refund")){
			sender.sendMessage(ChatColor.WHITE+"/lottery refund"+ChatColor.GREEN+" : returns your money, and removes your bet");
		}//end open
		if(sender.hasPermission("lottery.time")){
			sender.sendMessage(ChatColor.WHITE+"/lottery time"+ChatColor.GREEN+" : shows remaining time until end of lottery");
		}//end open
		if(sender.hasPermission("lottery.stats")){
			sender.sendMessage(ChatColor.WHITE+"/lottery stats <server/self>"+ChatColor.GREEN+" : shows categories of best all time stats");
		}//end open
		if(sender.hasPermission("lottery.history")){
			sender.sendMessage(ChatColor.WHITE+"/lottery history"+ChatColor.GREEN+" : shows last 10 lottery winners");
		}//end open
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
			if(StringUtils.isNumeric(args[1])&&StringUtils.isNumeric(args[2])&&number.isValidNumber(Integer.parseInt(args[1]))&&Double.parseDouble(args[2])>0){//if you picked in the right range from config range.
				
					Player player = getPlayer(sender);
					if(lotto.econ.getBalance(player)>=Double.parseDouble(args[2])){//can you afford this ticket?
						lotto.econ.withdrawPlayer(player, Double.parseDouble(args[2]));//purchased!
						//create ticket in ActiveTickets.yml 
						TicketManager tman = new TicketManager(lotto);
						if(tman.addTicket(player,Integer.parseInt(args[1]),Double.parseDouble(args[2]))){
							String message = lotto.getConfig().getString(lotto.betAcceptedMsg);
							message = replaceVars(Integer.parseInt(args[1]),Double.parseDouble(args[2]),message);
							message = lotto.subColors(message);//replace any colors.
							sender.sendMessage(message);
						}else if(sender.hasPermission("lottery.refund")){
							String message = lotto.getConfig().getString(lotto.refundBeforeBetAgainMsg); //there is already an active bid! use /lottery refund to remove it. IF THEY HAVE THAT PERMISSION!
							sender.sendMessage(lotto.subColors(message));//send the message after swapping colors.
						}else{
							String message = lotto.getConfig().getString(lotto.alreadyPlacedBetMsg);//sorry you have already placed your bid! Message.
							sender.sendMessage(lotto.subColors(message));
						}//end else bid already placed msg.
					}else{
						sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.notEnoughMoneyMsg)));//Not enough money to place the bet message!
					}//end else not enough money message
			//	}else{
					//sender.sendMessage(lotto.subColors(lotto.missingBuyArguments));//missing buy arguments message.
				}//end else missing Buy Arguments.
			}else{//invalid arguments.
				String message = lotto.getConfig().getString(lotto.invalidNumberArguments);//there is an invalid number problem. Send message
				sender.sendMessage(lotto.subColors(message));
			}//end else invalid arguments
			sender.sendMessage("BUY");
		}else{//Lottery is inactive!
			String message = lotto.getConfig().getString(lotto.noActiveLotteryMsg);//send inactive lottery msg.
			sender.sendMessage(lotto.subColors(message));
		}//end else inactive lottery
	}//end buyCommand()
	
		private void refundCommand(CommandSender sender, Command cmd, String label, String[] args){
			if(lotto.isActive){
				//see if they have ActiveTicket
				Player player = getPlayer(sender);
				PlayerData pdata = new PlayerData(player, lotto);
				TicketManager tman = new TicketManager(lotto);
				if(!tman.refundTicket(player)){//if the refund was run
					sender.sendMessage(lotto.subColors(lotto.getConfig().getString(lotto.noBet2RefundMsg)));//send the no bet to refund message.
				}
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
