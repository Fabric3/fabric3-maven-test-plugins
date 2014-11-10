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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.surefire.report.BriefConsoleReporter;
import org.apache.maven.surefire.report.BriefFileReporter;
import org.apache.maven.surefire.report.ReporterException;
import org.apache.maven.surefire.report.ReporterManagerFactory;
import org.apache.maven.surefire.report.RunStatistics;
import org.apache.maven.surefire.report.XMLReporter;
import org.apache.maven.surefire.suite.SurefireTestSuite;
import org.apache.maven.surefire.testset.TestSetFailedException;
import org.fabric3.plugin.api.runtime.PluginRuntime;
import org.fabric3.runtime.maven.TestSuiteFactory;

/**
 * Executes integration tests.
 */
public class TestRunner {
    private File reportsDirectory;
    private Boolean trimStackTrace;
    private Log log;
    private Boolean ignoreTestFailures;

    public TestRunner(File reportsDirectory, boolean trimStackTrace, Log log, boolean ignoreTestFailures) {
        this.reportsDirectory = reportsDirectory;
        this.trimStackTrace = trimStackTrace;
        this.log = log;
        this.ignoreTestFailures = ignoreTestFailures;
    }

    public void executeTests(PluginRuntime runtime) throws MojoExecutionException, MojoFailureException {
        SurefireTestSuite testSuite =  runtime.getComponent(TestSuiteFactory.class).createTestSuite(ignoreTestFailures);
        log.info("Executing tests...");
        boolean success = runTests(testSuite);
        if (!success) {
            String msg = "There were test failures";
            throw new MojoFailureException(msg);
        }
    }

    @SuppressWarnings({"unchecked"})
    private boolean runTests(SurefireTestSuite suite) throws MojoExecutionException {
        try {
            List definitions = new ArrayList();
            Object[] params = new Object[]{reportsDirectory, trimStackTrace};
            definitions.add(new Object[]{XMLReporter.class.getName(), params});
            definitions.add(new Object[]{BriefFileReporter.class.getName(), params});
            definitions.add(new Object[]{BriefConsoleReporter.class.getName(), new Object[]{trimStackTrace}});
            ReporterManagerFactory factory = new ReporterManagerFactory(definitions, getClass().getClassLoader());
            suite.execute(factory, null);
            RunStatistics statistics = factory.getGlobalRunStatistics();
            return statistics.getErrorSources().isEmpty() && statistics.getFailureSources().isEmpty();
        } catch (ReporterException | TestSetFailedException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

}
