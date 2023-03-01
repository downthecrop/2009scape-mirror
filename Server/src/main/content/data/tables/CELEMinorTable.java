package content.data.tables;

import core.api.StartupListener;
import core.ServerConstants;
import core.game.node.item.Item;
import core.game.node.item.WeightedChanceItem;
import core.tools.Log;
import core.tools.RandomFunction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.log;

/**
 * Handles the Chaos Elemental's minor drop table. It is supposed to roll this table alongside its standard major drops on the main table.
 * @author Crash
 */
public final class CELEMinorTable implements StartupListener {

	/**
	 * The item id of the item representing the C. Ele minor drop table slot in a drop
	 * table.
	 */
	public static final int SLOT_ITEM_ID = 799; // Crash: Item ID 799 is currently a null, blank paper note object. Unsure if used for cutscene/cutscene items, but hijacking this as C.Ele's item container as it'll never be dropped or obtained in normal gameplay.

	/**
	 * The rare drop table.
	 */
	private static final List<WeightedChanceItem> TABLE = new ArrayList<>(20);


	/**
	 * Initialize needed objects for xml reading/writing
	 */
	static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	static DocumentBuilder builder;

	static {
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public CELEMinorTable() throws ParserConfigurationException {}

	@Override
	public void startup() {
		if(ServerConstants.CELEDT_DATA_PATH != null && !new File(ServerConstants.CELEDT_DATA_PATH).exists()){
			log(this.getClass(), Log.ERR, "Can't locate CELEDT file at " + ServerConstants.CELEDT_DATA_PATH);
			return;
		}
		parse(ServerConstants.CELEDT_DATA_PATH);
		log(this.getClass(), Log.FINE, "Loaded up Chaos Elemental drop table from " + ServerConstants.CELEDT_DATA_PATH);
	}

	/**
	 * Parses the xml file for the CELEDT.
	 * @param file the .xml file containing the CELEDT.
	 */
	public static void parse(String file){
		try {
			Document doc = builder.parse(file);

			NodeList itemNodes = doc.getElementsByTagName("item");
			for(int i = 0; i < itemNodes.getLength(); i++){
				Node itemNode = itemNodes.item(i);
				if(itemNode.getNodeType() == Node.ELEMENT_NODE){
					Element item = (Element) itemNode;
					int itemId = Integer.parseInt(item.getAttribute("id"));
					int minAmt = Integer.parseInt(item.getAttribute("minAmt"));
					int maxAmt = Integer.parseInt(item.getAttribute("maxAmt"));
					int weight = Integer.parseInt(item.getAttribute("weight"));

					TABLE.add(new WeightedChanceItem(itemId,minAmt,maxAmt,weight));
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static Item retrieve(){
		return RandomFunction.rollWeightedChanceTable(TABLE);
	}
}