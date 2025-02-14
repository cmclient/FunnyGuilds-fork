package net.dzikoysk.funnyguilds.nms.v1_20R2.playerlist;

import net.dzikoysk.funnyguilds.nms.api.playerlist.PlayerList;
import net.dzikoysk.funnyguilds.nms.api.playerlist.PlayerListAccessor;

public class V1_20R2PlayerListAccessor implements PlayerListAccessor {

    @Override
    public PlayerList createPlayerList(int cellCount) {
        return new V1_20R2PlayerList(cellCount);
    }

}
