/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.nxyi.addon.Utils;

import java.io.IOException;

public class SendToWebhook {

    public static void payload(String WEBHOOK, String content){
        DiscordWebhook webhook = new DiscordWebhook(WEBHOOK);

        webhook.setContent(content);
        try {
            webhook.execute();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
