package org.crandor.game.node.entity.player.ai.general;

import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.map.RegionPlane;
import org.crandor.game.world.map.path.Pathfinder;

public class ScriptAPI {

    public double distance(Location l1, Location l2) {
        return Pathfinder.find(l1, l2).getPoints().size();
    }

    public NPC getNearestNPC(Location loc, String npcName) {
        NPC npc = null;
        double minDistance = Double.MAX_VALUE;
        for (RegionPlane p : RegionManager.forId(loc.getRegionId()).getPlanes()) {
            for (NPC n : p.getNpcs()) {
                if (n != null) {
                    if (n.getName().equals(npcName) && distance(loc, n.getLocation()) < minDistance) {
                        npc = n;
                    }
                }
            }
        }

        System.out.println("Returning " + npc.getName());
        return npc;
    }

    public GameObject getNearestGameObject(Location loc, int objectId) {
        GameObject nearestObject = null;
        double minDistance = Double.MAX_VALUE;
        for (GameObject[] o : RegionManager.forId(loc.getRegionId()).getPlanes()[0].getObjects()) {
            for (GameObject obj : o) {
                if (obj != null) {
                    if (distance(loc, obj.getLocation()) < minDistance) {
                        nearestObject = obj;
                    }
                }
            }
        }

        return nearestObject;
    }
}
