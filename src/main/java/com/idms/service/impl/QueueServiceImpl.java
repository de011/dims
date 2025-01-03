package com.idms.service.impl;

import com.idms.dto.QueueDTO;
import com.idms.service.QueueService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueServiceImpl implements QueueService {
    @Override
    public List<QueueDTO> getQueueList() {
        return List.of(new QueueDTO(1, "Queue A"), new QueueDTO(2, "Queue B"));
    }
}
