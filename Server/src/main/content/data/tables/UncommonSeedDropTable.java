package content.data.tables;

import core.api.StartupListener;
import core.game.node.item.Item;
import core.game.node.item.WeightedChanceItem;
import core.tools.Log;
import core.tools.RandomFunction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import core.ServerConstants;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.log;

/**
 * Handles the uncommon seed drop table.
 * @author Von Hresvelg
 */
public final class UncommonSeedDropTable implements StartupListener {

	/**
	 * The item id of the item representing the uncommon seed drop table slot in a drop
	 * table.
	 */
	public static final int SLOT_ITEM_ID = 14422;

	/**
	 * The uncommon seed drop table.
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

	public UncommonSeedDropTable() throws ParserConfigurationException {}

	@Override
	public void startup() {
		if(ServerConstants.USDT_DATA_PATH != null && !new File(ServerConstants.USDT_DATA_PATH).exists()){
			log(this.getClass(), Log.ERR, "Can't locate USDT file at " + ServerConstants.USDT_DATA_PATH);
			return;
		}
		parse(ServerConstants.USDT_DATA_PATH);
		log(this.getClass(), Log.FINE, "Initialized Uncommon Seed Drop Table from " + ServerConstants.USDT_DATA_PATH);
	}

	/**
	 * Parses the xml file for the uncommon seed drop table.
	 * @param file the .xml file containing the uncommon seed drop table.
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