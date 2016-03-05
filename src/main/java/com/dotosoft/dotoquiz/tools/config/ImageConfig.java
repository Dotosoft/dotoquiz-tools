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

package com.dotosoft.dotoquiz.tools.config;

import static java.lang.String.format;

public class ImageConfig {
	private String toDir;
	private String fileSuffix;
	private boolean removeGamma;
	private int compressionLevel;
	private String compressor;
	private String logLevel;
	private String resize;

	public String getToDir() {
		return toDir;
	}

	public void setToDir(String toDir) {
		this.toDir = toDir;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public boolean isRemoveGamma() {
		return removeGamma;
	}

	public void setRemoveGamma(boolean removeGamma) {
		this.removeGamma = removeGamma;
	}

	public int getCompressionLevel() {
		return compressionLevel;
	}

	public void setCompressionLevel(int compressionLevel) {
		this.compressionLevel = compressionLevel;
	}

	public String getCompressor() {
		return compressor;
	}

	public void setCompressor(String compressor) {
		this.compressor = compressor;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getResize() {
		return resize;
	}

	public void setResize(String resize) {
		this.resize = resize;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("Image Configurations:\n")
			.append(format("Output Directory: %s\n", toDir))
			.append(format("File Suffix: %s\n", toDir))
			.append(format("Remove Gamma: %s\n", toDir))
			.append(format("Compression Level: %s\n", toDir))
			.append(format("Compressor: %s\n", toDir))
			.append(format("LogLevel: %s\n", toDir))
			.append(format("Resize: %s\n", toDir))
			.toString();
	}
}
