package com.oceane.dm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.oceane.dm.TestsShowroomTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceane.dm.model.Dummy;
import com.oceane.dm.repository.DummyRepository;

@SpringBootTest
class DummyServiceWithMockedRepositoryTest {

    @MockBean
    private DummyRepository dummyRepository;

    @Autowired
    private DummyService dummyService;

    @Test
    void assertThatCreatedDummyIsSaved() {
        final Dummy dummy = new Dummy();
        dummy.setName(TestsShowroomTestHelper.DUMMY_NAME);
        dummyService.createDummy(dummy);
        verify(dummyRepository, times(1)).save(any());
    }
}
