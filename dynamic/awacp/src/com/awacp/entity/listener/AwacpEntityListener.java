package com.awacp.entity.listener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.awacp.entity.SystemLog;
import com.awacp.util.Action;
import com.sts.core.entity.BaseEntity;
import com.sts.core.entity.User;

public class AwacpEntityListener {

	private static Connection CONN = null;
	private static final String URL = "jdbc:mysql://localhost:3306/softech_awacp";
	private static final String USER_NAME = "softech_awacp";
	private static final String PASSWORD = "awacp2016";
	private static final String DRIVER = "com.mysql.jdbc.Driver";

	static {
		try {

			Class.forName(DRIVER).newInstance();
			CONN = DriverManager.getConnection(URL, USER_NAME, PASSWORD);

		} catch (SQLException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PrePersist
	public void prePersist(BaseEntity entity) throws Exception {
		perform(entity, Action.INSERTED);
	}

	@PreUpdate
	public void preUpdate(BaseEntity entity) throws Exception {
		perform(entity, Action.UPDATED);
	}

	public void perform(BaseEntity entity, Action action) throws Exception {
		System.err.println("DEBUG::PRE/POST LISTEN::BEGIN");
		try {
			Long userId = action.equals(Action.INSERTED) ? entity.getCreatedById() : entity.getUpdatedById();
			if (userId != null) {
				String userQuery = "SELECT ID, USERCODE FROM USER WHERE ID = ?";
				if (!CONN.isValid(5)) {
					CONN = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
				}
				PreparedStatement us = CONN.prepareStatement(userQuery);
				us.setLong(1, userId);
				ResultSet rs = us.executeQuery();
				String code = "";
				if (rs != null) {
					rs.next();
					code = rs.getString(2);
					rs.close();
				}
				SystemLog sl = new SystemLog();
				sl.setSection(entity.getClass().getSimpleName());
				sl.setDescription("" + action.name() + ": ");
				String sql = "INSERT INTO SYSTEMLOG(SECTION, DESCRIPTION, DATECREATED, CREATEDBYID, UC, VERSION) VALUES(?, ?, ?, ?, ?, ?)";
				PreparedStatement ps = CONN.prepareStatement(sql);
				ps.setString(1, entity.getClass().getSimpleName());
				ps.setString(2, entity.getAuditMessage() == null ? action.name()
						: action.name() + " : " + entity.getAuditMessage());
				ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()), Calendar.getInstance());
				ps.setLong(4, userId);
				ps.setString(5, code);
				ps.setLong(6, 1L);
				ps.execute();
			}
		} catch (Exception e) {
			System.err.println("ERROR AT::PRE/POST LISTEN");
			throw e;
		}

		System.err.println("DEBUG::PRE/POST LISTEN::END");
	}

	public static void main(String args[]) throws Exception {
		BaseEntity b = new User();
		b.setCreatedById(1L);
		b.setUpdatedById(1L);
		new AwacpEntityListener().perform(b, Action.INSERTED);
	}

}
