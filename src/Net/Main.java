package Net;

import DBcontrol.Database;
import elements.Habitat;

public class Main {

    //public static String ipAddr = "localhost";
    //public static int port = 8080;
    //public static Client client;

    public static void main(String[] args) throws InterruptedException {

        Habitat hab = new Habitat();
        hab.requestFocus();
        hab.setVisible(true);

        //client = new Client(ipAddr, port);


    }
}