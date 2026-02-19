package com.errandly.Errandly.Exception.Custom;

public class RunnerNotFoundException extends RuntimeException {
    public RunnerNotFoundException(Long id){
        super("Runner not found with id: " + id);
    }
}
