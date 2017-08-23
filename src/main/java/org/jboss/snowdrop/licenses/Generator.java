/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.snowdrop.licenses;

import org.jboss.snowdrop.licenses.properties.GeneratorProperties;
import org.jboss.snowdrop.licenses.xml.LicenseSummary;

import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class Generator {

    public static void main(String... args) throws Exception {
        Properties executionProperties = argsToProperties(args);
        GeneratorProperties generatorProperties;
        if (executionProperties.getProperty("generatorProperties") == null) {
            generatorProperties = new GeneratorProperties();
        } else {
            generatorProperties = new GeneratorProperties(executionProperties.getProperty("generatorProperties"));
        }

        LicenseSummaryFactory licenseSummaryFactory = new LicenseSummaryFactory(generatorProperties);
        LicenseFilesManager licenseFilesManager = new LicenseFilesManager();

        LicenseSummary licenseSummary = licenseSummaryFactory.getLicenseSummary(executionProperties.getProperty("groupId"),
                executionProperties.getProperty("artifactId"), executionProperties.getProperty("version"),
                executionProperties.getProperty("type", "jar"));

        licenseFilesManager.createLicensesXml(licenseSummary, executionProperties.getProperty("destination"));
        licenseFilesManager.createLicensesHtml(licenseSummary, executionProperties.getProperty("destination"));
    }

    private static Properties argsToProperties(String... args) {
        Properties properties = new Properties();
        Arrays.stream(args)
                .map(s -> s.replace("-D", ""))
                .filter(s -> s.contains("="))
                .map(s -> s.split("="))
                .filter(a -> a.length == 2)
                .forEach(a -> properties.put(a[0], a[1]));

        Objects.requireNonNull(properties.getProperty("groupId"), "'groupId' is required");
        Objects.requireNonNull(properties.getProperty("artifactId"), "'artifactId' is required");
        Objects.requireNonNull(properties.getProperty("version"), "'version' is required");
        Objects.requireNonNull(properties.getProperty("destination"),  "'destination' is required");

        return properties;
    }

}
