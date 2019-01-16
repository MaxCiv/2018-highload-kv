package ru.mail.polis.maxciv.future;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

public class Caller<T> {

    private final ExecutorService executorService;

    public Caller(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public CompletionService<T> makeAllCallsInParallel(List<Callable<T>> calls) {
        ExecutorCompletionService<T> completionService = new ExecutorCompletionService<>(executorService);
        calls.forEach(completionService::submit);
        return completionService;
    }
}
