/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2014 OpenPlans
 * (c) 2008-2010 GeoSolutions
 *
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 *
 * Original from GeoWebCache 1.5.1 under a LGPL license
 */

package org.geoserver.platform.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import javax.servlet.ServletContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.util.IOUtils;
import org.springframework.web.context.ServletContextAware;

/**
 * A lock provider based on file system locks 一种基于文件系统锁的锁提供程序
 *
 * @author Andrea Aime - GeoSolutions
 */
public class FileLockProvider implements LockProvider, ServletContextAware {

    public static Log LOGGER = LogFactory.getLog(FileLockProvider.class);

    private File root;
    /** The wait to occur in case the lock cannot be acquired 在无法获取锁的情况下发生的等待 */
    int waitBeforeRetry = 20;
    /** max lock attempts 最大锁定尝试次数 */
    int maxLockAttempts = 120 * 1000 / waitBeforeRetry;

    MemoryLockProvider memoryProvider = new MemoryLockProvider();

    public FileLockProvider() {
        // base directory obtained from servletContext
        // 从servletContext获得的基本目录
    }

    public FileLockProvider(File basePath) {
        this.root = basePath;
    }

    @Override
    public Resource.Lock acquire(final String lockKey) {
        // first off, synchronize among threads in the same jvm (the nio locks won't lock threads in
        // the same JVM)
        // 首先，在同一个jvm中的线程之间进行同步（nio锁不会锁定同一个jvm中的线程）
        final Resource.Lock memoryLock = memoryProvider.acquire(lockKey);

        //
        // 然后在不同进程之间同步
        final File file = getFile(lockKey);
        try {
            FileOutputStream currFos = null;
            FileLock currLock = null;
            try {
                // try to lock
                // 试图锁定
                int count = 0;
                while (currLock == null && count < maxLockAttempts) {
                    // the file output stream can also fail to be acquired due to the other nodes
                    // deleting the file
                    // 由于其他节点删除文件，也可能无法获取文件输出流
                    currFos = new FileOutputStream(file);
                    try {
                        currLock = currFos.getChannel().lock();
                    } catch (OverlappingFileLockException e) {
                        IOUtils.closeQuietly(currFos);
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ie) {
                            // ok, moving on
                            // 好的，继续
                        }
                    } catch (IOException e) {
                        // this one is also thrown with a message "avoided fs deadlock"
                        // 这个函数还抛出一条消息“避免fs死锁”
                        IOUtils.closeQuietly(currFos);
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ie) {
                            // 好的，继续
                        }
                    }
                    count++;
                }

                // verify we managed to get the FS lock
                // 验证我们是否成功获取了FS锁
                if (count >= maxLockAttempts) {
                    throw new IllegalStateException(
                            "Failed to get a lock on key "
                                    + lockKey
                                    + " after "
                                    + count
                                    + " attempts");
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(
                            "Lock "
                                    + lockKey
                                    + " acquired by thread "
                                    + Thread.currentThread().getId()
                                    + " on file "
                                    + file);
                }

                // store the results in a final variable for the inner class to use
                // 将结果存储在最终变量中，以便内部类使用
                final FileOutputStream fos = currFos;
                final FileLock lock = currLock;

                // nullify so that we don't close them, the locking occurred as expected
                // 使无效以便我们不关闭它们，锁定按预期进行
                currFos = null;
                currLock = null;

                return new Resource.Lock() {

                    boolean released;

                    @Override
                    public void release() {
                        if (released) {
                            return;
                        }

                        try {
                            released = true;
                            if (!lock.isValid()) {
                                // do not crap out, locks usage is only there to prevent duplication
                                // of work
                                // 别废话，锁的使用只是为了防止重复工作
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug(
                                            "Lock key "
                                                    + lockKey
                                                    + " for releasing lock is unkonwn, it means "
                                                    + "this lock was never acquired, or was released twice. "
                                                    + "Current thread is: "
                                                    + Thread.currentThread().getId()
                                                    + ". "
                                                    + "Are you running two instances in the same JVM using NIO locks? "
                                                    + "This case is not supported and will generate exactly this error message");
                                    return;
                                }
                            }
                            try {
                                lock.release();
                                IOUtils.closeQuietly(fos);
                                file.delete();

                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug(
                                            "Lock "
                                                    + lockKey
                                                    + " released by thread "
                                                    + Thread.currentThread().getId());
                                }
                            } catch (IOException e) {
                                throw new IllegalStateException(
                                        "Failure while trying to release lock for key " + lockKey,
                                        e);
                            }
                        } finally {
                            memoryLock.release();
                        }
                    }

                    @Override
                    public String toString() {
                        return "FileLock " + file.getName();
                    }
                };
            } finally {
                if (currLock != null) {
                    currLock.release();
                    memoryLock.release();
                }
                IOUtils.closeQuietly(currFos);
                file.delete();
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failure while trying to get lock for key " + lockKey, e);
        }
    }

    private File getFile(String lockKey) {
        // avoid same directory as GWC
        // 避免使用与GWC相同的目录
        File locks = new File(root, "filelocks");
        locks.mkdirs();
        String sha1 = DigestUtils.sha1Hex(lockKey);
        return new File(locks, sha1 + ".lock");
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        String data = GeoServerResourceLoader.lookupGeoServerDataDirectory(servletContext);
        if (data != null) {
            root = new File(data);
        } else {
            throw new IllegalStateException("Unable to determine data directory");
        }
    }

    @Override
    public String toString() {
        return "FileLockProvider " + root;
    }
}
