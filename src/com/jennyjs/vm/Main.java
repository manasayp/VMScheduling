package com.jennyjs.vm;

import com.jennyjs.vm.PCPU.PCPUManager;
import com.jennyjs.vm.PCPU.PhysicalCPU;
import com.jennyjs.vm.Task.TaskGenerator;
import com.jennyjs.vm.Util.Constants;
import com.jennyjs.vm.Util.ParseResult;
import com.jennyjs.vm.Util.ReadFile;
import com.jennyjs.vm.VCPU.Dom0Manager;
import com.jennyjs.vm.VCPU.VCPUManager;
import com.jennyjs.vm.VCPU.VirtualCPU;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;


/**
 * Created by jenny on 11/14/15.
 */
class Main {

    public static void main(String[] args) throws InterruptedException {
        final ParseResult parseResult;
        try {
            URL path = Main.class.getResource("input.json");
            parseResult = ReadFile.parseFile(path.getFile());
        } catch (Exception e) {
            System.out.println("Can't read file! " + e);
            return;
        }

        try {
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output_" + System.currentTimeMillis() + "_" + parseResult.getType().name() + ".txt")), true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        VirtualCPU.parseResult = parseResult;

        final Thread taskGeneratorThread = new Thread(new TaskGenerator(parseResult.getTasks()));
        taskGeneratorThread.run();

        //load task to VCPU, put the VCPUs into the run queue and sort based on MRG
        for (VirtualCPU virtualCPU : parseResult.getVirtualCPUs()) {
            VCPUManager.VCPUConnectorQueue.getInstance().add(virtualCPU);
        }

        VCPUManager.init(parseResult.getType());
        Dom0Manager.getInstance().start();
        VCPUManager.getInstance().start();

        // create several PCPUs and put them into the PCPU queue
        for (int j = 0; j < Constants.PCPU_NUMBER; j++){
            PCPUManager.getInstance().addPCUP(new PhysicalCPU(j));
        }

        // allocate the vCPUs in the runQueue to the idle pCPUs
        PCPUManager.getInstance().run();
    }
}
