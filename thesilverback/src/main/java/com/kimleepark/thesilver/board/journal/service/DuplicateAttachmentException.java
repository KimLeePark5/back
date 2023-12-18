package com.kimleepark.thesilver.board.journal.service;

public class DuplicateAttachmentException extends RuntimeException {

    public DuplicateAttachmentException() {
        super();
    }

    public DuplicateAttachmentException(String message) {
        super(message);
    }

    public DuplicateAttachmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateAttachmentException(Throwable cause) {
        super(cause);
    }
}

