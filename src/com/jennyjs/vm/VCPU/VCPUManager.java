package com.jennyjs.vm.VCPU;

import com.jennyjs.vm.ScheduleAlgorithm.CreditComparator;
import com.jennyjs.vm.ScheduleAlgorithm.MRGComparator;
import com.jennyjs.vm.ScheduleAlgorithm.VCPUScheduler;
import com.jennyjs.vm.Task.Task;
import com.jennyjs.vm.Task.TaskQueue;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jenny on 11/15/15.
 */
public class VCPUManager extends Thread {

    private final ScheduleType type;

    public ScheduleType getType() {
        return type;
    }

    public enum ScheduleType {
        MRG(new MRGComparator()),
        CREDIT(new CreditComparator());

        private final Comparator<VirtualCPU> comparator;

        ScheduleType(Comparator<VirtualCPU> comparator){
            this.comparator = comparator;
        }

        public Comparator<VirtualCPU> getComparator(){
            return this.comparator;
        }

        private static final Map<String, ScheduleType> typeByName;

        static {
            Map<String, ScheduleType> tmpMap = new HashMap<>();
            for (ScheduleType scheduleType : values()){
                tmpMap.put(scheduleType.name(), scheduleType);
            }
            typeByName = Collections.unmodifiableMap(tmpMap);
        }

        public static ScheduleType mapFromString(String str){
            return typeByName.get(str);
        }
    }

    private static VCPUManager vcpuManager;

    public static void init(final ScheduleType type){
        if (vcpuManager == null){
            vcpuManager = new VCPUManager(type);
        }
    }

    public static VCPUManager getInstance(){
        if (vcpuManager == null) {
            throw new IllegalArgumentException("VCPUManager singleton hasn't been initialized");
        }
        return vcpuManager;
    }

    private VCPUManager(ScheduleType type){
        this.type = type;
        VCPUScheduler.init(type.getComparator());
    }

    @Override
    public void run() {

        while (true){
            try {
                Task task = TaskQueue.getInstance().poll();
                VirtualCPU virtualCPU = VCPUConnectorQueue.getInstance().poll();

                if (!virtualCPU.isBusy){
                    virtualCPU.loadTask(task);
                }

                if (task.taskType == Task.TaskType.IoTask){
                    Dom0Manager.Dom0Queue.getInstance().add(virtualCPU);
                } else {
                    VCPUScheduler.getInstance().addVcpu(virtualCPU);
                }
            } catch (InterruptedException e){
                System.out.print(e.getMessage());
                break;
            }
        }
    }

    /**
     * Created by jenny on 11/15/15.
     */
    public static class VCPUConnectorQueue {
        private final BlockingQueue<VirtualCPU> queue = new LinkedBlockingQueue<>();
        private static VCPUManager.VCPUConnectorQueue VCPUConnectorQueue;

        public static VCPUManager.VCPUConnectorQueue getInstance(){
            if (VCPUConnectorQueue == null){
                VCPUConnectorQueue = new VCPUConnectorQueue();
            }
            return VCPUConnectorQueue;
        }

        public void add(VirtualCPU virtualCPU){
            this.queue.add(virtualCPU);
        }

        public VirtualCPU poll() throws InterruptedException {
            return this.queue.take();
        }
    }
}
