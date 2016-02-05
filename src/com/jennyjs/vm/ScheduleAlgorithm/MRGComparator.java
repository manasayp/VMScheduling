package com.jennyjs.vm.ScheduleAlgorithm;

import com.jennyjs.vm.VCPU.VirtualCPU;

import java.util.Comparator;

/**
 * Created by jenny on 11/21/15.
 */
public class MRGComparator implements Comparator<VirtualCPU> {

    @Override
    public int compare(VirtualCPU o1, VirtualCPU o2) {
        // put IO task in the front
        if (o1.task.taskType.getPriority() != o2.task.taskType.getPriority()){
            return o2.task.taskType.getPriority() - o1.task.taskType.getPriority();
        }
        // then sort according to VCPU priority
        if (o1.p.priorityLevel() != o2.p.priorityLevel()) {
            return o1.p.priorityLevel() - o2.p.priorityLevel();
        }

        // then sort according to clusterID
        if (o1.clusterId != o2.clusterId){
            return o1.clusterId - o2.clusterId;
        }
        return 0;
    }
}
