package com.oceane.dm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.oceane.dm.TestsShowroomTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceane.dm.model.Dummy;

@SpringBootTest
class DummyServiceTest {

    @Autowired
    private DummyService dummyService;

    @Test
    void assertThatCreatedDummyHasId() {
        final Dummy dummy = new Dummy();
        dummy.setName(TestsShowroomTestHelper.DUMMY_NAME);
        final Dummy savedDummy = dummyService.createDummy(dummy);
        assertNotNull(savedDummy);
        final Long savedDummyId = savedDummy.getId();
        assertNotNull(savedDummyId);
        assertTrue(savedDummyId > 0);
    }

    @Test
    void assertThatCreatedDummyNameIsTranslated() {
        final Dummy dummy = new Dummy();
        dummy.setName(TestsShowroomTestHelper.DUMMY_NAME);
        final Dummy savedDummy = dummyService.createDummy(dummy);
        assertNotNull(savedDummy);
        assertEquals(TestsShowroomTestHelper.DUMMY_PIRATE_NAME, savedDummy.getName());
    }
}
