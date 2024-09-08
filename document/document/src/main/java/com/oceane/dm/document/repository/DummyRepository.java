package com.oceane.dm.document.repository;

import com.oceane.dm.document.model.Dummy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyRepository extends JpaRepository<Dummy, Long> {
}
