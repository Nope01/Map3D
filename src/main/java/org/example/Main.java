package org.example;

import org.example.database.DbAccess;

public class Main {
    Engine engine;

    public static void main(String[] args) {
        DbAccess.getNode();

        Engine engine = new Engine();
        engine.startEngine();
    }
}

