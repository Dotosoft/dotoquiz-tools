/*
	Copyright 2015 Denis Prasetio
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package com.dotosoft.dotoquiz.command.image.impl;

import java.util.List;

public interface ImageWebClient {
	List getAlbums( boolean showall ) throws Exception;
	List getPhotos(Object albumEntry) throws Exception;
	Object uploadImageToAlbum(String imageFilePath, Object remotePhoto, Object albumEntry ) throws Exception;
	Object insertAlbum(String title, String description, boolean isPublic) throws Exception;
	void deletePhoto(Object param) throws Exception;
}