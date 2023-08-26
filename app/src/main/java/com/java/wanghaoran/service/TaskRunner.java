package com.java.wanghaoran.service;


import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lombok.AllArgsConstructor;
import lombok.Data;

public class TaskRunner {

    @FunctionalInterface
    public interface Callback<R> {//这个接口来自2022年科协暑培的代码
        void complete(Result<R> res);
    }

    @Data
    @AllArgsConstructor
    public static class Result<R> {//这个函数来自2022年科协暑培的代码
        private boolean ok;
        private R result;
        private Throwable error;

        public static <T> Result<T> ofResult(T res) {
            return new Result<>(true, res, null);
        }

        public static <T> Result<T> ofError(Throwable error) {
            return new Result<>(false, null, error);
        }
    }

    private final static TaskRunner instance = new TaskRunner();
    private final Executor workers = Executors.newSingleThreadExecutor();
    private final Handler uiThread = new Handler(Looper.getMainLooper());

    private TaskRunner() {//这个函数来自2022年科协暑培的代码
    }

    public static TaskRunner getInstance() {
        return instance;
    }

    public <R> void execute(Callable<R> task, Callback<R> callback) {//这个函数来自2022年科协暑培的代码
        workers.execute(() -> {
//            try {
//               Thread.sleep((long) (1000));
//           } catch (InterruptedException ignored) {
//            }
            try {
                final R res = task.call();
                uiThread.post(() -> callback.complete(Result.ofResult(res)));
            } catch (Exception e) {
                e.printStackTrace();
                uiThread.post(() -> callback.complete(Result.ofError(e)));
            }
        });
    }

}