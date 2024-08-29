package com.study.async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
@Service
public class TaskService {

    @Async("taskExecutor") //指定线程池
    public void syncTask() {
        log.info("Executing async task in thread: {}", Thread.currentThread().getName());
        try {
            Thread.sleep(3000); // 模拟耗时操作
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted", e);
        }
        log.info("Async operation completed.");
    }


    /**
     * 使用 CompletionStage 实现异步任务
     * CompletionStage 提供了一种声明式的异步编程模型，可以用来构建复杂的异步工作流。
     */
    public CompletionStage<String> syncTask1() {
        CompletableFuture<String> future = new CompletableFuture<>();

        // 模拟异步操作
        new Thread(() -> {
            try {
                Thread.sleep(3000); // 模拟耗时操作
                future.complete("Async operation completed.");
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
            }
        }).start();
        return future; // 返回CompletionStage
    }

    /**
     * 使用 CompletableFuture
     * CompletableFuture 是 CompletionStage 的一个实现，提供了更丰富的API来构建复杂的异步工作流。
     */
    public CompletionStage<String> syncTask2() {
        return CompletableFuture.supplyAsync(this::doWork)
                .thenApply(result -> "Completed: " + result);
    }

    private String doWork() {
        try {
            Thread.sleep(3000); // 模拟耗时操作
        } catch (InterruptedException e) {
            throw new IllegalStateException("Interrupted", e);
        }
        return "Async operation done.";
    }

}
