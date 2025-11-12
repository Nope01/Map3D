package org.example.database;

public class Tag {
    public String key;
    public String tag;

    public Tag(String key, String tag) {
        this.key = key;
        this.tag = tag;
    }

    public String toString() {
        return (key + " = " + tag);
    }
}
