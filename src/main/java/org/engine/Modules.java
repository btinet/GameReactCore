package org.engine;

import org.gamereact.module.AudioPlayerModuleBuilder;

public class Modules {


    public static Module createModule(String moduleName) {
        try {
            switch (moduleName) {
                case "AUDIO_PLAYER_MODULE":
                    return new AudioPlayerModuleBuilder().createAudioPlayerModule();
            }
        } catch (Exception ignored) {

        }

        return null;
    }

}
