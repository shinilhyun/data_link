package com.enjoybt.data_link.link.service.impl;

import com.enjoybt.data_link.link.service.ManageService;
import org.springframework.stereotype.Service;

@Service
public class ManageServiceImpl implements ManageService {

	@Override
	public Double setStringToDoubleData(String val) throws NumberFormatException {
		val = val.replace(",", "");
		val = val.replace("\"", "");
		
		return (Double)Double.parseDouble(val);
	}
}
