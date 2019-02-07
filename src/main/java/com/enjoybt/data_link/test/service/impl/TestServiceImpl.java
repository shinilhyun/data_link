package com.enjoybt.data_link.test.service.impl;

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
    public String sqlTest() throws Exception {
        return (String)commonDAO.selectOne("test.test");
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
