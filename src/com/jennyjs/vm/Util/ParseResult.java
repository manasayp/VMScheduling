package com.jennyjs.vm.Util;

import com.jennyjs.vm.Task.Task;
import com.jennyjs.vm.VCPU.VCPUManager;
import com.jennyjs.vm.VCPU.VirtualCPU;

import java.util.Collections;
import java.util.List;

/**
 * An immutable class to represent result of parsing a file
 */
public class ParseResult {
    private final int numTasks;
    private final List<Integer> grpCredits;
    private final List<Integer> vmsTotalVcpuWt;
    private final List<Task> tasks;
    private final List<VirtualCPU> virtualCPUs;
    private final VCPUManager.ScheduleType type;

    public ParseResult(final int numTasks,
                       final List<Integer> grpCredits,
                       final List<Integer> vmsTotalVcpuWt,
                       final List<Task> tasks,
                       final List<VirtualCPU> virtualCPUs,
                       final VCPUManager.ScheduleType type) {

        this.numTasks = numTasks;
        this.grpCredits = Collections.unmodifiableList(grpCredits);
        this.vmsTotalVcpuWt = Collections.unmodifiableList(vmsTotalVcpuWt);
        this.tasks = Collections.unmodifiableList(tasks);
        this.virtualCPUs = Collections.unmodifiableList(virtualCPUs);
        this.type = type;
    }

    public int getNumTasks() {
        return numTasks;
    }


    public List<Integer> getGrpCredits() {
        return grpCredits;
    }


    public List<Integer> getVmsTotalVcpuWt() {
        return vmsTotalVcpuWt;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<VirtualCPU> getVirtualCPUs() {
        return virtualCPUs;
    }

    public VCPUManager.ScheduleType getType() {
        return type;
    }
}
