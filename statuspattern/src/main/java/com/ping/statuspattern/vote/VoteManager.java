package com.ping.statuspattern.vote;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Id VoteManager.java, v 1.0 2019-08-01 15:44 zsp $$
 * @author: zhangsp
 */

public class VoteManager {
    /**
     * 持有状态处理对象
     */
    private VoteState state = null;
    /**
     * 记录用户投票的结果，Map<String,String>对应Map<用户名称，投票的选项>
     */
    private Map<String, String> mapVote = new HashMap<>();

    /**
     * 记录用户投票次数，Map<String,Integer>对应Map<用户名称，投票的次数>
     */
    private Map<String, Integer> mapVoteCount = new HashMap<>();

    /**
     * 获取用户投票结果的Map
     */
    public Map<String, String> getMapVote() {
        return mapVote;
    }

    /**
     * 投票
     * @param user    投票人
     * @param voteItem    投票的选项
     */
    public void vote(String user,String voteItem){
        Integer oldVoteCount = mapVoteCount.get(user);
        if (oldVoteCount == null) {
            oldVoteCount = 0;
        }
        oldVoteCount += 1;
        mapVoteCount.put(user, oldVoteCount);

        //2.判断该用户的投票类型，就相当于判断对应的状态
        //到底是正常投票、重复投票、恶意投票还是上黑名单的状态
        if(oldVoteCount == 1){
            state = new NormalVoteState();
        }
        else if(oldVoteCount > 1 && oldVoteCount < 5){
            state = new RepeatVoteState();
        }
        else if(oldVoteCount >= 5 && oldVoteCount <8){
            state = new SpiteVoteState();
        }
        else if(oldVoteCount > 8){
            state = new BlackVoteState();
        }
        //然后转调状态对象来进行相应的操作
        state.vote(user, voteItem, this);
    }

}
