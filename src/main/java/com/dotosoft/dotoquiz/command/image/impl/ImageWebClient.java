package com.dotosoft.dotoquiz.command.image.impl;

import java.io.File;
import java.util.List;

public interface ImageWebClient {
	List getAlbums( boolean showall ) throws Exception;
	List getPhotos(Object photoEntry) throws Exception;
	Object uploadImageToAlbum(File imageFile, Object remotePhoto, Object albumEntry, String localMd5CheckSum ) throws Exception;
	Object insertAlbum(Object album) throws Exception;
	void deletePhoto(Object param) throws Exception;
	List filterPhoto( List photoCollection, String photo ) throws Exception;
}