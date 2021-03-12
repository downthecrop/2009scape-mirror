package core.game.world.map.path;

@FunctionalInterface
public interface ClipMaskSupplier {

    int getClippingFlag(int z, int x, int y);
}
