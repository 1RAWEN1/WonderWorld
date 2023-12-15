package com.mygdx.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public interface MyInterface {

    FileOutputStream getOStream() throws FileNotFoundException;

    FileInputStream getIStream() throws FileNotFoundException;
}
