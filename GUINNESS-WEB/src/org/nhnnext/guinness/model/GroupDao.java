package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GroupDao extends AbstractDao {

    public void createGroup(Group group) throws SQLException, ClassNotFoundException {
        String sql = "insert into GROUPS values(?,?,?,DEFAULT,?)";
        queryNotForReturn(sql, group.getGroupId(), group.getGroupName(),
                group.getGroupCaptainUserId(), group.isPublic());
    }

    public void deleteGroup(Group group) throws SQLException, ClassNotFoundException {
        String sql = "delete from GROUPS where groupId=?";
        queryNotForReturn(sql, group.getGroupId());
    }

    public void createGroupUser(String groupCaptainUserId, String groupId) throws SQLException, ClassNotFoundException {
        String sql = "insert into GROUPS_USERS values(?,?)";
        queryNotForReturn(sql, groupCaptainUserId, groupId);
    }

    public boolean checkExistGroupId(String groupId) throws Exception {
        String sql = "select groupId from GROUPS where groupId=?";
    	List<Map<String, Object>> queryResult = queryForReturn(sql, groupId);
        boolean result = false;
        
        if(queryResult.size() != 0)
            result = true;
        
        return result;
    }

    public Group findByGroupId(String groupId) throws SQLException, ClassNotFoundException {
        String sql = "select * from GROUPS where groupId = ?";
    	List<Map<String, Object>> queryResult = queryForReturn(sql, groupId);
        
        Map<String, Object> map = queryResult.get(0);
        System.out.println(map.get("groupId").toString()+" "+map.get("groupName").toString());
        
        return new Group(map.get("groupId").toString()
                , map.get("groupName").toString()
                , map.get("groupCaptainUserId").toString()
                , Integer.parseInt(map.get("isPublic").toString()));
    }

    public ArrayList<Group> readGroupList(String userId) throws ClassNotFoundException, SQLException  {
        String sql = "select A.groupId from GROUPS_USERS as A inner join USERS as B on A.userId = B.userId where A.userId =?";
    	List<Map<String, Object>> queryResult = queryForReturn(sql, userId);
        ArrayList<Group> result = new ArrayList<Group>();
    	Map<String, Object> map;
    	
        for(int index=0; index < queryResult.size() ; index++) {
            map = queryResult.get(index);
            result.add(findByGroupId(map.get("groupId").toString()));
        }
        Collections.sort(result,new GroupDao.comGroupName());
        return result;
    }
    
    public static  class comGroupName implements Comparator<Group>{
        @Override
        public int compare(Group o1, Group o2) {
        	return o1.getGroupName().compareTo(o2.getGroupName());
        }
    }

}
