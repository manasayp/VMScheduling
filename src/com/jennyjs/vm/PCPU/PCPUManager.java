package com.jennyjs.vm.PCPU;


import com.jennyjs.vm.Util.Constants;
import com.jennyjs.vm.VCPU.VirtualCPU;
import com.jennyjs.vm.ScheduleAlgorithm.PCPUComparator;
import com.jennyjs.vm.ScheduleAlgorithm.VCPUScheduler;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * Created by jenny on 11/22/15.
 */
public class PCPUManager implements Runnable {

    private final PriorityBlockingQueue<PhysicalCPU> pCPUQueue;

    private PCPUManager(Comparator<PhysicalCPU> comparator){
        pCPUQueue = new PriorityBlockingQueue<>(5, comparator);
    }


    private static PCPUManager pcpuManager;
    public static PCPUManager getInstance(){
        if (pcpuManager == null){
            pcpuManager = new PCPUManager(new PCPUComparator());
        }
        return pcpuManager;
    }

    public void addPCUP(PhysicalCPU physicalCPU){
        pCPUQueue.add(physicalCPU);
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(Constants.PCPU_NUMBER);
        //take from dom0 queue first and check, then take from VCPU queue
        while(true){
            try {
                PhysicalCPU physicalCPU = pCPUQueue.take();
                VirtualCPU virtualCPU = VCPUScheduler.getInstance().pollVcpu();
                physicalCPU.loadVCPU(virtualCPU);
                System.out.println("Loaded VCPU " + virtualCPU.vCpuId + " to PCUP " + physicalCPU.pCPUId + " task:[" + virtualCPU.task + "]");
                executor.submit(physicalCPU);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
