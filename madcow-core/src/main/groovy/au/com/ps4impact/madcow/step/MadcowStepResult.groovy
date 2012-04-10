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

    MadcowStepResult(StatusType status, String message = null) {
        this.status = status;
        this.message = message;
        this.hasResultFile = false;
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

    public static MadcowStepResult NOT_YET_EXECUTED() {
        return new MadcowStepResult(MadcowStepResult.StatusType.NOT_YET_EXECUTED, null);
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