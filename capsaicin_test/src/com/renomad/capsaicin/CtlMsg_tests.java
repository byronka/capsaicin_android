package com.renomad.capsaicin.tests;

import com.renomad.capsaicin.CtlMsg;
import junit.framework.*;
import com.renomad.capsaicin.CtlAct;
import java.util.UUID;

public class CtlMsg_tests extends TestCase {

    public void test_canCreateCtlMsg_wantvid() {
	int sid = 1234;
	CtlAct action = CtlAct.CLIENT_WANTS_VID;
	UUID vid = new UUID(0,5);
	UUID uid = new UUID(0,1);
	long offset = 0;
	short sbytes = 1024;
	new CtlMsg(sid, action, vid, uid, offset, sbytes);
    }

    public void test_canCreateCtlMsg_sendvid() {
	Assert.fail("Not yet implemented");
    }

    public void test_canCreateCtlMsg_OK() {
	Assert.fail("Not yet implemented");
    }

    public void test_should_not_be_possible_for_race_condition() {
	Assert.fail("Not yet implemented");
    }

    public void test_output_is_in_correct_order() {
	Assert.fail("Not yet implemented");
    }
}
