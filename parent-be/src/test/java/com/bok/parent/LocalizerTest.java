package com.bok.parent;

import com.bok.parent.be.geolocalization.GeographicalLocalizer;
import com.bok.parent.geolocalization.model.GeographicalInfo;
import com.bok.parent.geolocalization.repository.GeographicalInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles("test")
public class LocalizerTest {

    @Autowired
    GeographicalLocalizer geographicalLocalizer;

    @Autowired
    GeographicalInfoRepository geographicalInfoRepository;

    @Autowired
    ModelTestUtil modelTestUtil;

    @BeforeEach
    public void clear() {
        modelTestUtil.clearAll();
    }

    @Test
    public void localizeIp() {
        String ip = "home.faraone.ovh";
        GeographicalInfo geoInfo = geographicalLocalizer.localize(ip);
        assertNotNull(geoInfo);
        log.info("country: {}, city:{}, postalCode: {}, state: {}", geoInfo.getCountry(), geoInfo.getCity(), geoInfo.getPostalCode(), geoInfo.getState());
        assertEquals(1, geographicalInfoRepository.findAll().size());
    }

}
