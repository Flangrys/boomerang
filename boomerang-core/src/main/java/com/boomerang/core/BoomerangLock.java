package com.boomerang.core;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public final class BoomerangLock {

    private static final File lockfile = new File("process.lock");

    private static FileLock lockfileLock;
    private static FileChannel lockfileChannel;

    public static void revokeLock() {
        try {
            if (lockfileLock != null) {
                lockfileLock.release();
                lockfileChannel.close();
                lockfile.delete();
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Cannot release the lockfile");
        }
    }

    public static void acquireLock() {
        try {
            boolean isLockfileDeleted = true;

            if (lockfile.exists()) {
                isLockfileDeleted = lockfile.delete();
            }

            if (!isLockfileDeleted) {
                throw new RuntimeException("Cannot delete the lockfile");
            }

            lockfileChannel = new RandomAccessFile(lockfile, "rw").getChannel();

            lockfileLock = lockfileChannel.tryLock();

            if (lockfileLock == null) {
                lockfileChannel.close();

                throw new RuntimeException("An existing process is currently running");
            }

        } catch (OverlappingFileLockException exc) {
            throw new RuntimeException("An existing process is currently running", exc);

        } catch (IOException exc) {
            throw new RuntimeException("Cannot launch this process due to an unexpected exception", exc);
        }
    }
}
