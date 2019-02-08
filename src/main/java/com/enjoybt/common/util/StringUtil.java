package com.enjoybt.common.util;

import com.enjoybt.data_link.link.controller.LinkRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StringUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(LinkRestController.class);
	
	public static String exportErrorMessage(Exception e) {
		String output = null;
		ByteArrayOutputStream out = null;
		PrintStream ps = null;
		try{
			out = new ByteArrayOutputStream();
			ps = new PrintStream(out);
			e.printStackTrace(ps);
			output = out.toString();
		}catch(Exception e1) {
			LOGGER.error("Export Error Message", e1);
		}
		return output;
	}
}
