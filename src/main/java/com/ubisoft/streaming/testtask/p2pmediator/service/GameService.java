package com.ubisoft.streaming.testtask.p2pmediator.service;

import com.ubisoft.streaming.testtask.p2pmediator.dto.VideoGame;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    public boolean exists(final VideoGame videoGame) {
        return true;
    }
}
