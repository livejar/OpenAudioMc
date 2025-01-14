package com.craftmend.openaudiomc.bungee.modules.node;

import com.craftmend.openaudiomc.bungee.OpenAudioMcBungee;
import com.craftmend.openaudiomc.bungee.modules.node.listeners.NodePacketListener;
import com.craftmend.openaudiomc.velocity.messages.PacketManager;
import com.craftmend.openaudiomc.velocity.messages.implementations.BungeeCordPacketManager;
import lombok.Getter;

public class NodeManager {

    @Getter private PacketManager packetManager;

    public NodeManager(OpenAudioMcBungee openAudioMcBungee) {
        packetManager = new BungeeCordPacketManager(openAudioMcBungee, "openaudiomc:node");
        packetManager.registerListener(new NodePacketListener());
    }

}
