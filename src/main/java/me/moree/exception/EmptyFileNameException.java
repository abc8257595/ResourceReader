package me.moree.exception;

/**
 * Created by MORE-E on 3/16/2017.
 */
public class EmptyFileNameException extends RuntimeException {
    public EmptyFileNameException() {
        super("Filename is null or empty!");
    }
}
