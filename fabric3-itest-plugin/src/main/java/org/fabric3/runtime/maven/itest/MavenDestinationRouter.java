/*
 * Fabric3
 * Copyright (c) 2009-2015 Metaform Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Portions originally based on Apache Tuscany 2007
 * licensed under the Apache 2.0 license.
 */
package org.fabric3.runtime.maven.itest;

import org.apache.maven.plugin.logging.Log;
import org.fabric3.api.annotation.monitor.MonitorLevel;
import org.fabric3.api.host.monitor.DestinationRouter;
import org.fabric3.api.host.monitor.MessageFormatter;

/**
 * Forwards monitor events to the Maven logger.
 */
public class MavenDestinationRouter implements DestinationRouter {
    private Log log;

    public MavenDestinationRouter(Log log) {
        this.log = log;
    }

    public int getDestinationIndex(String name) {
        return 0;
    }

    public void send(MonitorLevel level, int destinationIndex, long timestamp, String source, String message, boolean parse, Object... args) {
        message = MessageFormatter.format(message, args);

        if (MonitorLevel.SEVERE == level) {
            if (log.isErrorEnabled()) {
                Throwable e = null;
                for (Object o : args) {
                    if (o instanceof Throwable) {
                        e = (Throwable) o;
                    }
                }
                if (message != null) {
                    log.error(message, e);
                } else {
                    log.error(e);
                }
            }
        } else if (MonitorLevel.WARNING == level) {
            if (log.isWarnEnabled()) {
                log.warn(message);
            }
        } else if (MonitorLevel.INFO == level) {
            if (log.isInfoEnabled()) {
                log.info(message);
            }
        } else if (MonitorLevel.DEBUG == level) {
            if (log.isDebugEnabled()) {
                log.debug(message);
            }
        } else if (MonitorLevel.TRACE == level) {
            if (log.isDebugEnabled()) {
                log.debug(message);
            }
        }
    }

}