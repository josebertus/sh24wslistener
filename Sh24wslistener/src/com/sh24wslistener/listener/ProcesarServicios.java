package com.sh24wslistener.listener;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;



public class ProcesarServicios {
	 
	

	private Logger log;
	
    static final Logger LOGGER = Logger.getLogger(WsListener.class); 

    public ProcesarServicios()  {

        log = Logger.getLogger(ProcesarServicios.class);
        
        //System.out.println("Consumimos los servicios");
        

		String consulta;
		Boolean resprod;
		Clob esquema;
		try{

			System.out.println("llamada demonio pendientes." + new Date() );
       	    LOGGER.info("Inicio PP_DEMONIO_PENDIENTES. " + new Date() );

			/**
			 * Get initial context that has references to all configurations and
			 * resources defined for this web application.
			 */
			Context initialContext = new InitialContext();

			/**
			 * Get Context object for all environment naming (JNDI), such as
			 * Resources configured for this web application.
			 */
			Context environmentContext = (Context) initialContext
					.lookup("java:comp/env");
			/**
			 * Name of the Resource we want to access.
			 */
			String dataResourceName = "jdbc/PgeDB";
			/**
			 * Get the data source for the SQL to request a connection.
			 */
			DataSource dataSource = (DataSource) environmentContext
					.lookup(dataResourceName);
			/**
			 * Request a Connection from the pool of connection threads.
			 */
			Connection conn = dataSource.getConnection();
			StringBuilder msg = new StringBuilder();
			/**
			 * Use Connection to query the database for a simple table listing.
			 * Statement will be closed automatically.
			 */
			Statement stm = conn.createStatement();
			try {
				String query = "BEGIN pac_shws.PP_DEMONIO_PENDIENTES;END;";
				Boolean rs = stm.execute(query);
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			} finally {
				// Release connection back to the pool
				if (conn != null) {
					stm.close();
					stm = null;
					conn.close();
					conn = null;
				}
				
			}			
	        
		}catch (Exception e)
		{ 
			System.out.println("Error"+e);
			log.error("Error: " + e);
		}
       
        	
		System.out.println("Fin demonio pendientes." + new Date() );
		LOGGER.info("Fin PP_DEMONIO_PENDIENTES. " + new Date() );
        
    
    }
    

}
