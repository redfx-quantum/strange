module org.redfx.strange {
    requires java.logging;
    requires jdk.httpserver;

    exports org.redfx.strange;
    exports org.redfx.strange.algorithm;
    exports org.redfx.strange.gate;
    exports org.redfx.strange.local;
    exports org.redfx.strange.render;
}
