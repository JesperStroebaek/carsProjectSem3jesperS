package carsProjectSem3.cars.repository;

import carsProjectSem3.cars.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,String> {
  boolean existsByEmail(String email);
}
