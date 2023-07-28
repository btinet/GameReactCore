package org.gamereact.module;

import org.engine.TangibleObject;
import org.gamereact.component.Track;

import java.util.ArrayList;

public class AudioPlayerModuleBuilder {

    private final TangibleObject tangibleObject;
    private String title;
    private String file;
    private ArrayList<Track> tracks = new ArrayList<>();

    public AudioPlayerModuleBuilder(TangibleObject tangibleObject) {
        this.tangibleObject = tangibleObject;
    }

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
        return new AudioPlayerModule(tangibleObject, title, file, tracks);
    }
}