package com.java.wanghaoran.service;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskRunner {

    @FunctionalInterface
    public interface Callback<R> {//这个接口来自2022年科协暑培的代码
        void complete(Result<R> res);
    }

    public static class Result<R> {//这个函数来自2022年科协暑培的代码
        private boolean ok;
        private R result;
        private Throwable error;

        public Result(boolean _ok, R _result, Throwable _error) {
            boolean ok = _ok;
            R result = _result;
            Throwable error = _error;
        }

        public boolean getOk() {return ok;}
        public R getResult() {return result;}
        public Throwable getError() {return error;}

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
                uiThread.post(() -> callback.complete(Result.ofError(e)));
            }
        });
    }

}
