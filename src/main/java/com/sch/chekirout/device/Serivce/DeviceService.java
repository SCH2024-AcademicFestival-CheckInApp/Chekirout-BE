package com.sch.chekirout.device.Serivce;

import com.sch.chekirout.device.Repository.UserDeviceRepository;
import com.sch.chekirout.device.domain.UserDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    private UserDeviceRepository userDeviceRepository;


    // 기존 UserDevice 조회
    public Optional<UserDevice> findDeviceByUserId(Long userId) {
        return userDeviceRepository.findByUserId(userId);
    }


    public void saveOrUpdateDevice(UserDevice device) {
        userDeviceRepository.save(device);
    }


}