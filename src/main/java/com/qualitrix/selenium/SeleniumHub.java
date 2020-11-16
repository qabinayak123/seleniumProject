package com.qualitrix.selenium;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class SeleniumHub {


    private SeleniumHubProcess serviceProcess;
    private String host;
    private int port;
    private URL url;

    public SeleniumHub() {

    }

    /**
     * Method is to spawn selenium standalone sever.
     *
     * @param logPath     - Log Path
     * @param browserName - Browser Name
     * @param driverPath  - Driver Path
     * @return url - Endpoint to use in initiating the driver
     * @throws InterruptedException - Throws InterruptedException
     * @throws IOException          - Throws IOException
     */
    public URL start(String logPath, String browserName, String driverPath)
        throws InterruptedException, IOException {
        System.out.println("---- Starting SeleniumStandAloneServer Service ----");
        logPath = logPath + File.separator
            + "seleniumHub_log.txt";

        String servicePath = System.getProperty("user.dir")
            + File.separator;
        String proxyJarPath = servicePath
            + "selenium-server-standalone-3.141.59.jar";
        int port = findFreePort();
        String[] command = {"java", getDriverParameter(browserName, driverPath),
            "-jar", proxyJarPath, "-log", logPath,
            "-port", String.valueOf(port)};

        for (String s : command) {
            System.out.print(s + " ");
        }
        System.out.println("");
        startService("SeleniumStandAloneServer", command, "Clients should connect to", logPath);

        System.out.println("---- Started SeleniumStandAloneServer Service ----");
        this.url = new URL("http://127.0.0.1:" + port + "/wd/hub");
        return this.url;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public URL getUrl() {
        return this.url;
    }

    public void stop() {
        this.serviceProcess.destroyProcess();
    }

    /**
     * Method is to return free port.
     *
     * @return port - free port
     */
    public static int findFreePort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            int port = socket.getLocalPort();
            try {
                socket.close();
            } catch (IOException e) {
                // Ignore IOException on close()
            }
            return port;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new IllegalStateException("Could not find a free TCP/IP "
            + "port to start embedded Jetty HTTP Server on");
    }

    private void startService(String processName,
                              String[] procesCommand, String logMessage, String logPath)
        throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        this.serviceProcess = new SeleniumHubProcess(countDownLatch, processName, procesCommand);
        new Thread(serviceProcess).start();
        countDownLatch.await();
    }

    private String getDriverParameter(String browserName, String driverPath) {
        if (browserName.equalsIgnoreCase("chrome")) {
            return "-Dwebdriver.chrome.driver=" + driverPath + File.separator + "chromedriver";
        } else if (browserName.equalsIgnoreCase("firefox")) {
            return "-Dwebdriver.gecko.driver=" + driverPath;
        } else if (browserName.equalsIgnoreCase("internet explorer")) {
            return "-Dwebdriver.ie.driver=" + driverPath;
        } else if (browserName.equalsIgnoreCase("MicrosoftEdge")) {
            return "-Dwebdriver.ege.driver=" + driverPath;
        }
        return "";
    }
}