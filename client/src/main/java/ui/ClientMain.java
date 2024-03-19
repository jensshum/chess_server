package ui;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        int serverNum = 8080;


        new Repl(serverNum).run();

    }
}

