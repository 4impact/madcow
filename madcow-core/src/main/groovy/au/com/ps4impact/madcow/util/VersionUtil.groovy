/*
 * Copyright 2012 4impact, Brisbane, Australia
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package au.com.ps4impact.madcow.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

/**
 * Utility to handle reading the madcow version file information.
 *
 * @author: Gavin Bunney
 */
public class VersionUtil {

    protected static final String VERSION_FILENAME = "madcow.version";

    public static String version;
    public static int buildNumber;
    public static DateTime timestamp;

    static {
        List<String> lines = ResourceFinder.locateResourceOnClasspath(VersionUtil.class.classLoader, VERSION_FILENAME).URL.readLines();
        lines.each { line ->
            String[] parts = line.split('=');
            if ('madcow.version'.equals(parts[0].trim())) {
                version = parts[1].trim();
            } else if ('madcow.build'.equals(parts[0].trim())) {
                buildNumber = parts[1].trim() as int;
            } else if ('madcow.buildTimestamp'.equals(parts[0].trim())) {
                DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
                timestamp = parser.parseDateTime(parts[1].trim());
            }
        }
    }

    static String getVersionString() {
        return "v${version} (${buildNumber})";
    }
}
