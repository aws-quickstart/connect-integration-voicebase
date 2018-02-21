/**
 * Copyright 2016-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not
 * use this file except in compliance with the License. A copy of the License is
 * located at 
 * 
 *      http://aws.amazon.com/apache2.0/ 
 *      
 * or in the "license" file
 * accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.voicebase.sdk.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
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
