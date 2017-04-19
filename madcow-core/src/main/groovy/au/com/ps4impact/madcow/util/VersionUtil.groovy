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

    public static String version = "?";
    public static int buildNumber = 0;
    public static String buildTag = "";
    public static String commitHash = "";
    public static DateTime timestamp = new DateTime();

    static {
        try {
            List<String> lines = ResourceFinder.locateResourceOnClasspath(VersionUtil.class.classLoader, VERSION_FILENAME).URL.readLines();
            lines.each { line ->
                String[] parts = line.split('=');
                if ('madcow.version'.equals(parts[0].trim())) {
                    version = parts[1].trim();
                } else if ('madcow.build'.equals(parts[0].trim())) {
                    buildNumber = parts[1].trim() as int;
                } else if ('madcow.tag'.equals(parts[0].trim())) {
                    buildTag = parts[1].trim();
                } else if ('madcow.commit'.equals(parts[0].trim())) {
                    commitHash = parts[1].trim()
                    if (commitHash.length()>7){
                        commitHash = commitHash.substring(0,7);
                    }
                } else if ('madcow.buildTimestamp'.equals(parts[0].trim())) {
                    DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
                    timestamp = parser.parseDateTime(parts[1].trim());
                }
            }
        } catch (ignored) { }
    }

    static String getVersionString() {
        return "v${version} build (${buildNumber})";
    }

    static String getFullVersionString() {
        def out = new StringBuffer()
        out << '|'
        out << ' Madcow '.center(45, "*")
        out << '|' << '\n'
        out << '| Version:  '.padRight(43-version.length()+2)
        out << "${version} "
        out << '|'.padRight(46-version.length()+2) << '\n'
        out << '| Build:    '.padRight(43-String.valueOf(buildNumber).length()+2)
        out << "${buildNumber} "
        out << '|'.padRight(46-String.valueOf(buildNumber).length()+2) << '\n'
        out << '| Release:  '.padRight(43-buildTag.length()+2)
        out << "${buildTag} "
        out << '|'.padRight(46-buildTag.length()+2) << '\n'
        out << '| Commit #: '.padRight(43-commitHash.length()+2)
        out << "${commitHash} "
        out << '|'.padRight(46-commitHash.length()+2) << '\n'
        out << '| Built On: '.padRight(43-timestamp.toString().length()+2)
        out << "${timestamp} "
        out << '|'.padRight(46-timestamp.toString().length()+2) << '\n'
        out << '|'
        out << '********'.center(45, "*")
        out << '|' << '\n'
    }
}
