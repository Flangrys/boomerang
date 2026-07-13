package com.boomerang.core;

import com.boomerang.core.cli.BoomerangCli;
import com.boomerang.core.exceptions.InitializationException;
import com.boomerang.core.exceptions.TerminationException;
import com.boomerang.core.net.BoomerangNetworkBackend;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicReference;

public final class BoomerangServer implements Service {
    private static final Logger logger = LogManager.getLogger(BoomerangServer.class);
    private static final AtomicReference<ServerStatus> SERVER_STATUS = new AtomicReference<>(ServerStatus.STARTING);

    private static final BoomerangCli boomerangCli = new BoomerangCli();
    private static final BoomerangEtc boomerangEtc = new BoomerangEtc();

    private final BoomerangNetworkBackend serverNetBackend;

    public BoomerangServer(String[] args) {
        this.serverNetBackend = new BoomerangNetworkBackend();
    }

    @Override
    public void start() {
        BoomerangLock.acquireLock();

        if (!SERVER_STATUS.compareAndSet(ServerStatus.STARTING, ServerStatus.RUNNING)) {
            throw new RuntimeException("Cannot start the server in this state");
        }

        try {
            logger.info("Starting Boomerang Server...");

            logger.trace("Starting network backend...");
            this.serverNetBackend.start();

        } catch (InitializationException exc) {
            throw new RuntimeException("An exception occurred during the start-up", exc);
        }
    }

    @Override
    public void stop() {
        if (!SERVER_STATUS.compareAndSet(ServerStatus.RUNNING, ServerStatus.STOPPED)) {
            throw new RuntimeException("Cannot stop the server in this state");
        }

        try {
            logger.info("Stopping Boomerang Server...");
            this.serverNetBackend.stop();

        } catch (TerminationException exc) {
            throw new RuntimeException("An exception occurred during the finish-up");
        }
    }

    public static void main(String[] args) {
        final BoomerangServer boomerangServer = new BoomerangServer(args);
        final Runtime runtime = Runtime.getRuntime();

        runtime.addShutdownHook(BoomerangThread.ofPlatform("shutdown").unstarted(boomerangServer::stop));

        boomerangServer.start();
    }
}
