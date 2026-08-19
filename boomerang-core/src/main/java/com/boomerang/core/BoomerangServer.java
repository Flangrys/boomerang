package com.boomerang.core;

import com.boomerang.core.cli.BoomerangCli;
import com.boomerang.core.exceptions.InitializationException;
import com.boomerang.core.exceptions.TerminationException;
import com.boomerang.core.net.BoomerangNetworkBackend;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicReference;

public final class BoomerangServer implements Service {
    private static final Logger logger = LogManager.getLogger(BoomerangServer.class);
    private static final AtomicReference<ServerStatus> SERVER_STATUS = new AtomicReference<>(ServerStatus.STARTING);

    private final BoomerangCli boomerangCli;
    private final BoomerangEtc boomerangEtc;

    private final BoomerangNetworkBackend serverNetBackend;
    private final BoomerangRegistryBackend serverRegistryBackend;

    public BoomerangServer(BoomerangCli boomerangCli) {
        this.boomerangCli = boomerangCli;

        this.boomerangEtc = new BoomerangEtc();
        this.serverNetBackend = new BoomerangNetworkBackend();
        this.serverRegistryBackend = new BoomerangRegistryBackend();
    }

    @Override
    public void start() {
        BoomerangLock.acquireLock();

        if (!SERVER_STATUS.compareAndSet(ServerStatus.STARTING, ServerStatus.RUNNING)) {
            throw new RuntimeException("Cannot start the server in this state");
        }

        logger.info("Starting Boomerang Server...");

        try {
            logger.debug("Starting registry backend...");
            this.serverRegistryBackend.start();

            logger.debug("Starting network backend...");
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

        logger.info("Stopping Boomerang Server...");

        try {
            logger.debug("Stopping Boomerang Server...");
            this.serverNetBackend.stop();

            logger.debug("Stopping registry backend...");
            this.serverRegistryBackend.stop();

        } catch (TerminationException exc) {
            throw new RuntimeException("An exception occurred during the finish-up");
        }
    }

    public static void main(String[] args) {
        final BoomerangCli boomerangCli = BoomerangCli.parse(args);
        final BoomerangServer boomerangServer = new BoomerangServer(boomerangCli);

        final Runtime runtime = Runtime.getRuntime();

        runtime.addShutdownHook(BoomerangThread.ofPlatform("shutdown").unstarted(boomerangServer::stop));
        runtime.addShutdownHook(BoomerangThread.ofPlatform("shutdown").unstarted(BoomerangLock::revokeLock));

        boomerangServer.start();
    }
}
