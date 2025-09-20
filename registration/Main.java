package registration;

import registration.controller.AppController;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new java.io.File("data").mkdirs();

            AppController controller = new AppController();
            controller.start();
        });
    }
}