package org.akala.server.user.service.impl;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.akala.server.user.bean.AddressInfo;
import org.akala.server.user.bean.AkalaUser;
import org.akala.server.user.repository.AddressInfoRepository;
import org.akala.server.user.repository.AkalaUserRepository;
import org.akala.server.user.service.AkalaAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service("akalaAddressService")
public class AkalaAddressServiceImpl implements AkalaAddressService {

  @Resource(name = "akalaUserRepository")
  private AkalaUserRepository akalaUserRepository;

  @Resource(name = "addressInfoRepository")
  private AddressInfoRepository addressInfoRepository;

  @Override
  public void saveAddress(String userKey, String userType, AddressInfo addressInfo) {
    AkalaUser user = akalaUserRepository.findOneByLoginsKeyAndLoginsType(userKey, userType);
    if (user.getAddressInfos() == null) {
      user.setAddressInfos(new ArrayList<AddressInfo>());
    }
    if (addressInfo.getId() == null) {
      addressInfoRepository.save(addressInfo);
      user.getAddressInfos().add(addressInfo);
    } else {
      for (AddressInfo tempAddr : user.getAddressInfos()) {
        if (tempAddr.getId().equals(addressInfo.getId())) {
          BeanUtils.copyProperties(addressInfo, tempAddr);
        }
      }
    }
    akalaUserRepository.save(user);
  }
}
