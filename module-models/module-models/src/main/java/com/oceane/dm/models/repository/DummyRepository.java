package com.oceane.dm.models.repository;

import com.oceane.dm.models.model.Dummy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyRepository extends JpaRepository<Dummy,Long> {
    Dummy findByName(String name);
}
