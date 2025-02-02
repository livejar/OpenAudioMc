package com.craftmend.openaudiomc.api.impl.event.events;

import com.craftmend.openaudiomc.api.impl.event.AudioEvent;
import com.craftmend.openaudiomc.api.impl.event.events.enums.VoiceEventCause;
import com.craftmend.openaudiomc.generic.networking.client.objects.player.ClientConnection;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This event gets called whenever a player enters the proximity of another player and links up
 * with their voice chat, providing the speaker and listener separately. This means that this
 * event could fire twice, once where player A is the speaker, and once where player A is the listener.
 *
 * Entering another players proximity doesn't have to be because they moved closer to another,
 * It could also be the result or shouting or other context changes (the cause fields specifies
 * what caused a specific event instance, so you can just ignore the ones that you dont want)
 */
@Getter
@AllArgsConstructor
public class PlayerEnterVoiceProximityEvent extends AudioEvent {

    private ClientConnection speaker;
    private ClientConnection listener;
    private VoiceEventCause cause;

}
