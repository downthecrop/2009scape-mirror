package org.crandor.game.node.entity.player.ai.general;

import org.crandor.game.interaction.MovementPulse;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.object.GameObject;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.Point;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.map.RegionPlane;
import org.crandor.game.world.map.path.Path;
import org.crandor.game.world.map.path.Pathfinder;

public class ScriptAPI {

    public double distance(Node n1, Node n2)
    {
        return Math.sqrt(Math.pow(n1.getLocation().getX() - n2.getLocation().getX(), 2) + Math.pow(n2.getLocation().getY() - n1.getLocation().getY(), 2));
    }

    public Node getNearestEntity(Player me, String entityName)
    {
        Node entity = null;
        double minDistance = Double.MAX_VALUE;
        for (Node e : RegionManager.forId(me.getLocation().getRegionId()).getPlanes()[me.getLocation().getZ()].getEntities())
        {
            if (e != null && e.getName().equals(entityName) && distance(me, e) < minDistance && Pathfinder.find(me, e).isSuccessful())
            {
                /*if (entity != null)
                {
                    System.out.println("I'm at " + me.getLocation() + ". " + e.getLocation() + distance(me, e) + " is closer to me than " + entity.getLocation() + distance(me, entity));
                }*/
                entity = e;
                minDistance = distance(me, e);
            }
        }

        System.out.println("Start: " + me.getLocation() + ". End: " + entity.getLocation());
        return entity;
    }

    public GameObject getNearestGameObject(Location loc, int objectId) {
        GameObject nearestObject = null;
        double minDistance = Double.MAX_VALUE;
        for (GameObject[] o : RegionManager.forId(loc.getRegionId()).getPlanes()[0].getObjects()) {
            for (GameObject obj : o) {
                if (obj != null) {
                    if (distance(loc, obj) < minDistance) {
                        nearestObject = obj;
                    }
                }
            }
        }

        return nearestObject;
    }
}
