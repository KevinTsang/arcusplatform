/*
 * Copyright 2019 Arcus Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arcussmarthome.notification.provider;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.arcussmarthome.core.dao.MobileDeviceDAO;
import com.arcussmarthome.core.dao.PersonDAO;
import com.arcussmarthome.core.dao.PlaceDAO;
import com.arcussmarthome.messages.model.MobileDevice;
import com.arcussmarthome.messages.model.Person;
import com.arcussmarthome.messages.model.Place;
import com.arcussmarthome.notification.dispatch.DispatchUnsupportedByUserException;
import com.arcussmarthome.notification.message.NotificationBuilder;
import com.arcussmarthome.notification.message.NotificationMessageRenderer;
import com.arcussmarthome.notification.provider.apns.ApnsSender;
import com.arcussmarthome.notification.retry.RetryProcessor;
import com.arcussmarthome.platform.notification.Notification;
import com.arcussmarthome.platform.notification.NotificationMethod;
import com.arcussmarthome.platform.notification.audit.NotificationAuditor;

@RunWith(MockitoJUnitRunner.class)
public class ApnsProviderTest {
    @Mock
    private NotificationAuditor auditor;

    @Mock
    private NotificationMessageRenderer messageRenderer;

    @Mock
    private MobileDeviceDAO mobileDeviceDao;

    @Mock
    private PersonDAO personDao;
    
    

    @Mock
    private ApnsSender sender;

    @Mock
    private Notification notification;

    @Mock
    private Person person;
    
    @Mock private PlaceDAO placeDao;   
    @Mock private Place place;
    private UUID placeId = UUID.randomUUID();
    
    @Mock
    private RetryProcessor retryProcessor;

    @InjectMocks
    private ApnsProvider apnsProvider;

    private UUID personId = UUID.randomUUID();
    
    private String iosToken1 = "0000000000000000000000000000000000000000000000000000000000000000";
    private String iosToken2 = "9999999999999999999999999999999999999999999999999999999999999999";

    @Before
    public void setup() {
        ArrayList<MobileDevice> mobileDevices = new ArrayList<MobileDevice>();

        MobileDevice ios1 = new MobileDevice();
        ios1.setOsType("ios");
        ios1.setNotificationToken(iosToken1);

        MobileDevice ios2 = new MobileDevice();
        ios2.setOsType("ios");
        ios2.setNotificationToken(iosToken2);

        MobileDevice android1 = new MobileDevice();
        android1.setOsType("android");
        android1.setNotificationToken("android1-token");

        MobileDevice android2 = new MobileDevice();
        android2.setOsType("android");
        android2.setNotificationToken("android2-token");

        mobileDevices.add(ios1);
        mobileDevices.add(ios2);
        mobileDevices.add(android1);
        mobileDevices.add(android2);

        notification = new NotificationBuilder().withPersonId(personId).withPlaceId(placeId).build();
        Mockito.when(personDao.findById(personId)).thenReturn(person);
        Mockito.when(placeDao.findById(placeId)).thenReturn(place);
        Mockito.when(mobileDeviceDao.listForPerson(person)).thenReturn(mobileDevices);
        Mockito.when(messageRenderer.renderMessage(notification, NotificationMethod.APNS,person, null)).thenReturn("rendered-message");
    }

    @Test(expected = DispatchUnsupportedByUserException.class)
    public void shouldThrowTerminalDispatchWithNoPerson() throws Exception {
        Mockito.when(personDao.findById(personId)).thenReturn(null);
        apnsProvider.notifyCustomer(notification);
    }

    @Test(expected = DispatchUnsupportedByUserException.class)
    public void shouldThrowTerminalDispatchWithNullMobileDevices() throws Exception {
        Mockito.when(mobileDeviceDao.listForPerson(person)).thenReturn(null);
        apnsProvider.notifyCustomer(notification);
    }

    @Test(expected = DispatchUnsupportedByUserException.class)
    public void shouldThrowTerminalDispatchWithNoMobileDevices() throws Exception {
        Mockito.when(mobileDeviceDao.listForPerson(person)).thenReturn(new ArrayList<MobileDevice>());
        apnsProvider.notifyCustomer(notification);
    }

    @Test
    public void shouldSendNotificationToMultipleDevices() throws Exception {
        apnsProvider.notifyCustomer(notification);
        Mockito.verify(retryProcessor).split(notification, NotificationMethod.APNS, iosToken1);
        Mockito.verify(retryProcessor).split(notification, NotificationMethod.APNS, iosToken2);
    }
}

