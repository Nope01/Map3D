package org.example;

import org.example.database.DbAccess;
import org.example.database.Node;
import org.example.database.Way;

public class Main {
    Engine engine;

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.startEngine();
    }
}

