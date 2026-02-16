package com.flp.ddd.dddlibrary.lending.application;

import com.flp.ddd.dddlibrary.lending.domain.CopyDTO;
import com.flp.ddd.dddlibrary.lending.domain.CopyId;
import org.springframework.stereotype.Service;

@Service
public interface CopySearchServiceInterface {
    CopyDTO findCopy(CopyId copyId);
}
