package au.com.ps4impact.madcow.step;

/**
 * A result of a step.
 */
class MadcowStepResult {

    enum StatusType {
        PASS,
        FAIL,
        NO_OPERATION
    }

    StatusType status;
    String message;

    MadcowStepResult(StatusType status, String message = null) {
        this.status = status;
        this.message = message;
    }
    
    static MadcowStepResult PASS(String message = null) {
        return new MadcowStepResult(MadcowStepResult.StatusType.PASS, message);
    }

    static MadcowStepResult FAIL(String message = null) {
        return new MadcowStepResult(MadcowStepResult.StatusType.FAIL, message);
    }

    static MadcowStepResult NO_OPERATION(String message = null) {
        return new MadcowStepResult(MadcowStepResult.StatusType.NO_OPERATION, message);
    }

    boolean failed() {
        return status == StatusType.FAIL;
    }

    boolean passed() {
        return status == StatusType.PASS;
    }

    boolean noOperation() {
        return status == StatusType.NO_OPERATION;
    }

    String toString() {
        return "Status: $status | Message: $message";
    }
}