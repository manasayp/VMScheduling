package com.jennyjs.vm.ScheduleAlgorithm;

import com.jennyjs.vm.VCPU.VirtualCPU;

import java.util.Comparator;

/**
 * Created by jenny on 11/21/15.
 */
public class CreditComparator implements Comparator<VirtualCPU> {
    @Override
    public int compare(VirtualCPU o1, VirtualCPU o2) {
        return o1.p.priorityLevel() - o2.p.priorityLevel();
    }
}
