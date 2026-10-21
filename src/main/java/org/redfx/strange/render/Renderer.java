package org.redfx.strange.render;

import org.redfx.strange.Program;
import org.redfx.strange.Result;

public interface Renderer extends AutoCloseable {

    void render(Program program, Result result);

    default void render(Program program) {
        render(program, null);
    }

    @Override
    default void close() {}
}
