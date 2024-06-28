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

    private static List<RelationEntityResourceHolderVO> relationEntityProcessingDataList = new ArrayList<>();

    public static List<ConceptionEntityResourceHolderVO> getConceptionEntityProcessingDataList(){
        return conceptionEntityProcessingDataList;
    }

    public static List<RelationEntityResourceHolderVO> getRelationEntityProcessingDataList(){
        return relationEntityProcessingDataList;
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

    public static boolean addRelationEntityToProcessingList(RelationEntityResourceHolderVO relationEntityResourceHolderVO){
        String relationEntityUID = relationEntityResourceHolderVO.getRelationEntityUID();
        boolean alreadyInList = false;
        for(RelationEntityResourceHolderVO currentRelationEntityResourceHolderVO:relationEntityProcessingDataList){
            if(relationEntityUID.equals(currentRelationEntityResourceHolderVO.getRelationEntityUID())){
                alreadyInList = true;
                break;
            }
        }
        if(!alreadyInList){
            relationEntityProcessingDataList.add(relationEntityResourceHolderVO);
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

    public static boolean removeRelationEntityFromProcessingList(String relationEntityUID){
        boolean removeSuccessFlag = false;
        for(RelationEntityResourceHolderVO currentRelationEntityResourceHolderVO:relationEntityProcessingDataList){
            if(relationEntityUID.equals(currentRelationEntityResourceHolderVO.getRelationEntityUID())){
                relationEntityProcessingDataList.remove(currentRelationEntityResourceHolderVO);
                removeSuccessFlag = true;
                break;
            }
        }
        return removeSuccessFlag;
    }

    public static void clearConceptionEntityProcessingList(){
        conceptionEntityProcessingDataList.clear();
    }

    public static void clearRelationEntityProcessingList(){
        relationEntityProcessingDataList.clear();
    }
}
