/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.sandbox.entity.base.concurrent;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javolution.util.FastList;

import org.etk.common.logging.Logger;
import org.etk.sandbox.entity.base.lang.SourceMonitored;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Sep 10, 2011  
 */
@SourceMonitored
public final class ExecutionPool {
    private static final Logger logger = Logger.getLogger(ExecutionPool.class);
    public static final ScheduledExecutorService GLOBAL_EXECUTOR = getExecutor(null, "ofbiz-config", -1, true);

    protected static class ExecutionPoolThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final String namePrefix;
        private int count = 0;

        protected ExecutionPoolThreadFactory(ThreadGroup group, String namePrefix) {
            this.group = group;
            this.namePrefix = namePrefix;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r);
            t.setDaemon(true);
            t.setPriority(Thread.NORM_PRIORITY);
            t.setName(namePrefix + "-" + count++);
            return t;
        }
    }

    @Deprecated
    public static ThreadFactory createThreadFactory(String namePrefix) {
        return createThreadFactory(null, namePrefix);
    }

    public static ThreadFactory createThreadFactory(ThreadGroup group, String namePrefix) {
        return new ExecutionPoolThreadFactory(group, namePrefix);
    }

    @Deprecated
    public static ScheduledExecutorService getExecutor(String namePrefix, int threadCount) {
        return getExecutor(null, namePrefix, threadCount, true);
    }

    public static ScheduledExecutorService getExecutor(ThreadGroup group, String namePrefix, int threadCount, boolean preStart) {
        if (threadCount == 0) {
            threadCount = 1;
        } else if (threadCount < 0) {
            int numCpus = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
            threadCount = Math.abs(threadCount) * numCpus;
        }
        ThreadFactory threadFactory = createThreadFactory(group, namePrefix);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threadCount, threadFactory);
        if (preStart) {
            executor.prestartAllCoreThreads();
        }
        return executor;
    }

    @Deprecated
    public static ScheduledExecutorService getNewExactExecutor(String namePrefix) {
        return getExecutor(null, namePrefix, -1, true);
    }

    @Deprecated
    public static ScheduledExecutorService getNewOptimalExecutor(String namePrefix) {
        return getExecutor(null, namePrefix, -2, true);
    }

    public static <F> List<F> getAllFutures(Collection<Future<F>> futureList) {
        List<F> result = FastList.newInstance();
        for (Future<F> future: futureList) {
            try {
                result.add(future.get());
            } catch (ExecutionException e) {
                logger.error(e.getMessage(), e);
            } catch (InterruptedException e) {
              logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public static void addPulse(Pulse pulse) {
        delayQueue.put(pulse);
    }

    public static void removePulse(Pulse pulse) {
        delayQueue.remove(pulse);
    }

    public static void pulseAll(Class<? extends Pulse> match) {
        Iterator<Pulse> it = delayQueue.iterator();
        while (it.hasNext()) {
            Pulse pulse = it.next();
            if (match.isInstance(pulse)) {
                it.remove();
                pulse.run();
            }
        }
    }

    static {
        ExecutionPoolPulseWorker worker = new ExecutionPoolPulseWorker();
        int processorCount = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        for (int i = 0; i < processorCount; i++) {
            Thread t = new Thread(worker);
            t.setDaemon(true);
            t.setName("ExecutionPoolPulseWorker(" + i + ")");
            t.start();
        }
    }

    private static final DelayQueue<Pulse> delayQueue = new DelayQueue<Pulse>();

    public static class ExecutionPoolPulseWorker implements Runnable {
        public void run() {
            try {
                while (true) {
                    delayQueue.take().run();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static abstract class Pulse implements Delayed, Runnable {
        protected final long expireTimeNanos;
        protected final long loadTimeNanos;

        protected Pulse(long delayNanos) {
            this(System.nanoTime(), delayNanos);
        }

        protected Pulse(long loadTimeNanos, long delayNanos) {
            this.loadTimeNanos = loadTimeNanos;
            expireTimeNanos = loadTimeNanos + delayNanos;
        }

        public long getLoadTimeNanos() {
            return loadTimeNanos;
        }

        public long getExpireTimeNanos() {
            return expireTimeNanos;
        }

        public final long getDelay(TimeUnit unit) {
            return unit.convert(expireTimeNanos - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        public final int compareTo(Delayed other) {
            long r = (expireTimeNanos - ((Pulse) other).expireTimeNanos);
            if (r < 0) return -1;
            if (r > 0) return 1;
            return 0;
        }
    }

}

