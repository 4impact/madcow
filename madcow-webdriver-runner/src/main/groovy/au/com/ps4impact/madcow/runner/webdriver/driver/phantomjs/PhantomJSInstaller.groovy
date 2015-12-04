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

package au.com.ps4impact.madcow.runner.webdriver.driver.phantomjs

import org.apache.log4j.Logger

import java.io.*;

/**
 * PhantomJS Installer unpacks PhantomJS into the Java tmp dir (e.g. /tmp/)
 * to makes sure you don't have to separately install phantom.
 *
 * Graciously borrowed from: https://github.com/anthavio/phanbedder
 */
class PhantomJSInstaller {

    protected static final Logger LOG = Logger.getLogger(PhantomJSInstaller.class);
    public static final String PHANTOMJS_VERSION = '1.9.8';

    /**
     * Unpack bundled phantomjs binary into ${java.io.tmpdir}/phantomjs-${phantomjs.version}/phantomjs
     *
     * @return File of the unbundled phantomjs binary
     */
    public static File unpack() {
        String javaIoTmpdir = System.getProperty("java.io.tmpdir");

        // multiple versions can coexist, so append the phanotomjs version number
        return unpack(new File(javaIoTmpdir, "phantomjs-" + PHANTOMJS_VERSION));
    }

    /**
     * Unpack bundled phantomjs binary into specified directory
     *
     * @param directory
     * @return file path of the unbundled phantomjs binary
     */
    public static String unpack(String directory) {
        File file = unpack(new File(directory));
        return file.getAbsolutePath();
    }

    /**
     * Unpack bundled phantomjs binary into specified directory
     *
     * @param directory
     * @return File of the unbundled phantomjs binary
     */
    public static File unpack(File directory) {
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IllegalArgumentException("Failed to make target directory: ${directory}");
            }
        }

        File file;
        boolean needToChmodX;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            file = new File(directory, "phantomjs.exe");
            performUnpack("phantomjs/windows/phantomjs.exe", file);
            needToChmodX = false;
        } else if (osName.contains("mac os")) {
            file = new File(directory, "phantomjs");
            performUnpack("phantomjs/macosx/phantomjs", file);
            needToChmodX = true;

        } else if (osName.contains("linux")) {
            file = new File(directory, "phantomjs");
            // Linux has i386 or amd64
            String osarch = System.getProperty("os.arch");
            if (osarch.equals("i386")) {
                performUnpack("phantomjs/linux86/phantomjs", file);
            } else {
                performUnpack("phantomjs/linux64/phantomjs", file);
            }
            needToChmodX = true;

        } else {
            throw new IllegalArgumentException("Unsupported OS ${osName}");
        }

        if (needToChmodX) {
            if (!file.setExecutable(true)) {
                throw new IllegalArgumentException("Failed to make executable ${file}");
            }
        }

        return file;
    }

    private static void performUnpack(String resource, File target) {
        if (target.exists() && target.isFile() && target.canExecute()) {
            LOG.debug("Using PhantomJS installation at ${target.absolutePath}");
            return; // keep existing
        }

        synchronized (this) {

            // check again... since someone else might have dropped it in since :)
            if (target.exists() && target.isFile() && target.canExecute()) {
                LOG.debug("Using PhantomJS installation at ${target.absolutePath}");
                return; // keep existing
            }

            LOG.info("Installing PhantomJS to ${target.absolutePath}");
            ClassLoader classLoader = PhantomJSInstaller.class.getClassLoader(); // same jarfile -> same class loader
            InputStream stream = classLoader.getResourceAsStream(resource);
            if (stream == null) {
                throw new IllegalStateException("Resource not found ${resource} using ClassLoader ${classLoader}");
            }

            BufferedInputStream input = new BufferedInputStream(stream);
            BufferedOutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(target));
                while (input.available() > 0) {
                    byte[] buffer = new byte[input.available()];
                    input.read(buffer);
                    output.write(buffer);
                }
                output.flush();

            } catch (Exception x) {
                throw new IllegalStateException("Failed to unpack resource: ${resource} into: ${target}", x);
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException ignored) {
                        // ignore
                    }
                }
                try {
                    input.close();
                } catch (IOException ignored) {
                    // ignore
                }
            }
        }
    }
}
