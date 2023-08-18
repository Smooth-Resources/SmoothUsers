package net.smoothplugins.smoothusers.loader;

import com.google.inject.Inject;

public class MainLoader {

    @Inject
    private ListenerLoader listenerLoader;

    public void load() {
        listenerLoader.load();
    }

    public void unload() {

    }
}
