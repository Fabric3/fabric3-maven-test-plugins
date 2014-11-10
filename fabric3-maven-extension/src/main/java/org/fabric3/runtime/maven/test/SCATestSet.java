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
 * Portions originally based on Apache Tuscany 2007
 * licensed under the Apache 2.0 license.
 */
package org.fabric3.runtime.maven.test;

import org.apache.maven.surefire.report.PojoStackTraceWriter;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.ReporterManager;
import org.apache.maven.surefire.report.StackTraceWriter;
import org.apache.maven.surefire.testset.SurefireTestSet;
import org.apache.maven.surefire.testset.TestSetFailedException;
import org.fabric3.spi.container.invocation.Message;
import org.fabric3.spi.container.invocation.MessageCache;
import org.fabric3.spi.container.invocation.WorkContext;
import org.fabric3.spi.container.invocation.WorkContextCache;
import org.fabric3.spi.container.wire.InvocationChain;
import org.fabric3.spi.container.wire.Wire;

/**
 *
 */
public class SCATestSet implements SurefireTestSet {
    private final String name;
    private Wire wire;
    private boolean ignoreTestFailures;

    public SCATestSet(String name, Wire wire, boolean ignoreTestFailures) {
        this.name = name;
        this.wire = wire;
        this.ignoreTestFailures =  ignoreTestFailures;
    }

    public void execute(ReporterManager reporterManager, ClassLoader loader) throws TestSetFailedException {
        Message message = MessageCache.getAndResetMessage();
        WorkContext workContext = WorkContextCache.getAndResetThreadWorkContext();
        for (InvocationChain chain : wire.getInvocationChains()) {
            String operationName = chain.getPhysicalOperation().getName();
            reporterManager.testStarting(new ReportEntry(this, operationName, name));
            try {
                message.setWorkContext(workContext);
                Message response = chain.getHeadInterceptor().invoke(message);
                if (response.isFault()) {
                    throw new TestSetFailedException(operationName, (Throwable) response.getBody());
                }

                reporterManager.testSucceeded(new ReportEntry(this, operationName, name));
            } catch (TestSetFailedException e) {
                StackTraceWriter stw = new PojoStackTraceWriter(name, operationName, e.getCause());
                reporterManager.testFailed(new ReportEntry(this, operationName, name, stw));
                if (!ignoreTestFailures) {
                    throw e;
                }
            }
            finally {
                message.reset();
                workContext.reset();
            }
        }
    }

    public int getTestCount() {
        return wire.getInvocationChains().size();
    }

    public String getName() {
        return name;
    }

    public Class<?> getTestClass() {
        throw new UnsupportedOperationException();
    }
}
