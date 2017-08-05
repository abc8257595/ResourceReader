package me.moree.exception;

/**
 * Created by MORE-E on 3/19/2017.
 */
public class FileTooLargeException extends RuntimeException {
    public FileTooLargeException() {
        super("Size of file is too large to load into memory!");
    }
}
