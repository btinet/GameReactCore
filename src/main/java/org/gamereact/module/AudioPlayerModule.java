package org.gamereact.module;


import com.tuio.TuioObject;
import org.engine.TangibleObject;
import org.gamereact.component.Track;
import java.util.ArrayList;


public class AudioPlayerModule extends MultimediaModule {

    public AudioPlayerModule(TangibleObject tangibleObject, String title, String file, ArrayList<Track> tracks) {
        super(tangibleObject, title, file, tracks);
    }

    @Override
    public void onTuioObjectRemoved(TuioObject tobj) {
        getMediaPlayer().dispose();
    }
}
