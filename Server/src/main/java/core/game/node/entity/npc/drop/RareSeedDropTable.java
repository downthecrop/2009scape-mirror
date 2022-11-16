package core.game.node.entity.npc.drop;

import api.StartupListener;
import core.game.node.item.Item;
import core.game.node.item.WeightedChanceItem;
import core.tools.RandomFunction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import rs09.ServerConstants;
import rs09.game.system.SystemLogger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static rs09.game.system.SystemLogger.logInfo;

/**
 * Handles the rare seed drop table.
 * @author Von Hresvelg
 */
public final class RareSeedDropTable implements StartupListener {

	/**
	 * The item id of the item representing the rare seed drop table slot in a drop
	 * table.
	 */
	public static final int SLOT_ITEM_ID = 14428;

	/**
	 * The rare seed drop table.
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

	public RareSeedDropTable() throws ParserConfigurationException {}

	@Override
	public void startup() {
		if(ServerConstants.RSDT_DATA_PATH != null && !new File(ServerConstants.RSDT_DATA_PATH).exists()){
			SystemLogger.logErr(this.getClass(), "Can't locate RSDT file at " + ServerConstants.RSDT_DATA_PATH);
			return;
		}
		parse(ServerConstants.RSDT_DATA_PATH);
		logInfo(this.getClass(), "Initialized Rare Seed Drop Table from " + ServerConstants.RSDT_DATA_PATH);
	}

	/**
	 * Parses the xml file for the rare seed drop table.
	 * @param file the .xml file containing the rare seed drop table.
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