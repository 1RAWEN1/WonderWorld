package com.mygdx.game;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class InterfaceImplementation implements MyInterface {

    private Context context;

    public InterfaceImplementation(Context context) {
        // Store the context for later use
        this.context = context;
    }

    @Override
    public FileOutputStream getOStream() throws FileNotFoundException {
        return context.openFileOutput("save.ser", Context.MODE_PRIVATE);
    }

    @Override
    public FileInputStream getIStream() throws FileNotFoundException {
        return context.openFileInput("save.ser");
    }
}
