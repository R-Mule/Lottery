package com.mulescraft.lottery;

public class Documentation {

	public Documentation(){
		
	}
	
	
	public String getDocumentation(){
		String message="Lottery Version 1.0.2 Welcome to Lottery!\n";
		message = message.concat("The section below, goes over example config setup, and what each container is used for.\n");
		message = message.concat("\n");
		message = message.concat("Auto Start Lottery Timer: true    Wether or not to start lottery on start up.\n");
		message = message.concat("\n");
		message = message.concat("Lottery Round Time in Minutes: 60    The amount of time in minutes each lottery should run for.\n");
		message = message.concat("Amount to Amplify Winnings By: 10.0    If you bet $200 you will get $200.00x10 = $2,000\n");
		message = message.concat("Ticket Max Pick Value: 9   Valid Ticket Range is between 1-9 INCLUSIVE.\n");
		message = message.concat("History Range to Show on Command History: 5   When /lottery history is used, the last 5 Lottery Results will be shown.\n");
		message = message.concat("\n");
		message = message.concat("Messages Section: This section will show ALL the possible message the players will be shown throughout using lottery commands.\n");
		message = message.concat("Color code examples are shown in these messages and you can customize them. If the message shows something like %amount%\n");
		message = message.concat("It means that message can also show the amount it relates to when you use %amount% in that message.\n");
		message = message.concat("\n");
		message = message.concat("If you have any questions please contact me on Spigot and I will aid you as soon as possible. Enjoy the Lottery!\n");

		return message;
	}
}
