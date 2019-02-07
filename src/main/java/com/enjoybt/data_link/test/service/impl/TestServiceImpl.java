package com.enjoybt.data_link.test.service.impl;

import com.enjoybt.common.Constants;
import com.enjoybt.common.database.dao.CommonBatchDAO;
import com.enjoybt.common.database.dao.CommonDAO;
import com.enjoybt.data_link.test.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl.class);

    @Autowired
    CommonDAO commonDAO;

    @Autowired
    CommonBatchDAO commonBatchDAO;

    @Override
    public Map<String,Object> sqlTest(){

        String data = null;
        Map<String, Object> result = new HashMap<>();

        try {
            data = (String)commonDAO.selectOne("test.test");
        } catch (Exception e) {
            LOGGER.error("sqlTest ERROR");
            result.put(Constants.KEY_RESULT, Constants.VALUE_RESULT_FAILURE);
            return result;
        }

        result.put(Constants.KEY_RESULT, Constants.VALUE_RESULT_SUCCESS);
        result.put(Constants.KEY_DATA, data);

        return result;
    }

    @Override
    public void batchTest() throws Exception {
        List<Object> batchParam = new ArrayList<>();
        Map<String, Object> m = new HashMap<>();
        m.put("test", "1");

        Map<String, Object> m2 = new HashMap<>();
        m2.put("test", "2");

        batchParam.add(m);
        batchParam.add(m2);

        commonBatchDAO.insertBatch("batchTest", batchParam);
    }
}
