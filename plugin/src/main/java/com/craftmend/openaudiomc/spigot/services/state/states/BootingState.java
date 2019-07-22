package com.craftmend.openaudiomc.spigot.services.state.states;

import com.craftmend.openaudiomc.spigot.services.state.interfaces.State;

public class BootingState implements State {

    @Override
    public String getDescription() {
        return "OpenAudioMc is still starting up";
    }

    @Override
    public Boolean isConnected() {
        return false;
    }

    @Override
    public Boolean canConnect() {
        return false;
    }
}