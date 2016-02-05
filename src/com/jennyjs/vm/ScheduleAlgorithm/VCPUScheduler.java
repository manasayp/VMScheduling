package com.jennyjs.vm.ScheduleAlgorithm;

import com.jennyjs.vm.Util.Constants;
import com.jennyjs.vm.VCPU.VirtualCPU;

import java.util.Comparator;
import java.util.concurrent.*;

/**
 * Created by jenny on 11/21/15.
 */
public class VCPUScheduler {
    private final BlockingQueue<VirtualCPU> runQueue;
    private static VCPUScheduler VCPUScheduler;

    private VCPUScheduler(Comparator<VirtualCPU> comparator){
        runQueue = new PriorityBlockingQueue<>(Constants.MAX_VCPU, comparator);
    }

    public static void init(Comparator<VirtualCPU> comparator) {
        VCPUScheduler = new VCPUScheduler(comparator);
    }

    public static VCPUScheduler getInstance(){
        if (VCPUScheduler == null){
            throw new IllegalArgumentException("VCPUScheduler hasn't been initialized.");
        }
        return VCPUScheduler;
    }

    public void addVcpu(VirtualCPU virtualCPU){
        this.runQueue.add(virtualCPU);
    }

    public VirtualCPU pollVcpu () throws InterruptedException {
        return runQueue.take();
    }

}
