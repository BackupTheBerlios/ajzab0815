/*
 * Created on 29.12.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.charts.updownchart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JPanel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.applejuicenet.client.gui.controller.ApplejuiceFassade;
import de.applejuicenet.client.shared.MapSetStringKey;

/**
 * @author Zab0815
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class UpDownChart extends JPanel implements MouseListener, MouseMotionListener {
	/*** <code>BG_LEFT_TO_RIGHT</code> draws the gradient from left with <code>BGStart</code> to right with <code>BGEnd</code> */
	public final static int BG_LEFT_TO_RIGHT = 0;
	/*** <code>BG_RIGHT_TO_LEFT</code> draws the gradient from right with <code>BGStart</code> to left with <code>BGEnd</code> */
	public final static int BG_RIGHT_TO_LEFT = 1;
	/*** <code>BG_DOWN_TO_UP</code> draws the gradient from up with <code>BGStart</code> to down with <code>BGEnd</code> */
	public final static int BG_UP_TO_DOWN = 2;
	/*** <code>BG_DOWN_TO_UP</code> draws the gradient from down with <code>BGStart</code> to up with <code>BGEnd</code> */
	public final static int BG_DOWN_TO_UP = 3;
	/*** <code>BG_UPLEFT_TO_DOWNRIGHT</code> draws the gradient from up-left with <code>BGStart</code> to down-right with <code>BGEnd</code> */
	public final static int BG_UPLEFT_TO_DOWNRIGHT = 4;
	/*** <code>BG_DOWNRIGHT_TO_UPLEFT</code> draws the gradient from down-right with <code>BGStart</code> to up-left with <code>BGEnd</code> */
	public final static int BG_DOWNRIGHT_TO_UPLEFT = 5;
	/*** <code>BG_UPRIGHT_TO_DOWNLEFT</code> draws the gradient from up-right with <code>BGStart</code> to down-left with <code>BGEnd</code> */
	public final static int BG_UPRIGHT_TO_DOWNLEFT = 6;
	/*** <code>BG_DOWNLEFT_TO_UPRIGHT</code> draws the gradient from down-left with <code>BGStart</code> to up-right with <code>BGEnd</code> */
	public final static int BG_DOWNLEFT_TO_UPRIGHT = 7;
	
	private final int minChartHeight = 100;

    private static Logger logger;
    
	private static int gradientDirection = BG_DOWN_TO_UP;
	private static Color BGEnd = new Color(20, 200, 20);
	private static Color BGStart = new Color(0, 0, 0);
	private Color downCol = new Color(20, 20, 200);
	private Color upCol = new Color(200, 20, 20);
	private Color creditCol = new Color(255, 255, 0);
	private Color lineCol = new Color(255, 255, 255);
	
	private UpDownData ud=null;

	private int maxWidth = 0;
	private int legendX = 250;
	private int legendY = 10;
	private int legendWidth = 200;
	private int legendHeight = 20;
	private int legendAlpha=128;
	private int mouseX;
	private int mouseY;

	private int topY = 0;
	private int bottomY = 0;
	private int bottomHeight = 30;
	private int maxSpeed = 10;
	private int oldHeight=0;
	private int oldWidth=0;
	
	private Image image = null;
	private BufferedImage backImage = null;
	private BufferedImage legendImage = null;
	
	private boolean dragging = false;	
	private boolean forceResize =false;
	
	private MapSetStringKey uploadSpeedKey = new MapSetStringKey("uploadspeed"); //$NON-NLS-1$
	private MapSetStringKey downloadSpeedKey = new MapSetStringKey("downloadspeed"); //$NON-NLS-1$
	private MapSetStringKey creditsKey = new MapSetStringKey("credits"); //$NON-NLS-1$
	private MapSetStringKey sessionUploadKey = new MapSetStringKey("sessionupload"); //$NON-NLS-1$
	private MapSetStringKey sessionDownloadKey = new MapSetStringKey("sessiondownload"); //$NON-NLS-1$
	private long maxCred;
	
	public UpDownChart() {
		logger = Logger.getLogger(getClass());
		addMouseListener(this);
		try {
			ud =new UpDownData(1);
			ud.clear();
		} catch (NullPointerException e) {
			if (logger != null) {
				if (logger.isEnabledFor(Level.ERROR))
					logger.error("Nullpointer-exception: data-storage not initialized!"); //$NON-NLS-1$
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && dragging==true) {
			if (legendX + e.getX() - mouseX > 0)
				legendX += e.getX() - mouseX;
			else legendX =0;
			
			if (legendY + e.getY() - mouseY > topY)
				legendY += e.getY() - mouseY;
			else legendY =topY;
			
			dragging = false;
			legendAlpha = 80;
			repaint();
		}
	};
	
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && dragging == false) {
			if (e.getX() > legendX && e.getX()< legendX+200) {
				if (e.getY() > legendY && e.getY() < legendY + 20) {
					dragging = true;
					mouseX = e.getX();
					mouseY = e.getY();
					legendAlpha = 200;
					repaint();
				}
			}
		} 
	};

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if (legendX + e.getX() - mouseX > 0)
			legendX += e.getX() - mouseX;
		else legendX =0;
		
		if (legendY + e.getY() - mouseY > topY)
			legendY += e.getY() - mouseY;
		else legendY =topY;
		legendAlpha = 80;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {};
	public void mouseExited(MouseEvent e) {dragging =false;};
	public void mouseEntered(MouseEvent e) {};
	public void mouseClicked(MouseEvent e) {};
	
	
	public void update(HashMap speeds){
		initializeImages();
		
		if (ud == null) {
			ud =new UpDownData(1);
		}
		
		//add data to the array
		try {
			Long upSpeed = (Long)speeds.get(uploadSpeedKey);
			Long downSpeed = (Long)speeds.get(downloadSpeedKey);
			Long credits = (Long)speeds.get(creditsKey);
			ud.add(upSpeed.longValue(), downSpeed.longValue(), credits.longValue(), System.currentTimeMillis());
		} catch (NullPointerException e) {
			//No Data, or no Time then exit
			return;
		}

		if (getWidth()==0 || getHeight()==0){
			/*
			 Da kamen schneller Daten rein, als das Plugin angezeigt werden konnte.
			 Wir nehmen folglich erst den naechsten Durchgang.
			 */
			return;
		}

		//Adjust scaling of maximum values from Up/Download rate.
		//Maximum loopcout = 3 to prevent a lockup.
		int iLoopCnt = 3;
		boolean SpeedChange=false;
		do {
			if ((ud.getMaxUp() < (maxSpeed-10)*1024) && (ud.getMaxDown() < (maxSpeed-10)*1024)) {
				maxSpeed = (int)(ud.getMaxUp()>ud.getMaxDown()?ud.getMaxUp():ud.getMaxDown())/1024;
				//SpeedChange = true;
			}
			// adjust scaling for up/down rate if needed
			if ((ud.getMaxUp() > maxSpeed*1024) || (ud.getMaxDown() > maxSpeed*1024)) {
				maxSpeed += 10;
				SpeedChange = true;
			}
			maxSpeed = (maxSpeed / 10) * 10;
			if (SpeedChange == true) {
				forceResize = true;
			}
			
			//bei zu kleinem Down/upload einfach 10Kb als kleinste Scalierung setzen
			if (maxSpeed == 0) {
				maxSpeed = 10;
				break;
			}
				
		} while ((SpeedChange == true) && (--iLoopCnt>0));
	
		//check if we're running out of visible area
		if (ud.size() >= maxWidth){
			///remove the first element of data
			ud.remove(0);
			image.getGraphics().clearRect(31,0, image.getWidth(null), image.getHeight(null));
			image.getGraphics().drawImage(backImage, 0, 0, image.getWidth(null), image.getHeight(null), new Color(0,0,0), null);
			repaint();
		}
		
		//drawGraph();
		repaint();
	}
		

	public void paintComponent(Graphics g) {
		
		if (image != null) {
	        if (getHeight()!=oldHeight || forceResize == true){
	            if (getHeight()>=minChartHeight){
	            	oldHeight = getHeight();
					bottomY = getHeight() - bottomHeight;

					image = null;
					backImage = null;
					initializeImages();					
	            }
	        }
	        
	        if (getWidth()!=oldWidth || forceResize == true){
	        	oldWidth = getWidth();
	            maxWidth = getWidth();
	            
	            image = null;
	            backImage = null;
	            initializeImages();					
	        }
	        
	        try {
	        	Graphics2D g2 = (Graphics2D)image.getGraphics();
	        	g2.clearRect(0,0,image.getWidth(null),image.getHeight(null));

	        	//copy backImage to components Image
	        	g2.drawImage(backImage, 0, 0, image.getWidth(null), image.getHeight(null)
	        				,0 , 0, backImage.getWidth(), backImage.getHeight(),Color.BLACK,null);

	        	
	        	drawLabel();
	        	drawLines();
	        	drawGraph();
	        	
	        	g.drawImage(image,0,0,null);
	        	g.drawImage(legendImage, legendX, legendY, new Color(0,0,0,legendAlpha), null);
	        	forceResize =false;
	        }
	        catch (Exception e) {
	        	if (logger.isEnabledFor(Level.ERROR))
	        		logger.error("Error drawing graphics :" + e.getMessage() + "\r\n" + e.getCause()); //$NON-NLS-1$ //$NON-NLS-2$
	        }
	    }
	    else{
	        super.paintComponent(g);
	    }
	}

	/**
	 * 
	 */
	private void drawGraph() {
		Graphics2D g = (Graphics2D)image.getGraphics();
		int lastDown = 0;
		int lastUp = 0;

		//get first time-stamp
		long lastTimeLabel = ud.get(0).getTime();
		double lAverageUp = 0.0;
		double lAverageDown = 0.0;
		
		//draw upload rate-graph
		for (int i = 0;i < ud.size();i++) {
			int up = (int)(ud.get(i).getUp() * bottomY / maxSpeed / 1024.0);
			int down = (int)(ud.get(i).getDown() * bottomY / maxSpeed / 1024.0);
			lAverageUp += ud.get(i).getUp();
			lAverageDown += ud.get(i).getDown();
			
			g.setColor(upCol);
			g.drawLine(31+i, bottomY - lastUp , i+32, bottomY - up );
			g.setColor(downCol);
			g.drawLine(31+i, bottomY - lastDown, i+32, bottomY - down);

			if (i % 100 == 0){
				lastTimeLabel = ud.get(i).getTime();
				drawTime(i+31, ud.get(i).getTime());
			}
			
			lastUp = up;
			lastDown = down;
		}
		
		Stroke s =new BasicStroke(1, BasicStroke.CAP_ROUND, 
				BasicStroke.JOIN_ROUND, 0, new float[]{6,6,6,6,6,6,0,0}, 0);
		
		NumberFormat nf = DecimalFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		drawMark(g, lAverageUp/ud.size(), nf, upCol, s,Messages.getString("UpDownChart.8") ); //$NON-NLS-1$
		drawMark(g, lAverageDown/ud.size(), nf, downCol, s,Messages.getString("UpDownChart.9") ); //$NON-NLS-1$

		//get settings from GUI and draw lines for max-settings
		s =new BasicStroke(1, BasicStroke.CAP_ROUND, 
				BasicStroke.JOIN_ROUND, 0, new float[]{6,6,0,0,6,6,0,0}, 0);
		double AJUpSpeed = ApplejuiceFassade.getInstance().getAJSettings().getMaxUpload();
		double AJDownSpeed = ApplejuiceFassade.getInstance().getAJSettings().getMaxDownload();
		if (AJUpSpeed == 0) AJUpSpeed = 128000/8;
		if (AJDownSpeed == 0) AJDownSpeed = 768000/8;
		drawMark(g, AJUpSpeed, nf, Color.GRAY, s, Messages.getString("UpDownChart.10") ); //$NON-NLS-1$
		drawMark(g, AJDownSpeed, nf, Color.GRAY, s ,Messages.getString("UpDownChart.11")); //$NON-NLS-1$
		
		g.dispose();
	}		

	/**
	 * Draw horizontal Line as mark
	 * @param g graphics to draw in
	 * @param speedVal speed in Byte/s
	 * @param nf NumberFormat for text
	 * @param col Color to draw text and Line
	 * @param stroke Stroke to draw line with
	 */
	private void drawMark(Graphics2D g, double speedVal, NumberFormat nf, Color col, Stroke stroke, String optText) {
		Double value = new Double(speedVal/1024.0);
		
		g.setStroke(stroke);
		g.setColor(col.brighter());
		
		speedVal = bottomY - (speedVal * bottomY / maxSpeed / 1024.0);
		g.drawLine(31, (int)speedVal, image.getWidth(null), (int)speedVal);
		g.setColor(col.brighter());
		String outText = new String(nf.format(value) + " " + optText); //$NON-NLS-1$
		
		g.drawString(outText, image.getWidth(null)-g.getFontMetrics().stringWidth(outText)-10, (int)speedVal - 10);
	}
	
	/**
	 * 
	 */
	private void drawLabel() {
		
		try {
			Graphics2D g = (Graphics2D)image.getGraphics();
			FontMetrics fm = g.getFontMetrics();
			
			//setup format of outtext
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumIntegerDigits(3);
			
			//first clear old text - fill with gradient
			//g.setPaint(getGradient(gradientDirection,image.getHeight(null),30));
			//g.fillRect(0, 0, 30, image.getHeight(null));
			
			//setup font
			g.setColor(Color.WHITE);
			int strWidth = fm.stringWidth ( "XXX -" ); //$NON-NLS-1$
			int strHeight = fm.getAscent();
			g.setFont(new Font(g.getFont().getName(), Font.PLAIN, g.getFont().getSize()));
			g.drawString("kb/s", 30-g.getFontMetrics().stringWidth("kb/s "), image.getHeight(null)-strHeight); //$NON-NLS-1$ //$NON-NLS-2$
			
			
			//draw text for speed
			for (int i=0;i<(maxSpeed/10)+1;i++){
				//calculate y-position
				int yPos = (int)(bottomY-((bottomY - topY - strHeight*1.0)/( maxSpeed/10)) * i);
				
				//set color and draw text for speed
				g.drawString(nf.format(i*10), 30-strWidth, yPos+strHeight/2);
			}
			g.setColor(lineCol);
			g.drawLine(30,topY,30,bottomY);
			g.dispose();
		}
		catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR))
				logger.error("Error drawing labels :" + e.getMessage() + "\r\n" + e.getCause()); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * 
	 */
	private void drawLegend(int alpha) {
		try {
			Graphics2D g = (Graphics2D)legendImage.getGraphics();
			
			g.setFont(new Font(g.getFont().getName(), Font.BOLD, g.getFont().getSize()));
			int strHeight = g.getFontMetrics().getAscent();

			//fill transparent area
			g.setColor (new Color(0, 0, 0, 255));
			g.fillRect(0, 0, legendWidth, legendHeight);
			
			//draw rounded frame around Legend
			g.setColor(new Color(0, 0, 0 ,alpha));    	
			g.fillRoundRect(1, 1, legendWidth-2, legendHeight-2, 7, 7);
			g.setColor(lineCol);    	
			g.drawRoundRect(1, 1, legendWidth-2, legendHeight-2, 7, 7);
			
			//now draw text
			g.setColor(Color.WHITE);
			g.drawString(Messages.getString("UpDownChart.18"), 32, strHeight+3); //$NON-NLS-1$
			g.drawString(Messages.getString("UpDownChart.19"), 132, strHeight+3); //$NON-NLS-1$

			//and the lines before the text
			g.setStroke(new BasicStroke((float) 2.0));
			g.setColor(downCol);
			g.drawLine(10, (int)(strHeight*1), 25, (int)(strHeight*1));
			
			g.setColor(upCol);
			g.drawLine(110, (int)(strHeight*1),125, (int)(strHeight*1));
			g.dispose();
		}
		catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR))
				logger.error("Error painting legend :" + e.getMessage() + "\r\n" + e.getCause()); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * 
	 */
	private void drawBackground(){
		try {
			Graphics2D g = (Graphics2D)backImage.getGraphics();
			g.setPaint(getGradient(gradientDirection,backImage.getHeight(),backImage.getWidth()));
			g.fillRect(0, 0, backImage.getWidth(), backImage.getHeight());
			g.dispose();
		}
		catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR))
				logger.error("Error painting background :" + e.getMessage() + "\r\n" + e.getCause()); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * 
	 */
	private void drawLines() {
		try {
			Graphics2D g = (Graphics2D)image.getGraphics();
			g.setColor(lineCol);
			for (int i=0;i<(maxSpeed/10)+1;i++){
				//calculate y-position
				int yPos = bottomY - ((bottomY-topY)/(maxSpeed/10)) * i;
				//draw horizontal line			
				g.drawLine(29, yPos, getWidth()-1, yPos);
			}
			g.dispose();
		}
		catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR))
				logger.error("Error drawing lines :" + e.getMessage() + "\r\n" + e.getCause()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
	}

	/**
	 * 
	 */
	private void drawTime(int aktXPos, long time) {
		try {
			Graphics2D g = (Graphics2D)image.getGraphics();
			
			g.setFont(new Font(g.getFont().getName(), g.getFont().getStyle(), 10));
			int strHeight = g.getFontMetrics().getAscent();
			
			String actualTime = new SimpleDateFormat("HH:mm").format(new Date(time)); //$NON-NLS-1$
			int strWidth = g.getFontMetrics().stringWidth (actualTime);
			g.drawString(actualTime, aktXPos, bottomY + strHeight + 3);
			
			g.setColor(Color.RED);
			float[] dash = {(float)5.0,(float)2.0};
			g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, 
					BasicStroke.JOIN_ROUND, 0, new float[]{0,0,0,6,0,0,0,6}, 0));
			g.drawLine(aktXPos, topY - 2 , aktXPos, bottomY - 2);
			g.dispose();
		}
		catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR))
				logger.error("Error drawing time :" + e.getMessage() + "\r\n" + e.getCause()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
	}

	/**
	 * 
	 */
	private void initializeImages() {
		try {
			if (image==null){
				try {
					if (getHeight()<minChartHeight){
						super.setSize(minChartHeight,getWidth());
					}

					bottomY = getHeight() - bottomHeight;
					maxWidth = getWidth()-31;
					
					
					image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
					//System.out.print("new image("+ image.getWidth(null)+ "," + image.getHeight(null) +")\r\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

			if (legendImage == null) {
				try {
					legendImage = new BufferedImage(legendWidth, legendHeight, BufferedImage.TYPE_INT_ARGB);
					drawLegend(legendAlpha);
					//System.out.print("new legendImage("+ legendImage.getWidth(null)+ "," + legendImage.getHeight(null) +")\r\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

			if (backImage == null) {
				try {
					switch (gradientDirection) {
					case UpDownChart.BG_DOWN_TO_UP :
					case UpDownChart.BG_UP_TO_DOWN :
						backImage = new BufferedImage(10, getHeight()-topY, BufferedImage.TYPE_INT_ARGB);
						break;
					case UpDownChart.BG_LEFT_TO_RIGHT:
					case UpDownChart.BG_RIGHT_TO_LEFT:
						backImage = new BufferedImage(getWidth()-31, 10, BufferedImage.TYPE_INT_ARGB);
						break;
					default :
						backImage = new BufferedImage(10, getHeight()-topY, BufferedImage.TYPE_INT_ARGB);
						break;
					}
					//System.out.print("new backImage("+ backImage.getWidth(null)+ "," + backImage.getHeight(null) +")\r\n");
					drawBackground();
					drawLines();
					drawLabel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			if (logger.isEnabledFor(Level.ERROR))
				logger.error("Error creating Image :" + e.getMessage() + "\r\n" + e.getCause()); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Generates a gradient in the selected direction of the given <code>BGStart</code> and <code>BGEnd</code> colors.
	 * The direction may either be one of the following:<br>
	 * <code>BG_LEFT_TO_RIGHT</code><br>
	 * <code>BG_RIGHT_TO_LEFT</code><br>
	 * <code>BG_UP_TO_DOWN</code><br>
	 * <code>BG_DOWN_TO_UP</code><br>
	 * <code>BG_UPLEFT_TO_DOWNRIGHT</code><br>
	 * <code>BG_DOWNRIGHT_TO_UPLEFT</code><br>
	 * <code>BG_UPRIGHT_TO_DOWNLEFT</code><br>
	 * <code>BG_DOWNLEFT_TO_UPRIGHT</code><br>
	 * @param direction: see above
	 * @param height: height of the gradient
	 * @param width: width of the gradient
	 * @return the <code>GradientPaint</code> of the selected direction and size
	 */
	private static GradientPaint getGradient(int direction, int height, int width) {
		int x1=0; 
		int x2=0;
		int y1=0;
		int y2=0;
		switch (direction) {
			case BG_UP_TO_DOWN:
				x1=1; x2=1;
				y1=height;y2=0;
				break;
			case BG_DOWN_TO_UP:
				x1=1; x2=1;
				y1=0; y2=height;
				break;
			case BG_LEFT_TO_RIGHT:
				x1=0; x2=width;
				y1=1; y2=1;
				break;
			case BG_RIGHT_TO_LEFT:
				x1=width; x2=0;
				y1=1; y2=1;
				break;
			case BG_UPLEFT_TO_DOWNRIGHT:
				x1=0; x2=width;
				y1=height;y2=0;
				break;
			case BG_UPRIGHT_TO_DOWNLEFT:
				x1=width; x2=0;
				y1=height;y2=0;
				break;
			case BG_DOWNLEFT_TO_UPRIGHT:
				x1=0; x2=width;
				y1=0; y2=height;
				break;
			case BG_DOWNRIGHT_TO_UPLEFT:
				x1=width; x2=0;
				y1=0; y2=height;
				break;
		}
		return new GradientPaint (x1, y1, BGStart, x2, y2, BGEnd);
	}
		
	
	/**
	 * @return Returns the ud.
	 */
	public UpDownData getData() {
		return ud;
	}
	/**
	 * @return Returns the bGEnd.
	 */
	public Color getBGEnd() {
		return BGEnd;
	}

	/**
	 * @param end The bGEnd to set.
	 */
	public void setBGEnd(Color end) {
		BGEnd = end;
	}

	/**
	 * @return Returns the bGStart.
	 */
	public Color getBGStart() {
		return BGStart;
	}

	/**
	 * @param start The bGStart to set.
	 */
	public void setBGStart(Color start) {
		BGStart = start;
	}

	/**
	 * @return Returns the downCol.
	 */
	public Color getDownCol() {
		return downCol;
	}

	/**
	 * @param downCol The downCol to set.
	 */
	public void setDownCol(Color downCol) {
		this.downCol = downCol;
	}

	/**
	 * @return Returns the creditCol.
	 */
	public Color getCreditCol() {
		return creditCol;
	}
	/**
	 * @param creditCol The creditCol to set.
	 */
	public void setCreditCol(Color creditCol) {
		this.creditCol = creditCol;
	}

	/**
	 * @return Returns the lineCol.
	 */
	public Color getLineCol() {
		return lineCol;
	}

	/**
	 * @param lineCol The lineCol to set.
	 */
	public void setLineCol(Color lineCol) {
		this.lineCol = lineCol;
	}

	/**
	 * @return Returns the maxSpeed.
	 */
	public int getMaxSpeed() {
		return maxSpeed;
	}

	/**
	 * @param maxSpeed The maxSpeed to set.
	 */
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * @return Returns the image.
	 */
	public Image getImage() {
		return image;
	}
	/**
	 * @return Returns the maxWidth.
	 */
	public int getMaxWidth() {
		return maxWidth;
	}

	/**
	 * @param maxWidth The maxWidth to set.
	 */
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	/**
	 * @return Returns the upCol.
	 */
	public Color getUpCol() {
		return upCol;
	}

	/**
	 * @param upCol The upCol to set.
	 */
	public void setUpCol(Color upCol) {
		this.upCol = upCol;
	}

	/**
	 * @return Returns the gradientDirection.
	 */
	public int getGradientDirection() {
		return gradientDirection;
	}

	/**
	 * @param gradientDirection The gradientDirection to set.
	 */
	public void setGradientDirection(int gradientDirection) {
		UpDownChart.gradientDirection = gradientDirection;
	}



}
