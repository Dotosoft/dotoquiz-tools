package com.dotosoft.dotoquiz.command.image.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.util.ServiceException;

public interface ImageWebClient {
	List<GphotoEntry> getAlbums( boolean showall ) throws IOException, ServiceException;
	List<GphotoEntry> getPhotos(GphotoEntry photoEntry) throws IOException, ServiceException;
	GphotoEntry uploadImageToAlbum(File imageFile, GphotoEntry remotePhoto, GphotoEntry albumEntry, String localMd5CheckSum ) throws IOException, ServiceException, IllegalAccessException, InvocationTargetException;
	AlbumEntry insertAlbum(AlbumEntry album) throws IOException, ServiceException; 
}