package com.jennyjs.vm.VCPU;

import com.jennyjs.vm.Task.Task;
import com.jennyjs.vm.Util.ParseResult;

/**
 * Created by jenny on 11/15/15.
 */
public class VirtualCPU {
    public int clusterId;
    private final int vmId;
    final public int vCpuId;
    public int credit;
    private final int weight;
    public Priority p;
    public Task task;
    public boolean isBusy;
    public long startProcessingIOTaskTime;

    public static ParseResult parseResult;

    public enum Priority{
        under(1),
        over(2);

        private int level;
        Priority(int i) {
            this.level = i;
        }

        public int priorityLevel(){
            return level;
        }
    }


    public VirtualCPU(int vmId, int vCpuId, int weight){
        this.vmId = vmId;
        this.vCpuId = vCpuId;
        this.weight = weight;
    }

    public void loadTask(Task task){
        this.task = task;
        this.clusterId = task.groupID;
        this.isBusy = true;
        this.credit = Math.round(parseResult.getGrpCredits().get(this.clusterId - 1) * this.weight / parseResult.getVmsTotalVcpuWt().get(this.vmId));
        this.p = Priority.over;
        System.out.println("Loading task " + task.taskID + " to vCPU " + this.vCpuId);

    }

    public boolean withCreditLeft(){
        return this.credit > 0;
    }

}
