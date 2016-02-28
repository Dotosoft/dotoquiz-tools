package com.dotosoft.dotoquiz.tools.config;

import static java.lang.String.format;

public class ImageConfig {
	public String toDir;
	public String fileSuffix;
	public boolean removeGamma;
	public int compressionLevel;
	public String compressor;
	public String logLevel;
	public String resize;

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
