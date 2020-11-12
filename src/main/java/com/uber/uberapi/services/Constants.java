package com.uber.uberapi.services;

import com.uber.uberapi.repositories.DBConstantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Constants {

    final DBConstantsRepository dbConstantsRepository;

    private final Map<String,String> constants = new HashMap<>();

    private static final Integer TEN_MINUTES = 60 * 10 * 1000;
    public Constants(DBConstantsRepository dbConstantsRepository) {
        this.dbConstantsRepository = dbConstantsRepository;
        loadConstantsFromDB();
    }
    @Scheduled(fixedRate = TEN_MINUTES)
    public void loadConstantsFromDB(){
        dbConstantsRepository.findAll().forEach(dbConstant -> {
            constants.put(dbConstant.getName(),dbConstant.getValue());
        });
    }

    public Integer getRideStartOTPExpiryMin(){
        return Integer.parseInt(constants.getOrDefault("rideStartOTPExpiryMinutes","3600000"));
    }

    public String getSchedulingTopicName() {
        return constants.getOrDefault("schedulingTopicName","schedulingTopic");
    }
//
//    public void setSchedulingTopicName(String schedulingTopicName) {
//        this. = schedulingTopicName;
//    }

    public String getDriverMatchingTopicName() {
        return constants.getOrDefault("driverMatchingTopicName","driverMatchingTopic");
    }

    public int getMaxWaitTimeForPreviousRide() {
        return Integer.parseInt(constants.getOrDefault("maxWaitTimeForPreviousRide","900000"));
    }

    public Integer getBookingProcessBeforeTime() {
        return Integer.parseInt(constants.getOrDefault("bookingProcessBeforeTime","900000"));
    }

    public double getMaxDistanceKmForDriverMatching() {
        return Double.parseDouble(constants.getOrDefault("maxDistanceKmForDriverMatching", "2"));
    }

    public int getMaxDriverETAMinutes() {
        return Integer.parseInt(constants.getOrDefault("maxDriverETAMinutes", "15"));
    }

    public boolean getETAbasedFilterIsOn() {
        return Boolean.parseBoolean(constants.getOrDefault("eTAbasedFilterIsOn","false"));
    }

    public boolean getGenderBasedFilterIsOn() {
        return Boolean.parseBoolean(constants.getOrDefault("genderBasedFilterIsOn","false"));
    }

    public double getDefaultDriverSpeedInKMPH() {
        return Double.parseDouble(constants.getOrDefault("defaultDriverSpeedInKMPH","60"));
    }
}
