import core.game.node.entity.player.info.Rights;
import core.game.node.entity.player.link.IronmanMode;
import core.game.world.repository.Repository;
import core.net.IoSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class SessionDirtyDisconnectTests {

    @BeforeAll
    static void setup() {
        TestUtils.INSTANCE.preTestSetup();
    }

    @Test
    void socketCloseFailureStillQueuesPlayerForDisconnection() {
        MockPlayer player = TestUtils.INSTANCE.getMockPlayer(
            "dirty_disconnect", IronmanMode.NONE, Rights.ADMINISTRATOR, false
        );
        IoSession originalSession = player.getSession();
        IoSession session = new IoSession(new FailingCloseSelectionKey(), null);
        player.getDetails().setSession(session);
        session.setObject(player);

        try {
            session.disconnect();

            assertFalse(session.isActive());
            assertTrue(Repository.getDisconnectionQueue().contains(player.getName()));
            assertNotNull(Repository.getPlayerByName(player.getName()));
        } finally {
            Repository.getDisconnectionQueue().remove(player.getName());
            player.getDetails().setSession(originalSession);
            if (Repository.getPlayerByName(player.getName()) != null) {
                player.close();
            }
        }
    }

    private static final class FailingCloseSelectionKey extends SelectionKey {
        private final SocketChannel channel = new FailingCloseSocketChannel();
        private boolean valid = true;
        private int interestOps;

        @Override public SelectableChannel channel() { return channel; }
        @Override public Selector selector() { return null; }
        @Override public boolean isValid() { return valid; }
        @Override public void cancel() { valid = false; }
        @Override public int interestOps() { return interestOps; }
        @Override public SelectionKey interestOps(int ops) { interestOps = ops; return this; }
        @Override public int readyOps() { return 0; }
    }

    private static final class FailingCloseSocketChannel extends SocketChannel {
        private final Socket socket = new Socket() {
            @Override
            public synchronized void close() throws IOException {
                throw new IOException("simulated dirty disconnect");
            }
        };

        private FailingCloseSocketChannel() {
            super(SelectorProvider.provider());
        }

        @Override public SocketChannel bind(SocketAddress local) { return this; }
        @Override public <T> SocketChannel setOption(SocketOption<T> name, T value) { return this; }
        @Override public <T> T getOption(SocketOption<T> name) { return null; }
        @Override public SocketChannel shutdownInput() { return this; }
        @Override public SocketChannel shutdownOutput() { return this; }
        @Override public Socket socket() { return socket; }
        @Override public boolean isConnected() { return true; }
        @Override public boolean isConnectionPending() { return false; }
        @Override public boolean connect(SocketAddress remote) { return true; }
        @Override public boolean finishConnect() { return true; }
        @Override public SocketAddress getRemoteAddress() { return new InetSocketAddress("127.0.0.1", 43595); }
        @Override public int read(ByteBuffer dst) { return -1; }
        @Override public long read(ByteBuffer[] dsts, int offset, int length) { return -1; }
        @Override public int write(ByteBuffer src) { return src.remaining(); }
        @Override public long write(ByteBuffer[] srcs, int offset, int length) { return 0; }
        @Override public SocketAddress getLocalAddress() { return new InetSocketAddress("127.0.0.1", 43595); }
        @Override public Set<SocketOption<?>> supportedOptions() { return Collections.emptySet(); }
        @Override protected void implCloseSelectableChannel() { }
        @Override protected void implConfigureBlocking(boolean block) { }
    }
}
