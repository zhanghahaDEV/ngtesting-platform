package com.ngtesting.platform.service.impl;

import com.ngtesting.platform.dao.IssuePageElementDao;
import com.ngtesting.platform.model.IsuField;
import com.ngtesting.platform.model.IsuPageElement;
import com.ngtesting.platform.service.IssueFieldService;
import com.ngtesting.platform.service.IssuePageElementService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IssuePageElementServiceImpl extends BaseServiceImpl implements IssuePageElementService {
    Log logger = LogFactory.getLog(IsuJqlColumnServiceImpl.class);

	@Autowired
    IssuePageElementDao elementDao;

    @Autowired
    IssueFieldService fieldService;

    @Override
    @Transactional
    public void saveAll(Integer orgId, Integer pageId, Integer tabId, List<Map> maps) {

        int ordr = 10;
        for (Map map: maps) {
            map.put("ordr", ordr++);

            String id = map.get("id").toString();
            String key = map.get("key").toString();

            if (id == null) {
                IsuField field = fieldService.getField(key);

                IsuPageElement elm = new IsuPageElement(field.getCode(), field.getLabel(),
                        field.getType(), field.getInput(), field.getFullLine(), field.getRequired(),
                        field.getKey(), field.getFieldId(), tabId, pageId, orgId);

                elementDao.save(elm);
            }
        }

        long start = new Date().getTime();
        elementDao.saveOrdrs(maps, orgId);
        long end = new Date().getTime();

        logger.info("Update ordrs for " + maps.size() + " records spend " + (end - start) + " milliseconds");
    }

    @Override
    public void updateProp(String id, String prop, String val, Integer orgId) {
        if ("required".equals(prop) || "fullLine".equals(prop)) {

        } else {
            val = "'" + val + "'";
        }
        elementDao.updateProp(id, prop, val, orgId);
    }

//    @Override
//    public void add(IsuPageElement element) {
//        Integer maxOrder = elementDao.getMaxFieldOrdr(element.getTabId());
//        maxOrder = maxOrder == null? 0: maxOrder;
//        element.setOrdr(maxOrder + 1);
//        elementDao.add(element);
//    }
//
//    @Override
//    public boolean remove(Integer id, Integer orgId) {
//        Integer count = elementDao.remove(id, orgId);
//        if (count == 0) {
//            return false;
//        }
//
//        return true;
//    }

}
