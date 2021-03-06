package runquickly.timeout;

import com.alibaba.fastjson.JSON;
import runquickly.constant.Constant;
import runquickly.entrance.RunQuicklyTcpService;
import runquickly.mode.GameBase;
import runquickly.mode.GameStatus;
import runquickly.mode.Room;
import runquickly.mode.Seat;
import runquickly.redis.RedisService;

/**
 * Created by pengyi
 * Date : 17-8-31.
 * desc:
 */
public class ReadyTimeout extends Thread {

    private int roomNo;
    private RedisService redisService;
    private GameBase.BaseConnection.Builder response;
    private int gameCount;

    public ReadyTimeout(int roomNo, RedisService redisService, int gameCount) {
        this.roomNo = roomNo;
        this.redisService = redisService;
        this.response = GameBase.BaseConnection.newBuilder();
        this.gameCount = gameCount;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                wait(Constant.readyTimeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (redisService.exists("room" + roomNo)) {
            while (!redisService.lock("lock_room" + roomNo)) {
            }
            Room room = JSON.parseObject(redisService.getCache("room" + roomNo), Room.class);
            if ((0 == room.getGameStatus().compareTo(GameStatus.READYING) || 0 == room.getGameStatus().compareTo(GameStatus.WAITING))
                    && gameCount == room.getGameCount()) {
                boolean hasNoReady = false;
                for (Seat seat : room.getSeats()) {
                    if (!seat.isReady()) {
                        seat.setReady(true);
                        hasNoReady = true;
                        response.setOperationType(GameBase.OperationType.READY).setData(GameBase.ReadyResponse.newBuilder().setID(seat.getUserId()).build().toByteString());
                        room.getSeats().stream().filter(seat1 -> RunQuicklyTcpService.userClients.containsKey(seat1.getUserId())).forEach(seat1 ->
                                RunQuicklyTcpService.userClients.get(seat1.getUserId()).send(response.build(), seat1.getUserId()));
                    }
                }
                if (hasNoReady) {
                    room.start(response, redisService);
                }
            }
            redisService.addCache("room" + roomNo, JSON.toJSONString(room));
            redisService.unlock("lock_room" + roomNo);
        }
    }
}
