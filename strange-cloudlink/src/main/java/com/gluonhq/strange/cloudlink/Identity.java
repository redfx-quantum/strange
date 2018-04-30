package com.gluonhq.strange.cloudlink;

import java.util.Collections;

public class Identity extends Gate {

    public Identity(int idx) {
        super("I", "SingleQubit", Collections.emptyList());

        setAffectedQubitIndex(Collections.singletonList(idx));
    }
}
