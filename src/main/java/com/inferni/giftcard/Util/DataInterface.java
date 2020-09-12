package com.inferni.giftcard.Util;

import com.inferni.giftcard.GiftCard;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class DataInterface {
    private static DataInterface instance;

    public DataInterface() {
        instance = this;
    }

    public static DataInterface getInstance() {
        if (instance == null) {
            instance = new DataInterface();
        }
        return instance;
    }

    public void addToPendingEmailList(String player, int CardID){
        FileConfiguration dataF = GiftCard.data;
        if (!dataF.contains("Pending")) {
            dataF.createSection("Pending");
        }
        ConfigurationSection data = dataF.getConfigurationSection("Pending");
        if (!data.contains(player)){
            List<Integer> card = new ArrayList<Integer>();
            card.add(CardID);
            data.set(player, card);
        } else {
            List<Integer> card = data.getIntegerList(player);
            card.add(CardID);
            data.set(player, card);
        }

        GiftCard.getInstance().dataFile.saveData();
    }
}
