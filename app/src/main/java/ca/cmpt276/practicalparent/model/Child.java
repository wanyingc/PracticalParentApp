package ca.cmpt276.practicalparent.model;

import androidx.annotation.Nullable;

/**
* Used to store attributes of a child
*/

public class Child {
    private String name; // stores the name of the child
    private String bitmap; // stores the bitmap encoding of an image
    // Constructor
    public Child(String name) {
        this.name = name;
    }
    public Child(String name, String bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }

    // Getters
    public String getName() {
        return name;
    }
    public String getBitmap() {
        return bitmap;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Child c = (Child) obj;
        return (this.getName().equals(c.getName()));
    }
}
