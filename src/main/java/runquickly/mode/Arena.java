package runquickly.mode;

/**
 * Created by pengyi
 * Date : 17-8-28.
 * desc:
 */
public class Arena {

    private String id;

    private Integer arenaType;            //竞技类型
    private String name;                    //竞技名
    private Integer count;                  //人数
    private Integer entryFee;               //报名费
    private Integer reward;                 //奖励

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getArenaType() {
        return arenaType;
    }

    public void setArenaType(Integer arenaType) {
        this.arenaType = arenaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(Integer entryFee) {
        this.entryFee = entryFee;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }
}
