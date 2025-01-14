package com.craftmend.openaudiomc.api.impl.event.events;

import com.craftmend.openaudiomc.api.impl.event.AudioEvent;
import com.craftmend.openaudiomc.generic.networking.client.objects.player.ClientConnection;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This event gets called whenever a player unmutes their microphone _or_ whenever a player
 * activates their microphone for the first time. (so completing the setup and loading voicechat
 * after connecting to the webclient will also trigger this event)
 */
@Getter
@AllArgsConstructor
public class MicrophoneUnmuteEvent extends AudioEvent {

    private ClientConnection client;

}
