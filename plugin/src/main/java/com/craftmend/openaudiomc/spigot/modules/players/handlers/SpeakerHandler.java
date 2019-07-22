package com.craftmend.openaudiomc.spigot.modules.players.handlers;

import com.craftmend.openaudiomc.OpenAudioMcCore;
import com.craftmend.openaudiomc.spigot.OpenAudioMcSpigot;
import com.craftmend.openaudiomc.generic.media.objects.MediaUpdate;
import com.craftmend.openaudiomc.spigot.modules.players.interfaces.ITickableHandler;
import com.craftmend.openaudiomc.spigot.modules.players.objects.SpigotConnection;
import com.craftmend.openaudiomc.spigot.modules.speakers.objects.ApplicableSpeaker;
import com.craftmend.openaudiomc.generic.networking.packets.PacketClientCreateMedia;
import com.craftmend.openaudiomc.generic.networking.packets.PacketClientDestroyMedia;
import com.craftmend.openaudiomc.generic.networking.packets.PacketClientUpdateMedia;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SpeakerHandler implements ITickableHandler {

    private Player player;
    private SpigotConnection spigotConnection;

    /**
     * update speakers based on the players location
     */
    @Override
    public void tick() {
        List<ApplicableSpeaker> applicableSpeakers = new ArrayList<>(OpenAudioMcSpigot.getInstance().getSpeakerModule().getApplicableSpeakers(player.getLocation()));

        List<ApplicableSpeaker> enteredSpeakers = new ArrayList<>(applicableSpeakers);
        enteredSpeakers.removeIf(speaker -> containsSpeaker(spigotConnection.getSpeakers(), speaker));

        List<ApplicableSpeaker> leftSpeakers = new ArrayList<>(spigotConnection.getSpeakers());
        leftSpeakers.removeIf(speaker -> containsSpeaker(applicableSpeakers, speaker));

        enteredSpeakers.forEach(entered -> {
            if (!isPlayingSpeaker(entered)) {
                OpenAudioMcCore.getInstance().getNetworkingService().send(spigotConnection.getClientConnection(), new PacketClientCreateMedia(entered.getSpeaker().getMedia(), entered.getDistance(), entered.getSpeaker().getRadius()));
            }
        });

        spigotConnection.getSpeakers().forEach(current -> {
            if (containsSpeaker(applicableSpeakers, current)) {
                ApplicableSpeaker selector = filterSpeaker(applicableSpeakers, current);
                if (selector != null && (current.getDistance() != selector.getDistance())) {
                    MediaUpdate mediaUpdate = new MediaUpdate(selector.getDistance(), selector.getSpeaker().getRadius(), 450, current.getSpeaker().getMedia().getMediaId());
                    OpenAudioMcCore.getInstance().getNetworkingService().send(spigotConnection.getClientConnection(), new PacketClientUpdateMedia(mediaUpdate));
                }
            }
        });

        leftSpeakers.forEach(left -> OpenAudioMcCore.getInstance().getNetworkingService().send(spigotConnection.getClientConnection(), new PacketClientDestroyMedia(left.getSpeaker().getMedia().getMediaId())));

        spigotConnection.setCurrentSpeakers(applicableSpeakers);
    }

    private Boolean isPlayingSpeaker(ApplicableSpeaker speaker) {
        for (ApplicableSpeaker currentSpeaker : spigotConnection.getSpeakers())
            if (currentSpeaker.getSpeaker().getSource().equals(speaker.getSpeaker().getSource())) return true;
        return false;
    }

    private ApplicableSpeaker filterSpeaker(List<ApplicableSpeaker> list, ApplicableSpeaker query) {
        for (ApplicableSpeaker applicableSpeaker : list) {
            if (applicableSpeaker.getSpeaker() == query.getSpeaker()) return applicableSpeaker;
        }
        return null;
    }

    private Boolean containsSpeaker(List<ApplicableSpeaker> list, ApplicableSpeaker speaker) {
        for (ApplicableSpeaker currentSpeaker : list)
            if (currentSpeaker.getSpeaker().getSource().equals(speaker.getSpeaker().getSource())) return true;
        return false;
    }

}
