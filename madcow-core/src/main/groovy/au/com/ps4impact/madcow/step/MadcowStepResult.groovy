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

package au.com.ps4impact.madcow.step;

/**
 * A result of a step.
 *
 * @author Gavin Bunney
 */
class MadcowStepResult {

    enum StatusType {
        PASS,
        FAIL,
        PARSE_ERROR,
        NO_OPERATION,
        NOT_YET_EXECUTED,
    }

    StatusType status;
    String message;
    String detailedMessage;
    boolean hasResultFile;
    boolean hasScreenshot;

    MadcowStepResult(StatusType status, String message = null) {
        this.status = status;
        this.message = message;
        this.hasResultFile = false;
        this.hasScreenshot = false;
    }
    
    public static MadcowStepResult PASS(String message = null) {
        return new MadcowStepResult(MadcowStepResult.StatusType.PASS, message);
    }

    public static MadcowStepResult FAIL(String message = null) {
        return new MadcowStepResult(MadcowStepResult.StatusType.FAIL, message);
    }

    public static MadcowStepResult PARSE_ERROR(String message = null) {
        return new MadcowStepResult(MadcowStepResult.StatusType.PARSE_ERROR, message);
    }

    public static MadcowStepResult NO_OPERATION(String message = null) {
        return new MadcowStepResult(MadcowStepResult.StatusType.NO_OPERATION, message);
    }

    public static MadcowStepResult NOT_YET_EXECUTED(String message = null) {
        return new MadcowStepResult(MadcowStepResult.StatusType.NOT_YET_EXECUTED, message);
    }

    public boolean failed() {
        return status == StatusType.FAIL || status == StatusType.PARSE_ERROR;
    }
    
    public boolean parseError() {
        return status == StatusType.PARSE_ERROR;
    }

    public boolean passed() {
        return status == StatusType.PASS;
    }

    public boolean noOperation() {
        return status == StatusType.NO_OPERATION;
    }

    public boolean notYetExecuted() {
        return status == StatusType.NOT_YET_EXECUTED;
    }

    String toString() {
        return "Status: $status | Message: $message";
    }
}