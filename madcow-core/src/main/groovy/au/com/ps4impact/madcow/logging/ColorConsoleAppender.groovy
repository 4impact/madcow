/*
 * Copyright 2015 4impact, Brisbane, Australia
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

package au.com.ps4impact.madcow.logging

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

import java.io.PrintStream;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.fusesource.jansi.AnsiConsole;

/**
 * Color console appender.
 */
class ColorConsoleAppender extends ConsoleAppender {

    String gTarget = null;
    boolean usingStdErr;

    private Map<Level, String> levelToColor = new HashMap<Level, String>();
    private String gPattern = "";
    private boolean gPatternHighlight = false;
    static final String HIGHLIGHT_START = "{highlight}";
    static final String HIGHLIGHT_END = "{/highlight}";

    public static final String BLUE         = '\u001B[0;34m';
    public static final String GREEN        = '\u001B[0;32m';
    public static final String CYAN         = '\u001B[0;36m';
    public static final String RED          = '\u001B[0;31m';
    public static final String DARK_GRAY    = '\u001B[1;30m';
    public static final String LIGHT_BLUE   = '\u001B[1;34m';
    public static final String LIGHT_GREEN  = '\u001B[1;32m';
    public static final String LIGHT_CYAN   = '\u001B[1;36m';
    public static final String LIGHT_RED    = '\u001B[1;31m';
    public static final String LIGHT_PURPLE = '\u001B[1;35m';
    public static final String YELLOW       = '\u001B[1;33m';
    public static final String WHITE        = '\u001B[1;37m';

    protected static final String COLOR_RESET = "\u001b[0m";

    {
        // default colors
        levelToColor.put(Level.FATAL, RED);
        levelToColor.put(Level.ERROR, RED);
        levelToColor.put(Level.WARN,  YELLOW);
        levelToColor.put(Level.INFO,  LIGHT_BLUE);
        levelToColor.put(Level.DEBUG, LIGHT_CYAN);
        levelToColor.put(Level.TRACE, LIGHT_CYAN);
    }

    public ColorConsoleAppender() {
        super();
    }

    public ColorConsoleAppender(Layout layout) {
        super(layout);
    }

    public ColorConsoleAppender(Layout layout, String target) {
        super(layout, target);
    }

    public void setFatalColour(String value) {
        levelToColor.put(Level.FATAL, value.replace("{esc}", "\u001b"));
    }

    public void setErrorColour(String value) {
        levelToColor.put(Level.ERROR, value.replace("{esc}", "\u001b"));
    }

    public void setWarnColour(String value) {
        levelToColor.put(Level.WARN, value.replace("{esc}", "\u001b"));
    }

    public void setInfoColour(String value) {
        levelToColor.put(Level.INFO, value.replace("{esc}", "\u001b"));
    }

    public void setDebugColour(String value) {
        levelToColor.put(Level.DEBUG, value.replace("{esc}", "\u001b"));
    }

    public void setTraceColour(String value) {
        levelToColor.put(Level.TRACE, value.replace("{esc}", "\u001b"));
    }

    protected String getColour(Level level) {
        String result = levelToColor.get(level);
        if (null == result)
            return levelToColor.get(Level.ERROR);
        return result;
    }

    public void setPassThrough(boolean value) {
        System.setProperty("jansi.passthrough", value ? "true" : "false");
    }

    public void setStrip(boolean value) {
        System.setProperty("jansi.strip", value ? "true" : "false");
    }

    @Override
    protected void subAppend(LoggingEvent event) {
        @SuppressWarnings("resource")
        // Eclipse complains about this not being closed, but this is stdout/stderr.
        PrintStream currentOutput = usingStdErr ? AnsiConsole.err : AnsiConsole.out;

        if (!hackPatternString()) {
            currentOutput.print(getColour(event.getLevel()));
            currentOutput.print(getLayout().format(event));
        } else {
            String color = getColour(event.getLevel());
            currentOutput.print(getLayout().format(event).replace(HIGHLIGHT_START, color));
        }

        if (immediateFlush)
            currentOutput.flush();
    }

    /*
     * Adds a "reset color" before the newline to prevent some ugly artifacts
     */
    protected boolean hackPatternString() {
        String theTarget = getTarget();
        if (gTarget != theTarget) // I really want to have the same object, not just equal content
            usingStdErr = SYSTEM_ERR.equalsIgnoreCase(theTarget);

        EnhancedPatternLayout enhancedPatternLayout = null;
        PatternLayout patternLayout = null;
        String pattern;

        Class<?> c = this.getLayout().getClass();
        if (EnhancedPatternLayout.class.isAssignableFrom(c)) {
            enhancedPatternLayout = (EnhancedPatternLayout) this.getLayout();
            if (null == enhancedPatternLayout)
                return gPatternHighlight;
            pattern = enhancedPatternLayout.getConversionPattern();
        } else if (PatternLayout.class.isAssignableFrom(c)) {
            patternLayout = (PatternLayout) this.getLayout();
            if (null == patternLayout)
                return gPatternHighlight;
            pattern = patternLayout.getConversionPattern();
        } else
            return gPatternHighlight;

        if (gPattern.equals(pattern))
            return gPatternHighlight;

        int hiStart = pattern.indexOf(HIGHLIGHT_START);
        gPatternHighlight = (hiStart != -1);

        // If we have a {/highlight}, we put the COLOR_RESET there
        // Otherwise we put it at the end, or right before the final %n
        if (-1 != pattern.indexOf(HIGHLIGHT_END))
            gPattern = pattern.replace(HIGHLIGHT_END, COLOR_RESET);
        else if (pattern.endsWith("%n"))
            gPattern = pattern.substring(0, pattern.length() - 2) + COLOR_RESET + "%n";
        else
            gPattern = pattern + COLOR_RESET;

        if (null != enhancedPatternLayout) {
            enhancedPatternLayout.setConversionPattern(gPattern);
            this.setLayout(enhancedPatternLayout);
        }
        if (null != patternLayout) {
            patternLayout.setConversionPattern(gPattern);
            this.setLayout(patternLayout);
        }

        return gPatternHighlight;
    }

}
