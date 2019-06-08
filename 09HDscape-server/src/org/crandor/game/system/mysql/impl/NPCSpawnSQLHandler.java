package org.crandor.game.system.mysql.impl;

import org.crandor.ServerConstants;
import org.crandor.cache.Cache;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.system.mysql.SQLEntryHandler;
import org.crandor.game.system.mysql.SQLManager;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.tools.TimeStamp;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles the spawning of NPC's.
 *
 * @author Vexia
 */
public class NPCSpawnSQLHandler extends SQLEntryHandler<NPC> {

    private ArrayList<NPC> NPC_SPAWNS = new ArrayList<>();
    private ArrayList<Integer> REGIONS = new ArrayList<>();

    /**
     * The current npc being spawned.
     */
    private NPC npc;

    /**
     * Constructs a new {@Code NPCSpawnSqlHandler} {@Code Object}
     */
    public NPCSpawnSQLHandler() {
        super(null, (SQLManager.LOCAL ? "server" : ServerConstants.DATABASE_NAMES[0]) + ".npc_spawns", "", "");
    }

    /**
     * Constructs a new {@Code NPCSpawnSqlHandler} {@Code Object}
     *
     * @param entry  the entry.
     * @param table  the table.
     * @param column the column.
     * @param value  the value.
     */
    public NPCSpawnSQLHandler(NPC entry, String table, String column, String value) {
        super(entry, (SQLManager.LOCAL ? "server" : ServerConstants.DATABASE_NAMES[0]) + ".npc_spawns", "npc_id", "" + entry.getId());
    }

    /**
     * Runs the NPC Spawn SQL handler.
     *
     * @param args the args.
     * @throws SQLException the exception if thrown.
     */
    public static void main(String... args) throws SQLException {
        Cache.init();
        TimeStamp stamp = new TimeStamp();
        new NPCSpawnSQLHandler().parse();
        stamp.duration(true, "");
    }

    @Override
    public void parse() throws SQLException {
        connection = getConnection();
        if (connection == null) {
            SQLManager.close(connection);
            return;
        }
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + (SQLManager.LOCAL ? "server" : ServerConstants.DATABASE_NAMES[0]) + ".npc_spawns");
        ResultSet set = statement.executeQuery();
        while (set.next()) {
            parseNpc(set.getInt(1), set.getString(2));
        }
        SQLManager.close(statement.getConnection());
    }

    @Override
    public void create() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("");
        statement.executeUpdate();
        SQLManager.close(statement.getConnection());
    }

    @Override
    public void save() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("");
        statement.executeUpdate();
        SQLManager.close(statement.getConnection());
    }

    /**
     * Parses an npc id's spawns.
     *
     * @param id   the id.
     * @param data the data.
     */
    public void parseNpc(int id, String data) {
        String[] datas = data.split("-");
        String[] tokens = null;
        for (String d : datas) {
            try {
                tokens = d.replace("{", "").replace("}", "").split(",");
                npc = NPC.create(id, Location.create(Integer.valueOf(tokens[0].trim()), Integer.valueOf(tokens[1].trim()), Integer.valueOf(tokens[2].trim())));
                npc.setWalks(tokens[3].trim().equals("1"));
                npc.setDirection(Direction.values()[Integer.valueOf(tokens[4].trim())]);
                npc.setAttribute("spawned:npc", true);
                npc.init();
                NPC_SPAWNS.add(npc);
                int region_id = npc.getLocation().getRegionId();
                boolean walks = tokens[3].trim().equals("1");
                if (!REGIONS.contains(region_id)) {
                    REGIONS.add(region_id);
                }
//                System.out.println(npc.getName() + " " + walks);
//                convertNPC(npc);
//                System.out.println("Added npc " + npc.getName());
            } catch (Throwable t) {
                System.err.println("Error on " + id);
                t.printStackTrace();
            }
        }
        npc = null;
    }

    public void convertNPC(NPC npc) {
            JSONArray npcs_in_region = new JSONArray();
            int r = 0;
            for (NPC n : NPC_SPAWNS) {
                if (n.getLocation().getRegionId() == npc.getLocation().getRegionId()) {
                    Map obj = new LinkedHashMap();
                    obj.put("npcId", n.getId());
                    obj.put("xPos", n.getLocation().getX());
                    obj.put("yPos", n.getLocation().getY());
                    obj.put("height", n.getLocation().getZ());
                    obj.put("walks", n.isWalks());
                    if (n.isWalks()) {
                        obj.put("radius", 4);
                    }
                    obj.put("direction", n.getDirection().toInteger());
                    obj.put("name", n.getName());
                    npcs_in_region.add(obj);
                        r = n.getLocation().getRegionId();
                }

            }
            try (FileWriter file = new FileWriter("spawns/" + r + ".json")) {
                file.write(formatJSONStr(npcs_in_region.toString(), 3));
                r = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static String formatJSONStr(final String json_str, final int indent_width) {
        final char[] chars = json_str.toCharArray();
        final String newline = System.lineSeparator();

        String ret = "";
        boolean begin_quotes = false;

        for (int i = 0, indent = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c == '\"') {
                ret += c;
                begin_quotes = !begin_quotes;
                continue;
            }

            if (!begin_quotes) {
                switch (c) {
                    case '{':
                    case '[':
                        ret += c + newline + String.format("%" + (indent += indent_width) + "s", "");
                        continue;
                    case '}':
                    case ']':
                        ret += newline + ((indent -= indent_width) > 0 ? String.format("%" + indent + "s", "") : "") + c;
                        continue;
                    case ':':
                        ret += c + " ";
                        continue;
                    case ',':
                        ret += c + newline + (indent > 0 ? String.format("%" + indent + "s", "") : "");
                        continue;
                    default:
                        if (Character.isWhitespace(c)) continue;
                }
            }

            ret += c + (c == '\\' ? "" + chars[++i] : "");
        }

        return ret;
    }

    public void addNPC(Player player, String id) {
        try {
            connection = getConnection();
            if (connection == null) {
                SQLManager.close(connection);
                return;
            }
            int npc_id = Integer.parseInt(id);
            StringBuilder b = new StringBuilder("UPDATE `npc_spawns` ");
            //{3222,3221,0,1,3}
            b.append("SET `loc_data`='" + (result.next() ? "-{" +
                    player.getLocation().getX() + ", "
                    + player.getLocation().getY() + ", "
                    + player.getLocation().getZ() + ", 1, 0}"
                    : "{" +
                    player.getLocation().getX() + ", "
                    + player.getLocation().getY() + ", "
                    + player.getLocation().getZ() + ", 1, 0}") + "' WHERE `npc_id`='" + npc_id + "'");
            PreparedStatement statement = connection.prepareStatement(b.toString());
            statement.executeUpdate();
            SQLManager.close(statement.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Connection getConnection() {
        return SQLManager.getConnectionServer();
    }

}
