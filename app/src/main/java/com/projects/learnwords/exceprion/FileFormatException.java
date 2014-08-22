package com.projects.learnwords.exceprion;

import java.io.IOException;

/**
 * Created by Александр on 22.08.2014.
 */
public class FileFormatException extends IOException {
    public FileFormatException() {}
    public FileFormatException(String gripe){
        super(gripe);
    }
}
