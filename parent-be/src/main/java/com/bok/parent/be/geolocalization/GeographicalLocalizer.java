package com.bok.parent.be.geolocalization;

import com.bok.parent.geolocalization.model.GeographicalInfo;
import com.bok.parent.geolocalization.repository.GeographicalInfoRepository;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class GeographicalLocalizer {

    @Autowired
    GeographicalInfoRepository geographicalInfoRepository;
    @Value("${geolocalization.databasePath}")
    private String databasePath;
    private DatabaseReader database;

    @PostConstruct
    public void initialize() {
        log.info("GeoIP2 database path:{}", databasePath);
        try {
            this.database = new DatabaseReader.Builder(new ClassPathResource(databasePath).getInputStream()).build();
            log.info("GeoIP2 Database initialized");
        } catch (IOException ioe) {
            this.database = null;
            log.warn("Problems encountered while initializing IP localization database, skipping resolutions");
        }

    }

    public GeographicalInfo localize(String ipAddressString) {
        GeographicalInfo geoInfo = null;
        if (isNull(database)) {
            log.warn("Attempted to resolve an IP location with database problems, skipping.");
            return null;
        }
        try {
            InetAddress ipAddress = InetAddress.getByName(ipAddressString);
            Optional<CityResponse> response = ofNullable(database.city(ipAddress));
            if (!response.isPresent()) {
                return null;
            }
            String countryName = response.get().getCountry().getName();
            String cityName = response.get().getCity().getName();
            String postal = response.get().getPostal().getCode();
            String state = response.get().getLeastSpecificSubdivision().getName();
            Double latitude = response.get().getLocation().getLatitude();
            Double longitude = response.get().getLocation().getLongitude();
            Integer accuracyRadius = response.get().getLocation().getAccuracyRadius();

            geoInfo = new GeographicalInfo(null, cityName, countryName, postal, state, latitude, longitude, accuracyRadius);
            geoInfo = geographicalInfoRepository.save(geoInfo);
        } catch (UnknownHostException uhe) {
            log.error("Unknown host {}, {}", ipAddressString, uhe.getCause());

        } catch (IOException ioe) {
            log.error("IO error while opening database, {}", ioe.getCause().toString());

        } catch (GeoIp2Exception gip2e) {
            log.error("GeoIP error, {}", gip2e.getCause().toString());

        }
        return geoInfo;

    }

}
