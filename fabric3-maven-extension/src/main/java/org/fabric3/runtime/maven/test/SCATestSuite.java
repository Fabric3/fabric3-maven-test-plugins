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

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.ReporterException;
import org.apache.maven.surefire.report.ReporterManager;
import org.apache.maven.surefire.report.ReporterManagerFactory;
import org.apache.maven.surefire.suite.SurefireTestSuite;
import org.apache.maven.surefire.testset.TestSetFailedException;

/**
 *
 */
public class SCATestSuite implements SurefireTestSuite {
    private final Map<String, SCATestSet> testSets = new HashMap<>();
    private int testSetCount = 0;
    private int testCount = 0;

    public void add(SCATestSet testSet) {
        testSets.put(testSet.getName(), testSet);
        testSetCount += 1;
        testCount += testSet.getTestCount();
    }

    public void execute(ReporterManagerFactory reporterManagerFactory, ClassLoader classLoader) throws ReporterException, TestSetFailedException {
        ReporterManager reporterManager = reporterManagerFactory.createReporterManager();
        for (SCATestSet testSet : testSets.values()) {
            execute(testSet, reporterManager, classLoader);
        }
    }

    public void execute(String testSetName, ReporterManagerFactory reporterManagerFactory, ClassLoader classLoader)
            throws ReporterException, TestSetFailedException {
        ReporterManager reporterManager = reporterManagerFactory.createReporterManager();
        for (SCATestSet testSet : testSets.values()) {
            execute(testSet, reporterManager, classLoader);
        }
    }

    public int getNumTests() {
        return testCount;
    }

    public int getNumTestSets() {
        return testSetCount;
    }

    protected void execute(SCATestSet testSet, ReporterManager reporterManager, ClassLoader classLoader) throws ReporterException, TestSetFailedException {
        reporterManager.testSetStarting(new ReportEntry(this, testSet.getName(), "Starting"));
        testSet.execute(reporterManager, classLoader);
        reporterManager.testSetCompleted(new ReportEntry(this, testSet.getName(), "Completed"));
        reporterManager.reset();
    }

    public Map<?, ?> locateTestSets(ClassLoader classLoader) throws TestSetFailedException {
        throw new UnsupportedOperationException();
    }
}
