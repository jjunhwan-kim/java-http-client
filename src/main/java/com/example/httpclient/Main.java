package com.example.httpclient;


import com.example.httpclient.http.java.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        //Client client = new Client();
        //client.request();


        Client client = new Client();
        client.request();
    }
}
