package com.marcotte.blockhead.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ShellScript
{
    public static String executeCommand(String... command) {
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            // p.waitFor();
            InputStreamReader in = new InputStreamReader(p.getInputStream());
            BufferedReader reader = new BufferedReader(in);
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            p.destroy();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
