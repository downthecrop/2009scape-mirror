package rs09.game.ai.general.scriptrepository

class Idler : Script(){
    override fun tick() {
    }

    override fun newInstance(): Script {
        return this
    }
}