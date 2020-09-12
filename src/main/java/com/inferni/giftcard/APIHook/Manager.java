package com.inferni.giftcard.APIHook;


import com.inferni.giftcard.Email.Emailer;
import com.inferni.giftcard.GiftCard;
import com.inferni.giftcard.Util.DataInterface;
import freemarker.template.TemplateException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Manager {
    private static Manager instance;

    public Manager() {
        instance = this;
    }

    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }
        return instance;
    }

    public void makeGiftCard(String player, int amount, CommandSender sender){
        int cardID = Hook.makeNewCard(amount);
        DataInterface.getInstance().addToPendingEmailList(player, cardID);
        sender.sendMessage(ChatColor.translateAlternateColorCodes
                ('&', GiftCard.config.getString("createCard")
                        .replaceAll("%id%", cardID + "")));
    }

    public void email(String player, String email, CommandSender sender) throws TemplateException, IOException, MessagingException {
        FileConfiguration d = GiftCard.data;
        if (!d.contains("Pending")) {
            d.createSection("Pending");
        }
        ConfigurationSection data = d.getConfigurationSection("Pending");
        if (data.contains(player)){
            List<Integer> cards = data.getIntegerList(player);
            for (int c : cards){
                Emailer.getInstance().sendEmail(c+"", player, email);
                sender.sendMessage(ChatColor.translateAlternateColorCodes
                        ('&', GiftCard.config.getString("emailSent")
                                .replaceAll("%email%", email)));
                if (Bukkit.getOfflinePlayer(player).isOnline()){
                    for (String s : GiftCard.config.getStringList("recieveCard")){
                        Bukkit.getPlayer(player).sendMessage(ChatColor
                                .translateAlternateColorCodes('&', s
                                        .replaceAll("%code%", Hook.getCardCode(c+""))));
                    }
                }
            }
            data.set(player, null);
            Set<String> keys = data.getKeys(false);
            keys.remove(player);
            GiftCard.getInstance().dataFile.saveData();

        } else {
            //No player
        }
    }
}
