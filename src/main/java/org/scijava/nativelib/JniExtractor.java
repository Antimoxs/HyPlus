//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.scijava.nativelib;

import java.io.File;
import java.io.IOException;

public interface JniExtractor {
    File extractJni(String var1, String var2) throws IOException;

    void extractRegistered() throws IOException;
}
