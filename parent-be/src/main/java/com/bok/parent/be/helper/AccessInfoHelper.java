package com.bok.parent.be.helper;

import com.bok.parent.model.AccessInfo;
import com.bok.parent.repository.AccessInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessInfoHelper {


    final AccessInfoRepository accessInfoRepository;

    public AccessInfoHelper(@Autowired AccessInfoRepository accessInfoRepository) {
        this.accessInfoRepository = accessInfoRepository;
    }

    public AccessInfo findLastAccessInfoByAccountId(Long accountId) {
        return accessInfoRepository.findLastAccessInfoByAccountId(accountId).orElse(null);
    }
}
