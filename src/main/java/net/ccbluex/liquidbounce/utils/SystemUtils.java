package net.ccbluex.liquidbounce.utils;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemUtils {
    public static void notify(String title, String message, TrayIcon.MessageType messageType) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon");
        TrayIcon trayIcon = new TrayIcon(image, "Destiny");

        trayIcon.setToolTip("Destiny");
        tray.add(trayIcon);

        trayIcon.displayMessage(title, message, messageType);
    }

    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("hh:mm");

        return "(" + dateFormat.format(date) + ")";
    }
}
