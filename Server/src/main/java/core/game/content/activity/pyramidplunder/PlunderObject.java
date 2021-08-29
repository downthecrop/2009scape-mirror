package core.game.content.activity.pyramidplunder;

import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;

/**
 * Object wrapper for pyramid plunder nodes
 * @author ceik
 */

public class PlunderObject extends Scenery {
    private transient Player player;
    private static int thisId;
    public static int snakeId, openId;


    public PlunderObject(Scenery obj){
        super(obj.getId(),obj.getLocation(),obj.getRotation(),obj.getDirection());
        this.thisId = obj.getId();
        switch(thisId){
            case 16501:
                this.snakeId = 16509;
                this.openId = 16505;
                break;
            case 16502:
                this.snakeId = 16510;
                this.openId = 16506;
                break;
            case 16503:
                this.snakeId = 16511;
                this.openId = 16507;
                break;
            case 16495:
                this.openId = 16496;
                break;
            case 16473:
                this.openId = 16474;
                break;
        }
    }
}
