package com.sh24wslistener.listener;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.sh24wslistener.utils.ApplicationProperties;

/**
 * Application Lifecycle Listener implementation class sh24wslistener
 *
 */
public class WsListener extends TimerTask implements ServletContextListener {

	private Thread t = null;
	private Timer timer;



    public WsListener() {
        // TODO Auto-generated constructor stub
    }

    static final Logger LOGGER = Logger.getLogger(WsListener.class); 
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent contextEvent)  {
    	
    	 
    	 String segundos = null;
    	 
    	 LOGGER.info("Arrancado listener Sh24wslistener");
    	 System.out.println("Arrancando listener Sh24wslistener");
    	 try {
			 segundos = ApplicationProperties.getProps().getProperty("minutos.ejecucion");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("No se ha encontrado la propiedad minutos.ejecucion");
		}
    	 LOGGER.info("Tiempo de repetición: "+ segundos);
    	 timer = new Timer();
    	 timer.schedule(this, 0, Integer.parseInt(segundos));   

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent contextEvent)  { 
         // TODO Auto-generated method stub
    	 timer.cancel();
    	 timer.purge();
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		LOGGER.info("Ejecutamos listener Sh24wslistener "+new java.util.Date());
		
		ProcesarServicios pS = new ProcesarServicios();
		System.out.println("Ejecutamos listener Sh24wslistener "+new java.util.Date());
		
	}
    
    
	
}


