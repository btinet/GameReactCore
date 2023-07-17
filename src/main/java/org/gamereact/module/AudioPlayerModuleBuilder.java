package org.gamereact.module;

import org.engine.Track;

import java.util.ArrayList;

public class AudioPlayerModuleBuilder {
    private String title;
    private String file;
    private ArrayList<Track> tracks = new ArrayList<>();

    public AudioPlayerModuleBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public AudioPlayerModuleBuilder setFile(String file) {
        this.file = file;
        return this;
    }

    public AudioPlayerModuleBuilder setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
        return this;
    }

    public AudioPlayerModuleBuilder addTrack(Track track) {
        this.tracks.add(track);
        return this;
    }

    public AudioPlayerModule createAudioPlayerModule() {
        return new AudioPlayerModule(title, file, tracks);
    }
}