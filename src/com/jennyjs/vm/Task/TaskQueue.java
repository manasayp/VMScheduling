package com.jennyjs.vm.Task;


import com.jennyjs.vm.Util.Constants;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jenny on 11/14/15.
 */
public class TaskQueue {
    final private BlockingQueue<Task> queue;
    private static TaskQueue taskQueue;

    private TaskQueue(){
        this.queue = new LinkedBlockingQueue<>(Constants.TASK_QUEUE_SIZE);
    }

    public static TaskQueue getInstance(){
        if (taskQueue == null) {
            taskQueue = new TaskQueue();
        }
        return taskQueue;
    }
    public void addTask(Task t){
        this.queue.add(t);
    }

    public Task poll() throws InterruptedException {
        return this.queue.take();
    }
}

