package com.imgu.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class MyImage {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key id;

    @Persistent
    private String name;

    @Persistent
    Blob image;

    public MyImage() { }
    public MyImage(String name, Blob image) {
        this.name = name; 
        this.image = image;
    }

    // JPA getters and setters and empty contructor
    // ...
    public Blob getImage()              { return image; }
    public void setImage(Blob image)    { this.image = image; }
	public Key getId() {
		return id;
	}
	public void setId(Key id) {
		this.id = id;
	}
    
}

