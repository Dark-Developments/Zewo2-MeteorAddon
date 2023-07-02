package com.nxyi.addon.irc;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.nxyi.addon.Addon;
import com.nxyi.addon.modules.IRC;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class IRCReceiver extends Thread {
    static MinecraftClient mc = MinecraftClient.getInstance();

    Socket socket;
    Scanner in;
    PrintWriter out;
    Scanner inp;
    boolean loggedIN;
    String username;

    IRCSender sender;

    public IRCReceiver(Socket socket, Scanner in, PrintWriter out, Scanner inp, String username) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.inp = inp;
        this.loggedIN = false;
        this.username = username;
    }

    public void run() {
        while (true) {
            if (in.hasNextLine()) {
                final String line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {
                    out.println(username + ":" + Addon.CATEGORY.name);
                } else if (line.startsWith("NAMEACCEPTED")) {
                    System.out.println("Connected to IRC! Your Username is " + username);
                    Addon.getIrc().setConnected(true);
                    if (!loggedIN) {
                        sender = new IRCSender(socket, in, out, inp);
                        sender.start();
                    }
                } else if (line.startsWith("MESSAGE")) {
                    if (mc.player != null && Modules.get().isActive(IRC.class)) {
                        final String l = line.substring(8);
                        final String username = l.split(" ")[0].split(":")[0];
                        final String client = l.split(" ")[0].split(":")[1];
                        mc.player.sendMessage(Text.of("(§cIRC§f) " + "§8(§a§n" + username + "§f - [§8" + client + "§f]" + "§8)§f: " + l.substring(username.length() + client.length() + 1)));
                    }
                }
            }
        }
    }

    public IRCSender getSender() {
        return sender;
    }
}
