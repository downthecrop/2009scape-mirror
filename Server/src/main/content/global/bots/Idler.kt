package content.global.bots

import core.game.bots.Script

class Idler : Script(){
    override fun tick() {
    }

    override fun newInstance(): Script {
        return this
    }
}