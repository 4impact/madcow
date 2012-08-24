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

import org.apache.commons.lang3.StringUtils;

/**
 * Collection of formatting utilities.
 *
 * @author: Gavin Bunney
 */
class FormatUtil {

    /**
     * Converts a Map to a String.
     */
    static String convertMapToString(Map mapToConvert) {
        String line = '';
        mapToConvert.each { key, value ->

            String valueString
            if ((value instanceof String) || (value instanceof GString)) {
                valueString = "'$value'";
            } else if (value instanceof List) {
                valueString = FormatUtil.convertListToString(value as List)
            } else {
                valueString = FormatUtil.convertMapToString(value as Map)
            }

            line += "'$key' : ${valueString}, "
        }

        if (line != '') {
            line = StringUtils.substringBeforeLast(line, ', ');
        }

        return "[$line]"
    }

    /**
     * Converts a List to a String.
     */
    static String convertListToString(List listToConvert) {
        String quotedList = '';
        listToConvert.each { String val -> quotedList += "'${val}', " }

        if (quotedList != '') {
            quotedList = StringUtils.substringBeforeLast(quotedList, ', ');
        }
        return "[$quotedList]"
    }
}
