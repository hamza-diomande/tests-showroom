/*package com.oceane.dm.auth.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.oceane.dm.auth.AuthFeaturesTestHelper.DUMMY_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class DummyServiceWithMockedRepositoryTest {

    @MockBean
    private DummyRepository dummyRepository;

    @Autowired
    private DummyService dummyService;

    @Test
    void assertThatCreatedDummyIsSaved() {
        final Dummy dummy = new Dummy();
        dummy.setName(DUMMY_NAME);
        dummyService.createDummy(dummy);
        verify(dummyRepository, times(1)).save(any());
    }
}
*/