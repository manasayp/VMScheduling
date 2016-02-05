package com.jennyjs.vm.Task;

import com.jennyjs.vm.Util.Constants;

import java.util.List;

/**
 * Created by jenny on 11/14/15.
 */


public class TaskGenerator implements Runnable {
    private final List<Task> list;

    public TaskGenerator(final List<Task> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for (Task task : list) {
            try {
                Thread.sleep(Constants.TASK_GENERATING_INTERVAL);
                TaskQueue.getInstance().addTask(task);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
