package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class GroupDao extends AbstractDao {

    public void createGroup(Group group) throws SQLException, ClassNotFoundException {
        String sql = "insert into GROUPS values(?,?,?,DEFAULT,?)";
        queryNotForReturn(sql, group.getGroupId(), group.getGroupName(),
                group.getGroupCaptainUserId(), ""+group.isPublic());
    }

    public void deleteGroup(Group group) throws SQLException, ClassNotFoundException {
        String sql = "delete from GROUPS where groupId=?";
        queryNotForReturn(sql, group.getGroupId());
    }

    public void createGroupUser(String groupCaptainUserId, String groupId) throws SQLException, ClassNotFoundException {
        String sql = "insert into GROUPS_USERS values(?,?)";
        queryNotForReturn(sql, groupCaptainUserId, groupId);
    }

    public boolean checkExistGroupId(String groupId) throws ClassNotFoundException, SQLException {
    	boolean result = false;
    	String sql = "select groupId from GROUPS where groupId=?";
        String jsonResult = queryForReturn(sql, groupId);
        List<Group> list = gson.fromJson(jsonResult, GroupList);
        if(list.size() != 0)
            result = true;
        return result;
    }

    public Group findByGroupId(String groupId) throws SQLException, ClassNotFoundException {
        String sql = "select * from GROUPS where groupId = ?";
        String jsonResult = queryForReturn(sql, groupId);
        return gson.fromJson(jsonResult, Group.class);
    }

    public List<Group> readGroupList(String userId) throws ClassNotFoundException, SQLException  {
        String sql = "select * from GROUPS as G, (select groupId from GROUPS_USERS as A, USERS as B where A.userId = B.userId and B.userId = ?) as C where G.groupId = C.groupId";
        String jsonResult = queryForReturn(sql, userId);
        return gson.fromJson(jsonResult, GroupList);
    }
    
    public static  class comGroupName implements Comparator<Group>{
        @Override
        public int compare(Group o1, Group o2) {
        	return o1.getGroupName().compareTo(o2.getGroupName());
        }
    }

}
