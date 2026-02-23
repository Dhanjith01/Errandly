package com.errandly.Errandly.exception.custom;

public class RunnerNotFoundException extends RuntimeException {
    public RunnerNotFoundException(Long id){
        super("Runner not found with id: " + id);
    }
}
