package com.craftmend.openaudiomc.spigot.modules.punishments;

import com.craftmend.openaudiomc.api.impl.event.events.ClientRequestVoiceEvent;
import com.craftmend.openaudiomc.api.interfaces.AudioApi;
import com.craftmend.openaudiomc.generic.logging.OpenAudioLogger;
import com.craftmend.openaudiomc.spigot.services.dependency.DependencyHandler;
import litebans.api.Database;
import org.bukkit.plugin.Plugin;

public class LitebansIntegration implements DependencyHandler {

    @Override
    public void onLoad(String pluginName, Plugin plugin) {
        // enable voicechat blocking for muted players
        AudioApi.getInstance().getEventDriver()
                .on(ClientRequestVoiceEvent.class)
                .setHandler(event -> {

                    boolean isMuted = Database.get().isPlayerMuted(event.getRequester().getOwnerUUID(), null);

                    if (isMuted) {
                        OpenAudioLogger.toConsole("Blocking voicechat for " + event.getRequester().getPlayer().getName() + " because they are muted on LiteBans");
                        event.setCanceled(true);
                    }
                });

    }

}
