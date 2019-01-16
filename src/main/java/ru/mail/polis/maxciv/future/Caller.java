package ru.mail.polis.maxciv.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Caller<T> {

    private final ExecutorService executorService;

    public Caller(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public List<T> makeAllCallsInParallel(List<Callable<T>> calls) {
        List<T> results = new ArrayList<>();
        try {
            List<Future<T>> futures = executorService.invokeAll(calls);
            for (Future<T> future : futures) results.add(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
