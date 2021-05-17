package com.wg_planner.backend.Controller;

import com.wg_planner.backend.Http.Response.ResponseEntity;
import com.wg_planner.backend.Repository.NotificationChannelFirebaseRepository;
import com.wg_planner.backend.Repository.ResidentDeviceRepository;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.entity.NotificationChannel;
import com.wg_planner.backend.entity.NotificationChannelFirebase;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.ResidentDevice;
import com.wg_planner.backend.utils.HelperMethods;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class ResidentDeviceController {
    @Autowired
    ResidentAccountService residentAccountService;
    @Autowired
    NotificationChannelFirebaseRepository notificationChannelFirebaseRepository;
    @Autowired
    ResidentDeviceRepository residentDeviceRepository;

    @RequestMapping(value = "/new_device/{resident_account_id}", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity receiveNewDevice(@PathVariable("resident_account_id") String residentAccountId,
                                    @RequestParam String registrationToken) {
        try {
            Validate.notNull(registrationToken, "parameter token must not be %s", (Object) null);
            Validate.notEmpty(registrationToken, "parameter registrationToken must not be empty");
            ResidentDevice residentDevice =
                    registerNewDeviceWithNotificationChannelFirebase(Long.parseLong(residentAccountId),
                            registrationToken);
            return new ResponseEntity(getResponseHeadersAsNewResidentDeviceId(residentDevice.getId()), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity("error: resident account id sent must not be null",
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity("error: registration token sent must not be empty",
                    HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException httpClientErrorException) {
            return new ResponseEntity("error: resident account not found in server",
                    httpClientErrorException.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private MultiValueMap<String, String> getResponseHeadersAsNewResidentDeviceId(long newResidentDeviceId) {
        MultiValueMap<String, String> responseMap= new LinkedMultiValueMap<>();
        responseMap.add("residentDeviceId", String.valueOf(newResidentDeviceId));
        return responseMap;
    }

    @RequestMapping(value = "/update_device/{resident_device_id}", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity updateDeviceFirebaseToken(@PathVariable("resident_device_id") String residentDeviceId,
                                             @RequestParam String updatedRegistrationToken) {
        try {
            Validate.notNull(updatedRegistrationToken, "parameter token must not be %s", (Object) null);
            Validate.notEmpty(updatedRegistrationToken, "parameter token must not be empty");
            ResidentDevice updatedResidentDevice =
                    updateNotificationChannelFirebase(Long.parseLong(residentDeviceId),
                            updatedRegistrationToken);
            return new ResponseEntity(updatedResidentDevice.getId(), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity("error: resident device id sent must not be null",
                    HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity("error: registration token sent must not be empty",
                    HttpStatus.NOT_FOUND);
        } catch (HttpClientErrorException httpClientErrorException) {
            return new ResponseEntity("error: resident device not found in server",
                    httpClientErrorException.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResidentDevice registerNewDeviceWithNotificationChannelFirebase(long residentAccountId,
                                                                           String registrationToken) throws RuntimeException {//throws expection because server needs to send an HttpResponse
        Validate.notNull(registrationToken, "parameter registration token must not be %s",
                (Object) null);
        Validate.notEmpty(registrationToken, "parameter registration  token must not be empty");

        ResidentAccount ownerResidentAccount =
                residentAccountService.getResidentAccount(residentAccountId);
        if (ownerResidentAccount == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        boolean hasNotificationChannelWithPassedToken =
                ownerResidentAccount.getResidentDevices().stream().filter(Objects::nonNull).anyMatch(residentDevice -> residentDevice.getDeviceNotificationChannels().stream().filter(Objects::nonNull).anyMatch(notificationChannel -> Objects.equals(notificationChannel.getNotificationToken(), registrationToken)));
        if (hasNotificationChannelWithPassedToken) {//return the corresponding resident device
            return ownerResidentAccount.getResidentDevices().stream().filter(residentDevice -> residentDevice.getDeviceNotificationChannels().stream().anyMatch(notificationChannel -> notificationChannel.getNotificationToken().equals(registrationToken))).findFirst().get();
        }
        ArrayList<ResidentDevice> residentDevices = new ArrayList<>();
        ResidentDevice ownerResidentDevice = new ResidentDevice(ownerResidentAccount);
        NotificationChannelFirebase notificationChannelFirebase =
                new NotificationChannelFirebase(ownerResidentDevice, registrationToken);
        ownerResidentDevice.addToDeviceNotificationChannels(notificationChannelFirebase);
        residentDevices.add(ownerResidentDevice);
        ownerResidentAccount.setResidentDevices(residentDevices);
        residentDeviceRepository.save(ownerResidentDevice);
        notificationChannelFirebaseRepository.save(notificationChannelFirebase);
        residentAccountService.save(ownerResidentAccount);
        return ownerResidentDevice;
    }

    public ResidentDevice updateNotificationChannelFirebase(long residentDeviceId,
                                                            String updatedRegistrationToken) throws RuntimeException {//throws expection because server needs to send an HttpResponse
        Validate.notNull(updatedRegistrationToken, "parameter updateRegistrationToken must not be " +
                        "%s",
                (Object) null);
        Validate.notEmpty(updatedRegistrationToken, "parameter updateRegistrationToken must not be" +
                " empty");
        Optional<ResidentDevice> residentDevice =
                residentDeviceRepository.findById(residentDeviceId);
        if (residentDevice.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        List<NotificationChannel> notificationChannels = residentDevice.get().getDeviceNotificationChannels();
        NotificationChannelFirebase notificationChannelFirebase =
                selectFirebaseNotificationChannel(notificationChannels);
        if (notificationChannelFirebase == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        notificationChannelFirebase.setDeviceRegistrationToken(updatedRegistrationToken);
        notificationChannelFirebaseRepository.save(notificationChannelFirebase);
        return residentDevice.get();
    }

    private NotificationChannelFirebase selectFirebaseNotificationChannel(List<NotificationChannel> notificationChannels) {
        return (NotificationChannelFirebase) notificationChannels.stream().filter(notificationChannel -> notificationChannel.getClass().equals(NotificationChannelFirebase.class)).collect(HelperMethods.toSingleton());
    }
}