package org.engine;

import org.gamereact.module.Module;

public interface Command {

    Module create(TangibleObject tangibleObject);
}
