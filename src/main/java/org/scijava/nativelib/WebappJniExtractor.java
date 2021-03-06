//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.scijava.nativelib;

import java.io.File;
import java.io.IOException;

public class WebappJniExtractor extends BaseJniExtractor {
    private File nativeDir = new File(System.getProperty("java.library.tmpdir", "tmplib"));
    private File jniSubDir;

    public WebappJniExtractor(String classloaderName) throws IOException {
        this.nativeDir.mkdirs();
        if (!this.nativeDir.isDirectory()) {
            throw new IOException("Unable to create native library working directory " + this.nativeDir);
        } else {
            long now = System.currentTimeMillis();
            int attempt = 0;

            while(true) {
                File trialJniSubDir = new File(this.nativeDir, classloaderName + "." + now + "." + attempt);
                if (trialJniSubDir.mkdir()) {
                    this.jniSubDir = trialJniSubDir;
                    this.jniSubDir.deleteOnExit();
                    return;
                }

                if (!trialJniSubDir.exists()) {
                    throw new IOException("Unable to create native library working directory " + trialJniSubDir);
                }

                ++attempt;
            }
        }
    }

    protected void finalize() throws Throwable {
        File[] files = this.jniSubDir.listFiles();

        for(int i = 0; i < files.length; ++i) {
            files[i].delete();
        }

        this.jniSubDir.delete();
    }

    public File getJniDir() {
        return this.jniSubDir;
    }

    public File getNativeDir() {
        return this.nativeDir;
    }
}
