package com.ezkorea.hybrid_app.service.stat;

import com.ezkorea.hybrid_app.domain.myBatis.SaleMbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class StatService {

    private final SaleMbRepository saleMbRepository;

    public List<Map<String, Object>> findStatList(Map<String, String> paramMap) {
        return saleMbRepository.findStatList(paramMap);
    }
}
