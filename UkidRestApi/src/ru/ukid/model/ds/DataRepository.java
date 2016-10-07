package ru.ukid.model.ds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DataRepository {

	private static final Logger _logger = Logger.getLogger(DataRepository.class
			.getName());

	@Inject
	private ResourceVVV _resource;

	public boolean pingDataSource() throws Exception {

		Connection conn = _resource.getConnection();
		try (Statement st = conn.createStatement()) {
			String sql = "SELECT 1 FROM DUAL";
			logSQL(sql);
			st.execute(sql);
			return true;
		} catch (Exception e) {
			_logger.severe("Ping DS error: " + e.getMessage());
			throw e;
		}
	}

	public boolean authenticateUser(String user, String password) throws Exception {
		boolean result = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = _resource.getConnection().getMetaData().getURL();

			try (Connection conn = DriverManager.getConnection(url, user,
					password)) {
				if (conn.isValid(15)) // 15sec timeout
				{
					result = true;
				}
			}catch (Exception e) {
				throw e; 
			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	private static void logSQL(String sql) {
		_logger.info("SQL: " + sql);
	}

}
