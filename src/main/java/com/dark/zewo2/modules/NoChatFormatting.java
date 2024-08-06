package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.Text;

public class NoChatFormatting extends Module {
    public NoChatFormatting() {
        super(Addon.CATEGORY, "NoChatFormatting", "Stops Chat Colour Codes");
    }

    @EventHandler
    private void onRevieve(ReceiveMessageEvent event){
        event.setMessage(Text.literal(event.getMessage().getString()));
    }
}
