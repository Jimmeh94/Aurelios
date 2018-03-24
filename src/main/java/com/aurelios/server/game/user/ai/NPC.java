package com.aurelios.server.game.user.ai;

import com.aurelios.server.game.environment.nodes.Node;
import com.aurelios.server.game.user.User;
import org.spongepowered.api.entity.living.Living;

public class NPC {

    private AIMetaData metaData;
    private User user;
    private DailySchedule dailySchedule;

    public NPC(Node node) {
        metaData = new AIMetaData(node);
        dailySchedule = new DailySchedule(this);
        dailySchedule.generateSchedule();
    }

    public NPC(AIMetaData metaData){
        this.metaData = metaData;
        dailySchedule = new DailySchedule(this);
        dailySchedule.generateSchedule();
    }

    public void attachEntity(Living entity){
        if(user == null){
            user = new User(entity);
        } else {
            user.migrateEntity(entity);
        }
    }

    public AIMetaData getMetaData() {
        return metaData;
    }

    public void tick(){
        dailySchedule.tick();
    }

    public DailySchedule getDailySchedule() {
        return dailySchedule;
    }

    public User getUser() {
        return user;
    }
}
