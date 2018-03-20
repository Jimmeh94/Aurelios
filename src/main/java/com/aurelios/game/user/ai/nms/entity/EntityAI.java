package com.aurelios.game.user.ai.nms.entity;

import com.aurelios.game.user.ai.NPC;

public interface EntityAI {

    void init(NPC owner);
    void clearTasks();

}
