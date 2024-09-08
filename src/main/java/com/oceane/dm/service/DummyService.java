package com.oceane.dm.service;

import com.oceane.dm.model.Dummy;
import com.oceane.dm.repository.DummyRepository;
import org.springframework.stereotype.Service;

@Service
public class DummyService {
    private final DummyRepository dummyRepository;

    public DummyService(DummyRepository dummyRepository) {
        this.dummyRepository = dummyRepository;
    }

    /**
     * Create a {@link Dummy} from a valid {@link Dummy}
     *
     * @param dummy the dummy creation request
     * @return the new dummy
     */
    public Dummy createDummy(Dummy dummy) {
        // convert dummy name in pirate english
        dummy.setName("Ahoy " + dummy.getName());
        return dummyRepository.save(dummy);
    }
}
