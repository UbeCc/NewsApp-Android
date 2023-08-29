// 声明：本逻辑非原创
package com.java.wanghaoran.service;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * HttpManager类主要用于运行Http请求
 * 通过回调实现
 */
public class HttpManager {
    public interface Callback<R> {void complete(Result<R> res);}
    @Data
    @AllArgsConstructor
    public static class Result<R> {
        private boolean ok;
        private R result;
        private Throwable error;
        public static <T> Result<T> ofResult(T res) {return new Result<>(true, res, null);}
        public static <T> Result<T> ofError(Throwable error) {return new Result<>(false, null, error);}
    }

    private final static HttpManager instance = new HttpManager();
    private final Executor workers = Executors.newSingleThreadExecutor();
    private final Handler uiThread = new Handler(Looper.getMainLooper());

    private HttpManager() {}

    public static HttpManager getInstance() {return instance;}

    public <R> void execute(Callable<R> task, Callback<R> callback) {
        workers.execute(() -> {
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