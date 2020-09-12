package com.inferni.giftcard.Commands;

import com.inferni.giftcard.APIHook.Manager;
import com.inferni.giftcard.GiftCard;
import freemarker.template.TemplateException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.mail.MessagingException;
import java.io.IOException;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("giftcard")){
            if (!sender.isOp()){
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
                return false;
            }
            if (args.length == 3){
                if (args[0].equalsIgnoreCase("givecard")){
                    int amount;
                    try{
                        amount = Integer.valueOf(args[1]);
                    } catch (Exception ex){
                        sender.sendMessage("Invalid amount term");
                        return false;
                    }
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
                    if (target == null){
                        sender.sendMessage("Invalid player entered");
                        return false;
                    }
                    Manager.getInstance().makeGiftCard(args[2], amount, sender);

                }
                if (args[0].equalsIgnoreCase("emailcard")){
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if (target == null){
                        sender.sendMessage("Invalid player entered");
                        return false;
                    }
                    String email = args[2];
                    try {
                        Manager.getInstance().email(args[1], email, sender);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                sender.sendMessage(ChatColor
                        .translateAlternateColorCodes('&', "&cIncorrect Usage"));
                sender.sendMessage(ChatColor
                        .translateAlternateColorCodes('&', "&c/giftcard givecard [Amount] [Player]"));
                sender.sendMessage(ChatColor
                        .translateAlternateColorCodes('&', "&c/giftcard emailcard [Player] [Email]"));

            }
        }
        return false;
    }
}
