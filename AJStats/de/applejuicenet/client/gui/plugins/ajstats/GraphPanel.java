package de.applejuicenet.client.gui.plugins.ajstats;

import javax.swing.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.applejuicenet.client.shared.MapSetStringKey;
import de.charts.updownchart.UpDownData;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/ajzab0815/Repository/AJStats/de/applejuicenet/client/gui/plugins/ajstats/GraphPanel.java,v 1.1 2004/01/09 19:26:27 zab0815 Exp $
 *
 * <p>Titel: AppleJuice Core-GUI</p>
 * <p>Beschreibung: Erstes GUI für den von muhviehstarr entwickelten appleJuice-Core</p>
 * <p>Copyright: open-source</p>
 *
 * @author: Maj0r <aj@tkl-soft.de>
 *
 * $Log: GraphPanel.java,v $
 * Revision 1.1  2004/01/09 19:26:27  zab0815
 * Initial checkin
 * Version 0.9.1 Alpha
 *
 * Revision 1.1  2004/01/06 20:49:07  andy
 * Initial checkin
 *
 * Revision 1.4  2003/12/29 11:00:02  zab0815
 * Automatisches scaling der Anzeige und scrolling bei vollem Display, komplett überarbeitet.
 *
 *  * $Log: GraphPanel.java,v $
 *  * Revision 1.1  2004/01/09 19:26:27  zab0815
 *  * Initial checkin
 *  * Version 0.9.1 Alpha
 *  *
 *  * Revision 1.1  2004/01/06 20:49:07  andy
 *  * Initial checkin
 *  *
 * Revision 1.3  2003/12/22 16:25:02  maj0r
 * Bug behoben, der auftratt, wenn das Plugin aktualisiert wurde, obwohl es noch nicht angezeigt wird (Danke an Luke).
 *
 * Revision 1.2  2003/09/15 07:28:45  maj0r
 * Plugin zeigt nun ein Raster und die Zeit auf der x-Achse.
 *
 * Revision 1.1  2003/09/13 11:33:17  maj0r
 * Neues Plugin SpeedGraph.
 *
 *
 */

public class GraphPanel extends JPanel{
    private MapSetStringKey sessionUploadKey = new MapSetStringKey("sessionupload");   
    private MapSetStringKey sessionDownloadKey = new MapSetStringKey("sessiondownload"); 
    
    private static Logger logger;
	private Image image;
	private int maxCred=Integer.MAX_VALUE;
	private UpDownData ud;
    private int oldHeight=0;
    private int oldWidht=0;
	
	
    public GraphPanel() {
    	logger = Logger.getLogger(getClass());
    	try  {
    		setLayout(new BorderLayout());
    		this.setBackground(Color.BLACK);
    		//add(new JScrollPane(ud), BorderLayout.NORTH);
    		setBackground(Color.BLACK);
    		setSize(getWidth(), 150);
    	}
    	catch (Exception e){
    		if (logger.isEnabledFor(Level.ERROR))
    			logger.error("Unbehandelte Exception", e);
    	}
   	
    }
    
    public void paintComponent(Graphics g) {
    	g.setColor(Color.BLACK);
    	g.fillRect(0,0,getWidth(),getHeight());
    	
    	//Wenn kein Image da, oder größe verändet wurde, dann neu graphic erstellen
    	if ((image != null) || (oldHeight != getHeight()) || (oldWidht != getWidth())) {
    		image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    		oldHeight = getHeight();
    		oldWidht = getWidth();
    	
    		//drawLines();
    		drawGraph();
    		
    		g.drawImage(image, 0, 0, Color.BLACK, null);
    		
    	} else
    		super.paintComponent(g);
    }

    public void update(UpDownData ud){
        // check if image is created and if page is shown
    	if (image == null)
        	if (getWidth() > 0 && getHeight() > 0 )
        		image =new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        if (ud.getInstance() != null)
        	this.ud = ud.getInstance();

    	maxCred = (int)(ud.getMaxCredits()/1024);

    	maxCred = (maxCred / 10) * 10;
        	
        
        repaint();
    };
    
    private void drawGraph() {
    	Graphics2D g = (Graphics2D)image.getGraphics();
    	int oldCred=0;
    	
    	for (int i = 0;i < ud.size(); i++) {
    		int cred = (int)(image.getHeight(null) - 
    				(image.getHeight(null)/(maxCred*10) * (ud.get(i).getCredits()/1024)));
    		
    		g.drawLine(31+i,oldCred,32+i,cred);
    		oldCred = cred;
    	}
    	g.dispose();
    
    }
    
    
    private void drawLines() {
    	try {
    		Graphics2D g = (Graphics2D)image.getGraphics();
    		g.setColor(Color.WHITE);
    		for (int i=0;i<maxCred+1;i++){
    			//calculate y-position
    			int yPos = (image.getHeight(null) - (image.getHeight(null)/(maxCred*10)) * i);
    			//draw horizontal line			
    			g.drawLine(29, yPos, image.getWidth(null)-1, yPos);
    		}
    		g.dispose();
    	}
    	catch (Exception e) {
    		if (logger.isEnabledFor(Level.ERROR))
    			logger.error("Error drawing lines :" + e.getMessage() + "\r\n" + e.getCause());
    	}
    	
    }
    
 
}
