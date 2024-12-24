package com.monora.personalbothub.bot_db.repository;

import com.monora.personalbothub.bot_db.entity.TGUserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TGUserProfileRepository extends JpaRepository<TGUserProfileEntity, Long> {

}
