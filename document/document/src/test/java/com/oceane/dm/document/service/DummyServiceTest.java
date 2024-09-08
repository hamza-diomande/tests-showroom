package com.oceane.dm.document.service;

import com.oceane.dm.document.model.Dummy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.oceane.dm.document.DocumentTestHelper.DUMMY_NAME;
import static com.oceane.dm.document.DocumentTestHelper.DUMMY_PIRATE_NAME;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DummyServiceTest {

    @Autowired
    private DummyService dummyService;

    @Test
    void assertThatCreatedDummyHasId() {
        final Dummy dummy = new Dummy();
        dummy.setName(DUMMY_NAME);
        final Dummy savedDummy = dummyService.createDummy(dummy);
        assertNotNull(savedDummy);
        final Long savedDummyId = savedDummy.getId();
        assertNotNull(savedDummyId);
        assertTrue(savedDummyId > 0);
    }

    @Test
    void assertThatCreatedDummyNameIsTranslated() {
        final Dummy dummy = new Dummy();
        dummy.setName(DUMMY_NAME);
        final Dummy savedDummy = dummyService.createDummy(dummy);
        assertNotNull(savedDummy);
        assertEquals(DUMMY_PIRATE_NAME, savedDummy.getName());
    }
}
