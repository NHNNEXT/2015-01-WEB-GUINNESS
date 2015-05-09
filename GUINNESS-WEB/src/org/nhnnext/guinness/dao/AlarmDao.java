package org.nhnnext.guinness.dao;

import java.util.List;
import java.util.Map;

import org.nhnnext.guinness.model.Alarm;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class AlarmDao extends JdbcDaoSupport {

	public void create(Alarm alarm) {
		String sql = "insert into ALARMS (alarmId, calleeId, callerId, noteId, alarmText, createDate) values(?, ?, ?, ?, ?, default)";
		getJdbcTemplate().update(sql, alarm.getAlarmId(), alarm.getCalleeId(),
				alarm.getCallerId(), alarm.getNoteId(), alarm.getAlarmText());
	}

	public Alarm read(String alarmId) {
		String sql = "select * from ALARMS where alarmId = ?";

		try {
			return getJdbcTemplate().queryForObject(
				sql,
				(rs, rowNum) -> new Alarm(rs.getString("alarmId"), rs
						.getString("calleeId"), rs.getString("callerId"), rs
						.getString("noteId"), rs.getString("alarmText"), rs.getString("alarmStatus"), rs
						.getString("createDate")), alarmId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Map<String, Object>> list(String calleeId) {
		String sql = "select A.*, U.userName, G.groupName from ALARMS as A, USERS as U, NOTES as N, GROUPS as G where A.calleeId=? and A.callerId=U.userId and A.noteId = N.noteId and N.groupId = G.groupId order by A.createDate desc;";
		return getJdbcTemplate().queryForList(sql, calleeId);
	}

	public void delete(String alarmId) {
		String sql = "delete from ALARMS where alarmId = ?";
		getJdbcTemplate().update(sql, alarmId);
	}

	public List<Map<String, Object>> readNoteAlarm(String sessionUserId) {
		String sql = "select groupId, count(*) as groupAlarmCount from ALARMS as A, NOTES as N where A.alarmStatus = 'N' and A.calleeId =? and N.noteId = A.noteId GROUP BY groupId order by groupId;";
		return getJdbcTemplate().queryForList(sql, sessionUserId);
	}

}
