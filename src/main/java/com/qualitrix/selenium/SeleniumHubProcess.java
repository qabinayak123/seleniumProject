package com.qualitrix.selenium;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SeleniumHubProcess implements Runnable {

    private ProcessBuilder processBuilder;
    private CountDownLatch countDownLatch;
    private String processName;
    private Process process;

    /**
     * This is a constructor.
     *
     * @param countDownLatch - CountDownLatch
     * @param processName    - processName
     * @param commands       - commands
     */
    public SeleniumHubProcess(CountDownLatch countDownLatch, String processName,
                              String[] commands) {
        this.processName = processName;
        this.processBuilder = new ProcessBuilder(commands);
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println("---- Thread " + processName + " Service ----");
        try {
            this.process = processBuilder.start();
            // blocked for 30 seconds:(
            boolean exitCode = process.waitFor(20, TimeUnit.SECONDS);
            System.out.println("\n [" + this.processName + "] "
                + "Exited with error code : " + exitCode);
            this.countDownLatch.countDown();
            BufferedReader reader1 = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));
            String line1;
            while ((line1 = reader1.readLine()) != null) {
                System.out.println("[" + this.processName + "][ERROR] " + line1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyProcess() {
        this.process.destroyForcibly();
    }
}