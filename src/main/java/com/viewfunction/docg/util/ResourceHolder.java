package com.viewfunction.docg.util;

import com.github.wolfie.blackboard.Blackboard;

import java.util.ArrayList;
import java.util.List;

public class ResourceHolder {

    private static Blackboard applicationBlackboard;

    public static Blackboard getApplicationBlackboard() {
        return applicationBlackboard;
    }

    public static void setApplicationBlackboard(Blackboard applicationBlackboard) {
        ResourceHolder.applicationBlackboard = applicationBlackboard;
    }

    private static List<ConceptionEntityResourceHolderVO> conceptionEntityProcessingDataList = new ArrayList<>();

    public static List<ConceptionEntityResourceHolderVO> getConceptionEntityProcessingDataList(){
        return conceptionEntityProcessingDataList;
    }

    public static boolean addConceptionEntityToProcessingList(ConceptionEntityResourceHolderVO conceptionEntityResourceHolderVO){
        String conceptionEntityUID = conceptionEntityResourceHolderVO.getConceptionEntityUID();
        boolean alreadyInList = false;
        for(ConceptionEntityResourceHolderVO currentConceptionEntityResourceHolderVO:conceptionEntityProcessingDataList){
            if(conceptionEntityUID.equals(currentConceptionEntityResourceHolderVO.getConceptionEntityUID())){
                alreadyInList = true;
                break;
            }
        }
        if(!alreadyInList){
            conceptionEntityProcessingDataList.add(conceptionEntityResourceHolderVO);
        }
        return true;
    }

    public static boolean removeConceptionEntityFromProcessingList(String conceptionEntityUID){
        boolean removeSuccessFlag = false;
        for(ConceptionEntityResourceHolderVO currentConceptionEntityResourceHolderVO:conceptionEntityProcessingDataList){
            if(conceptionEntityUID.equals(currentConceptionEntityResourceHolderVO.getConceptionEntityUID())){
                conceptionEntityProcessingDataList.remove(currentConceptionEntityResourceHolderVO);

                removeSuccessFlag = true;
                break;
            }
        }
        return removeSuccessFlag;
    }

    public static void clearConceptionEntityProcessingList(){
        conceptionEntityProcessingDataList.clear();
    }
}
