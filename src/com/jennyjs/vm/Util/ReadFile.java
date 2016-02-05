package com.jennyjs.vm.Util;

import com.jennyjs.vm.Task.Task;
import com.jennyjs.vm.VCPU.VCPUManager;
import com.jennyjs.vm.VCPU.VirtualCPU;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manasa on 12/4/2015.
 */
public class ReadFile {

    public static ParseResult parseFile(final String pathToFile) throws IOException, ParseException {
        int numTasks;
        int numVms;
        int numVcpu;
        int numGroups;
        final List<Integer> grpCredits = new ArrayList<>();
        final List<Integer> vcpuCredits = new ArrayList<>();
        final List<Integer> vmsTotalVcpuWt = new ArrayList<>();
        final List<Task> taskInfo = new ArrayList<>();
        final List<VirtualCPU> vcpus = new ArrayList<>();
        final VCPUManager.ScheduleType type;

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(pathToFile));
        JSONObject jsonObject = (JSONObject) obj;

        numVms = ((Number)jsonObject.get("numVM")).intValue();
        JSONArray vCpulst = (JSONArray)jsonObject.get("vcpuCredits");
        numVcpu = vCpulst.size();
        JSONArray groupLst = (JSONArray)jsonObject.get("groupCredits");
        numGroups = groupLst.size();
        JSONArray taskArr = (JSONArray)jsonObject.get("groups");
        type = VCPUManager.ScheduleType.mapFromString((String) jsonObject.get("algo"));


        if((numVms > Constants.MAX_VM) || (numVcpu > Constants.MAX_VCPU) || (numGroups > Constants.CLUSTER_NUMBER)){
             System.out.println("Invalid Input[Max VM Supported = 10, MAX_VCPU Supported = 50, MAX_GROUPS Supported= 3 ]");
             System.exit(0);
        }
        for (int i = 0; i < numVms; i++) {
            vmsTotalVcpuWt.add(0);
        }

        int taskId = 0;
        for (Object aTaskArr : taskArr) {
            JSONObject task = (JSONObject) aTaskArr;
            //long totalTime, TaskType taskType, int taskID, int groupID
            int numOftasks = ((Number) task.get("tasksNum")).intValue();
            for (int i = 0; i < numOftasks; i++){
                taskInfo.add(new Task(
                        ((Number) task.get("exeTime")).intValue(),
                        Task.TaskType.mapFromString((String) task.get("type")),
                        taskId++,
                        ((Number) task.get("groupId")).intValue()));
            }
        }
        numTasks = taskInfo.size();

        for (Object aGroupLst : groupLst) {
            grpCredits.add(((Number) aGroupLst).intValue());
        }

        for (Object aVCpulst : vCpulst) {
            vcpuCredits.add(((Number) aVCpulst).intValue());
        }


        int vCpuId = 0,vmId = 0;

        //Configuring the number of vcpu per vm
        int vcpuPerVm = (int)Math.ceil((double) numVcpu / numVms);

        //create VCPUs and add to VCPUQueue.
        for(int i = 0; i < numVcpu; i++){
            int weight = vcpuCredits.get(i);
            if(vCpuId >= vcpuPerVm) {
                vCpuId = 0;
                vmId++;
            }
            vcpus.add(new VirtualCPU(vmId, vCpuId, weight));
            vmsTotalVcpuWt.set(vmId, vmsTotalVcpuWt.get(vmId) + weight);
            vCpuId++;
        }

        return new ParseResult(numTasks, grpCredits, vmsTotalVcpuWt, taskInfo, vcpus,type);
    }
}
