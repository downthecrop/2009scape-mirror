package org.crandor.game.content.eco.ge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.PlayerDetails;
import org.crandor.game.node.item.Item;

/**
 * Auto stocking feature for the grand exchange. 
 * @author jamix77
 *
 */
public class GEAutoStock {
	
	/**
	 * Should the auto stock feature be enabled in the game?
	 */
	public static final boolean toStock = true;
	
	/**
	 * The items to auto stock in the grand exchange.
	 */
	public static final ArrayList<Item> itemsToStock = new ArrayList<Item>();
	
	/**
	 * The values to modify the price by
	 */
	public static final ArrayList<Double> modValues = new ArrayList<Double>();
	
	/**
	 * Time to auto stock.
	 */
	public static void stock() {
		if (itemsToStock.isEmpty()) {
			try {
			      File file = new File("itemstostock.txt");
			      Scanner myReader = new Scanner(file);
			      while (myReader.hasNextLine()) {
			        String data = myReader.nextLine();
			        if (data.startsWith("#")) {
			        	continue;
			        }
			        itemsToStock.add(new Item(Integer.parseInt(data.split(":")[0]), Integer.parseInt(data.split(":")[1])));
			        modValues.add(Double.parseDouble(data.split(":")[2]));
			      }
			      myReader.close();
			    } catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
;		}
		
		
		for (int i = 0; i < itemsToStock.size(); i++) {
			Item item = itemsToStock.get(i);
			double markup = modValues.get(i);
			System.out.println("Stocking " + item.getName() + " id: " + item.getId() + " for " + (int)((item.getValue() * markup)/item.getAmount()) + " x" + item.getAmount() + " at " + markup + "x markup");
			GrandExchangeOffer offer = new GrandExchangeOffer(item.getId(), true);
			offer.setOfferedValue((int) ((item.getValue() * markup)/ item.getAmount()));
			offer.setAmount(item.getAmount());
			System.out.println(GEOfferDispatch.dispatch(new Player(PlayerDetails.getDetails("2009scape")), offer));
		}
	}
	
	
	
}
