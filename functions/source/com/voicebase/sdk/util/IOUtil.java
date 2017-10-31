package com.voicebase.sdk.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {
	
	public static File writeToTempFile(InputStream in, String filePrefix) throws IOException {
		return writeToTempFile(in, filePrefix, null);
	}

	public static File writeToTempFile(InputStream in, String filePrefix, String suffix) throws IOException {

		File file = File.createTempFile(filePrefix, suffix);

		try (FileOutputStream f = new FileOutputStream(file)) {
			byte[] buffer = new byte[4096];

			int bytesRead = 0;
			while ((bytesRead = in.read(buffer)) != -1) {
				f.write(buffer, 0, bytesRead);
			}
		}

		return file;
	}
}
