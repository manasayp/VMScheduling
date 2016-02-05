package com.jennyjs.vm.ScheduleAlgorithm;

import com.jennyjs.vm.PCPU.PhysicalCPU;

import java.util.Comparator;

/**
 * Created by jenny on 11/22/15.
 */
public class PCPUComparator implements Comparator<PhysicalCPU> {

    @Override
    public int compare(PhysicalCPU o1, PhysicalCPU o2) {
        return o1.status.pCPULevel() - o2.status.pCPULevel();
    }
}
