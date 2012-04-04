package au.com.ps4impact.madcow.step;

/**
 * A result of a step.
 */
class MadcowStepResult {

    enum StatusType {
        PASS,
        FAIL,
        PARSE_ERROR,
        NO_OPERATION
    }

    StatusType status;
    String message;
    String detailedMessage;

    MadcowStepResult(StatusType status, String message = null) {
        this.status = status;
        this.message = message;
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

    String toString() {
        return "Status: $status | Message: $message";
    }
}