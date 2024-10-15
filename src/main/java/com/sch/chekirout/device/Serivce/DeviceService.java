package com.sch.chekirout.device.Serivce;

import com.sch.chekirout.device.Repository.UserDeviceRepository;
import com.sch.chekirout.device.domain.UserDevice;
import com.sch.chekirout.device.exception.DeviceNotFoundException;
import com.sch.chekirout.device.exception.DeviceNotMatchException;
import com.sch.chekirout.device.util.UserAgentUtil;
import com.sch.chekirout.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
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



    public void validateDevice(User user, HttpServletRequest request) throws DeviceNotFoundException, DeviceNotMatchException {
        String userAgent = request.getHeader("User-Agent");
        String currentDeviceInfo = UserAgentUtil.extractDeviceInfo(userAgent);

        if(userAgent != null && userAgent.contains("PostmanRuntime")){
            currentDeviceInfo = "Postman Device";
        }

        // 기존 디바이스 정보 조회
        UserDevice existingDevice = userDeviceRepository.findByUser(user)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found for user: " + user.getUsername()));

        // 디바이스 정보 비교
        if (!existingDevice.getDeviceInfo().equals(currentDeviceInfo)) {
            throw new DeviceNotMatchException();
        }
    }
}