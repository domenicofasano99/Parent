package com.bok.parent.geolocalization.repository;

import com.bok.parent.geolocalization.model.GeographicalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeographicalInfoRepository extends JpaRepository<GeographicalInfo, Long> {
}
