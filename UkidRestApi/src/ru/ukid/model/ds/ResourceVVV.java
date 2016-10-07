package ru.ukid.model.ds;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;

import ru.ukid.rest.utils.UkidConstants;


@Stateless
public class ResourceVVV {
	
	@Resource(name=UkidConstants.DATA_SOURCE_JNDI, lookup=UkidConstants.DATA_SOURCE_JNDI)
    private javax.sql.DataSource _ukidDS;

	public Connection getConnection() {
		Connection conn = null;
		try
        {
            conn = _ukidDS.getConnection();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
      
        return conn;
    }

}
