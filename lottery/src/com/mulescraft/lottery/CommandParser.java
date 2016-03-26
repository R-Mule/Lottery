package com.mulescraft.lottery;

import org.apache.commons.lang.NumberUtils;
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
		//sender.sendMessage("Here with:"+args[0]);
		if (cmd.getName().equalsIgnoreCase("lottery")&&sender.hasPermission("lottery.lottery")){  // If the player typed /crate then do
			if (!(sender instanceof Player)){
				sender.sendMessage("This command can only be run by a player.");//sorry console.
			}//end console check.
			else{
				if(args!=null && args.length>0){
					if(args[0].equalsIgnoreCase("stop")&&sender.hasPermission("lottery.stop")){
						sender.sendMessage("STOP");
						//end lottery timer complete stop.
					}else if(args[0].equalsIgnoreCase("start")&&sender.hasPermission("lottery.start")){
						//force start the lottery timer
						sender.sendMessage("START");
					}else if(args[0].equalsIgnoreCase("reload")&&sender.hasPermission("lottery.reload")){
						//reload the config
						sender.sendMessage("RELOAD");
					}else if(args[0].equalsIgnoreCase("buy")&&sender.hasPermission("lottery.buy")){
						Numbers number = new Numbers(lotto);
						if(StringUtils.isNumeric(args[1])&&StringUtils.isNumeric(args[2])&&number.isValidNumber(Integer.parseInt(args[1]))&&Integer.parseInt(args[2])>0){//if you picked in the right range from config range.
							Player player = getPlayer(sender);
							if(lotto.econ.getBalance(player)>=Double.parseDouble(args[2])){//can you afford this ticket?
								lotto.econ.withdrawPlayer(player, Double.parseDouble(args[2]));//purchased!
								//create ticket in ActiveTickets.yml 
							}else{
								//error not enough money message
							}//end else not enough money message
						}else{//invalid arguments.
							//there is an invalid number problem. Send message
						}//end else invalid arguments
						//buy <lucky number> <amount of money>
						//PlayerData player = new PlayerData();
						sender.sendMessage("BUY");
					}else if(args[0].equalsIgnoreCase("refund")&&sender.hasPermission("lottery.refund")){
						//see if they have ActiveTicket
						//if active refund and delete ticket.
						//if not send message no active tickets.
						//removes the bid the player made, refunding money input
						sender.sendMessage("REFUND");
					}else if(args[0].equalsIgnoreCase("time")&&sender.hasPermission("lottery.time")){
						//shows remaining time
						//this should be fun....
						sender.sendMessage("TIME");
					}else if(args[0].equalsIgnoreCase("stats")&&sender.hasPermission("lottery.stats")){
						//shows stat options, like best all time winner, worst lost. etc.

						Player player = getPlayer(sender);
						PlayerData pdata = new PlayerData(player,lotto);
						sender.sendMessage("STATS:"+ pdata.getBiggestWin());
						pdata.addWin(1);
						sender.sendMessage("STATS:"+ pdata.getBiggestWin());

					}else if(args[0].equalsIgnoreCase("history")&&sender.hasPermission("lottery.history")){
						//shows last 10 lottery winners.
						sender.sendMessage("HISTORY");
					}else{
						//command unknown, maybe print menu? /lottery help would trigger this.
						printCommandMenu(sender);
					}
				}//end if there is an argument.
				else{
					printCommandMenu(sender);//There are no arguments. So /lottery menu display all commands.
				}
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
			sender.sendMessage(ChatColor.WHITE+"/lottery stats"+ChatColor.GREEN+" : shows categories of best all time stats");
		}//end open
		if(sender.hasPermission("lottery.history")){
			sender.sendMessage(ChatColor.WHITE+"/lottery history"+ChatColor.GREEN+" : shows last 10 lottery winners");
		}//end open
	}
	private Player getPlayer(CommandSender sender){
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.getName().equalsIgnoreCase(sender.getName())){
				return player;
			}//end if right player!

		}//end for all players
		return null;
	}//end getPlayer
}//end CommandParser
