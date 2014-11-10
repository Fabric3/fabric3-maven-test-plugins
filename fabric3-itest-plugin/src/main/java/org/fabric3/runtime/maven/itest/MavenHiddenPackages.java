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
 */
package org.fabric3.runtime.maven.itest;

import java.util.Arrays;

import org.fabric3.api.host.runtime.HiddenPackages;

/**
 *
 */
public class MavenHiddenPackages {
    private static String[] PACKAGES;

    static {
        String[] list = HiddenPackages.getPackages();
        PACKAGES = Arrays.copyOf(list, list.length + 1);
        PACKAGES[PACKAGES.length - 1] = "org.slf4j";
    }

    public static String[] getPackages() {
        return PACKAGES;
    }
}
