package com.nor.sdpplugin.other;

import com.nor.sdpplugin.service.ServiceDeskPlus;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDeskPlus.class);

    public static String displayFileSize(long size) {
        return FileUtils.byteCountToDisplaySize(size);
    }

    public static String readFile(String filePath) {
        Scanner in;
        try {
            in = new Scanner(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            logger.error("TOKEN FILE NOT FOUND!");
            return "TOKEN FILE NOT FOUND!";
        }
        StringBuilder sb = new StringBuilder();
        while (in.hasNext()) {
            sb.append(in.next());
        }
        in.close();
        return sb.toString();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public static String extractAllNumbers(String input) {
        StringBuilder allNumbers = new StringBuilder();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            allNumbers.append(matcher.group());
        }
        return !allNumbers.isEmpty() ? allNumbers.toString() : input;
    }


    public static void main(String[] args) {
        var s = extractAllNumbers("نرم افزار- فرهنگی و امور خانواده - ");
        System.out.println(s);
//        listIPAddresses();
//        listNetworkInterfaces();
//        getHostName();
//        getMACAddress();
//        getHostIPAddress();
//        getLocalIPAddress();
//        listSystemProperties();
    }

    public static void listSystemProperties() {
        // Get system properties
        System.out.println("System Properties:");
        System.getProperties().forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });

        // Get environment variables
        System.out.println("\nEnvironment Variables:");
        System.getenv().forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
    }


    public static void getHostIPAddress() {
        try {
            // Replace "www.example.com" with the host you want to look up
            String host = "www.example.com";
            InetAddress hostAddress = InetAddress.getByName(host);
            System.out.println("IP Address of " + host + ": " + hostAddress.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void getLocalIPAddress() {
        try {
            // Get the local host IP address
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("Local IP Address: " + localHost.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void getMACAddress() {
        try {
            // Get the local host IP address
            InetAddress localHost = InetAddress.getLocalHost();

            // Get the Network Interface associated with the local host IP
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);

            if (networkInterface != null) {
                // Get the MAC address from the network interface
                byte[] macAddressBytes = networkInterface.getHardwareAddress();

                if (macAddressBytes != null) {
                    // Convert the MAC address bytes to a readable format
                    StringBuilder macAddress = new StringBuilder();
                    for (int i = 0; i < macAddressBytes.length; i++) {
                        macAddress.append(String.format("%02X%s", macAddressBytes[i], (i < macAddressBytes.length - 1) ? "-" : ""));
                    }
                    System.out.println("MAC Address: " + macAddress.toString());
                } else {
                    System.out.println("MAC address could not be retrieved.");
                }
            } else {
                System.out.println("Network Interface for the specified IP could not be found.");
            }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }


    public static void getHostName() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("Hostname: " + localHost.getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void listNetworkInterfaces() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                System.out.println("Interface Name: " + networkInterface.getName());
                System.out.println("Display Name: " + networkInterface.getDisplayName());
                System.out.println("Is Up: " + networkInterface.isUp());
                System.out.println("Supports Multicast: " + networkInterface.supportsMulticast());
                System.out.println("Is Loopback: " + networkInterface.isLoopback());
                System.out.println("MAC Address: " + java.util.Arrays.toString(networkInterface.getHardwareAddress()));
                System.out.println("====================================");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public static void listIPAddresses() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    System.out.println("Interface: " + networkInterface.getName());
                    System.out.println("IP Address: " + address.getHostAddress());
                    System.out.println("====================================");
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


}

















