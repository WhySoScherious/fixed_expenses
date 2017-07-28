package com.earnest.expenses;

enum ExitStatus {
    SUCCESS(0),
    INVALID_ARG_COUNT(-1),
    INVALID_OPTION(-2),
    FILE_NOT_FOUND(1),
    IO_ERROR_OCCURRED(2);

    private int code;

    ExitStatus(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
