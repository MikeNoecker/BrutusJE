package org.example;

import org.apache.commons.cli.*;
//import java.io.PrintWriter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Main {

    // Arg option definitions
    private static final Option ARG_url = new Option("u", "url", true, "Add numbers together");
    private static final Option ARG_wordlist = new Option("w", "wordlist", true, "Path to wordlist");
    private static final Option ARG_verbose = new Option("v", "verbose", false, "Verbose mode");

    // Help menu
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        //pw.println("MathApp" + Math.class.getPackage().getImplementationVersion()); //junk?
        pw.println();
        formatter.printUsage(pw,100,"java -jar BrutusJE.jar [options]");
        formatter.printOptions(pw, 100, options, 3, 5);
        pw.println("\n   example: brutus -u https://google.com/ -w /wordlists/common.txt");
        pw.close();
    }

    // The meat
    public static void main(String[] args) throws IOException{ //figure out a way to make exception more localized to fileio for better security
        // Option obj
        CommandLineParser clp = new DefaultParser();

        Options options = new Options();
        options.addOption(ARG_url);
        options.addOption(ARG_wordlist);
        options.addOption(ARG_verbose);

        // Option vars
        //String url = "";
        //String[] wordlist = {};
        //ArrayList<String> wordlist = new ArrayList<String>();
        ArrayList<String> wordlist = new ArrayList<>();
        boolean verbose = false;

        try {
            CommandLine cl = clp.parse(options, args);

            if(cl.hasOption(ARG_verbose)) {
                verbose = true;
            }

            if(cl.hasOption(ARG_wordlist)) {
                String path = cl.getOptionValue("w");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

                // Reads specified wordlist into arraylist
                String tempLine;
                int i = 0; //test inside of loop (probably won't work)
                while((tempLine = bufferedReader.readLine()) != null) {
                    wordlist.add(tempLine);
                }
                bufferedReader.close(); //might break file reading
            }
            else {
                System.out.print("Required arg missing: -w, --wordlist");
                printHelp(options);
            }

            if(cl.hasOption(ARG_url)) {
                // TODO: 7/30/2022 add custom connection timeout
                // TODO: 7/30/2022 add disable redirects

                for(int i = 0; i < wordlist.size(); i++) {
                    // Set URL
                    URL url = new URL(cl.getOptionValue("u").concat(wordlist.get(i)));
                    // TODO: 7/30/2022 concat dir to url 

                    // Connection setup, connect to url
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");

                    // Get status code
                    int status = connection.getResponseCode();

                    // Output results (change for verbose)
                    if(status >= 200 && status < 300 &! verbose) {
                        System.out.println(url.toString() + ' ' + status);
                    } else if (verbose){
                        System.out.println(url.toString() + ' ' + status);
                    }

                    // cleanup
                    connection.disconnect();
                }
            }
            else {
                //options.getRequiredOptions(); //look into this later
                System.out.print("Required arg missing: -w, --wordlist");
                printHelp(options);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}