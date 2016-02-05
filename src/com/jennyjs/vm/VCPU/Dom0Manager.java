package com.jennyjs.vm.VCPU;

import com.jennyjs.vm.ScheduleAlgorithm.VCPUScheduler;
import com.jennyjs.vm.Util.Constants;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jenny on 12/6/15.
 */
public class Dom0Manager extends Thread {
    private static Dom0Manager dom0Manager;
    public static Dom0Manager getInstance(){
        if (dom0Manager == null){
            dom0Manager = new Dom0Manager();
        }
        return dom0Manager;
    }

    @Override
    public void run(){
        while (true){
            VirtualCPU virtualCPU;
            try {
                virtualCPU = Dom0Queue.getInstance().take();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                return;
            }

            if (System.currentTimeMillis() - virtualCPU.startProcessingIOTaskTime >= Constants.IO_TASK_PROCESSING_TIME){
                VCPUScheduler.getInstance().addVcpu(virtualCPU);
            } else {
                // put back virtual cpu to dom0Queue
                Dom0Queue.getInstance().internalAdd(virtualCPU);
            }
        }
    }

    /**
     * Created by jenny on 12/5/15.
     */
    public static class Dom0Queue {

        private final BlockingQueue<VirtualCPU> queue = new LinkedBlockingQueue<>();
        private static Dom0Queue dom0Queue;
        public static Dom0Queue getInstance (){
            if (dom0Queue == null){
                dom0Queue = new Dom0Queue();
            }
            return dom0Queue;
        }

        public void add(VirtualCPU virtualCPU){
            virtualCPU.startProcessingIOTaskTime = System.currentTimeMillis();
            this.queue.add(virtualCPU);
        }

        private void internalAdd(VirtualCPU virtualCPU) {
            this.queue.add(virtualCPU);
        }

        public VirtualCPU take() throws InterruptedException {
            return this.queue.take();
        }

    }
}
