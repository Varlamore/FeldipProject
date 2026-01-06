package com.cryptic;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Launch implements AppletStub {

    /**
     * The host address
     */
    private final String host = "127.0.0.1";

    /**
     * The parameters of the client.
     */
    private final Map<String, String> map = new HashMap<>();

    /**
     * The main entry point of the current application.
     *
     * @param args
     *            The command line arguments.
     * @throws IOException
     * @throws MalformedURLException
     */
    public static void main(final String[] args) throws Exception {
        final Launch applet = new Launch();
        applet.initialize();

        final Class<?> client = Class.forName("com.cryptic.Client");
        final Applet instance = (Applet) client.getConstructor().newInstance();

        final JFrame frame = new JFrame("OSRS");
        frame.add(instance);
        frame.setVisible(true);
        frame.setSize(781, 541);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        client.getSuperclass().getMethod("setStub", AppletStub.class).invoke(instance, applet);
        client.getMethod("init").invoke(instance);
        client.getMethod("start").invoke(instance);
    }

    /**
     * Reads the parameters text file, and stores the parameters.
     *
     * @throws IOException
     * @throws MalformedURLException
     */
    private void initialize() throws MalformedURLException, IOException {
        map.put("11", "0");
        map.put("12", "");
        map.put("13", "1");
        map.put("14", "0");
        map.put("15", "50759");
        //map.put("initial_jar", "gamepack_2537725.jar");
        map.put("1", ".runescape.com");
        map.put("2", "http://www.runescape.com/g=oldscape/slr.ws?order=LPWM");
        map.put("3", "5");
        map.put("4", "false");
        map.put("5", "0");
        map.put("6", "0");
        //map.put("codebase", "http://oldschool5.runescape.com/");
        map.put("7", "305");
        map.put("8", "true");
        map.put("9", "ElZAIrq5NpKN6D3mDdihco3oPeYN2KFy2DCquj7JMmECPmLrDP3Bnw");
        map.put("10", "true");
    }

    /*
     * (non-Javadoc)
     *
     * @see java.applet.AppletStub#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(final String paramName) {
        return map.get(paramName);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.applet.AppletStub#getDocumentBase()
     */
    @Override
    public URL getDocumentBase() {
        try {
            return new URL("http://" + host);
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.applet.AppletStub#getCodeBase()
     */
    @Override
    public URL getCodeBase() {
        try {
            return new URL("http://" + host);
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.applet.AppletStub#isActive()
     */
    @Override
    public boolean isActive() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.applet.AppletStub#getAppletContext()
     */
    @Override
    public AppletContext getAppletContext() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.applet.AppletStub#appletResize(int, int)
     */
    @Override
    public void appletResize(final int width, final int height) {
    }

}