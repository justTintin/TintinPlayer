package com.tintin.tintinplayer.module;

import java.io.Serializable;

/**
 * Created by alive on 2016/1/3.
 */
public class VideoModule implements Serializable
{

    private String path;

    private String size;

    private String duration;

    private String displayName;

    private String album;

    private String width;

    private String height;

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getAlbum()
    {
        return album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }

    public String getWidth()
    {
        return width;
    }

    public void setWidth(String width)
    {
        this.width = width;
    }

    public String getHeight()
    {
        return height;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    @Override
    public String toString()
    {
        return "VideoModule{" + "path='" + path + '\'' + ", size='" + size
                + '\'' + ", duration='" + duration + '\'' + ", displayName='"
                + displayName + '\'' + ", album='" + album + '\'' + ", width='"
                + width + '\'' + ", height='" + height + '\'' + '}';
    }
}
