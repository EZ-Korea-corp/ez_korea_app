package com.ezkorea.hybrid_app.service.adjustment;

import com.ezkorea.hybrid_app.domain.adjustment.AdjustmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdjustmentService {

    private final AdjustmentRepository adjustmentRepository;

}
