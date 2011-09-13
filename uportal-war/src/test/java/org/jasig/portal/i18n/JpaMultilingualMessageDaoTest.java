package org.jasig.portal.i18n;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jasig.portal.i18n.dao.IMessageDao;
import org.jasig.portal.portlet.dao.jpa.BaseJpaDaoTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:jpaMultilingualMessageDaoTestContext.xml")
public class JpaMultilingualMessageDaoTest extends BaseJpaDaoTest {
    
    private IMessageDao messageDao;
    
    @Autowired
    public void setMessageDao(IMessageDao messageDao) {
        this.messageDao = messageDao;
    }
    
    @Test
    public void testAllMethods() {
        execute(new Callable<Object>() {
            
            @Override
            public Object call() throws Exception {
                final String code = "Test message";
                
                // test #createMessage
                final Message msgUS = messageDao.createMessage(code, "en_US", "Test message");
                final Message msgUS2 = messageDao.createMessage("Test message 2", "en_US", "Test message2");
                final Message msgLV = messageDao.createMessage(code, "lv_LV", "Testa ziņojums");
                final Message msgDE = messageDao.createMessage(code, "de_DE", "Testnachricht");
                
                // test #getMessage
                final Message actual1 = messageDao.getMessage(code, "lv_LV");
                assertEquals(msgLV, actual1);
                
                // test #updateMessage
                msgLV.setValue("Labots testa ziņojums");
                messageDao.updateMessage(msgLV);
                
                final Message actual2 = messageDao.getMessage(code, "lv_LV");
                assertEquals(msgLV, actual2);
                
                // test #getMessagesByCode
                final ArrayList<Message> expected3 = new ArrayList<Message>();
                expected3.add(msgUS);
                expected3.add(msgLV);
                expected3.add(msgDE);
                final List<Message> actual3 = messageDao.getMessagesByCode(code);
                assertEquals(expected3, actual3);
                
                // test #deleteMessage
                messageDao.deleteMessage(msgDE);
                final List<Message> actual4 = messageDao.getMessagesByCode(code);
                assertEquals(2, actual4.size());
                
                // test #getMessagesByLocale
                final ArrayList<Message> expected5 = new ArrayList<Message>();
                expected5.add(msgUS);
                expected5.add(msgUS2);
                final List<Message> actual5 = messageDao.getMessagesByLocale("en_US");
                assertEquals(expected5, actual5);
                
                return null;
            }
        });
    }
}
