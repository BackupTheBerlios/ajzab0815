//////////////// JavaBrowser.java //////////
package de.utils.javabroser;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.text.html.StyleSheet;

import de.applejuicenet.client.gui.controller.ApplejuiceFassade;

public class JavaBrowser extends JPanel implements HyperlinkListener, ActionListener {

	private JLabel addrLabel = new JLabel ( "Address:" ) ;
	private JTextField addrText = new JTextField ( "http://www.applelinks.org" ) ;
	private JButton goButton = new JButton ( "Go" ) ;
	private JButton goBack = new JButton("Back)");
	private JButton goFwd = new JButton("Forward");

	private JEditorPane browser = new JEditorPane ( ) ; // The main HTML pane
	private HTMLEditorKit m_kit;
	private HTMLDocument m_doc;
	private StyleSheet m_context;
	private URL url;
	
	public JavaBrowser ( ) {

		/* Make the JEditoPane non-editable so that when user
		 clicks on a link then another page is loaded. By default, 
		 JEditorPane is editable and works as a HTML editor not as
		 a browser. Make make it work as a browser we must make it
		 non-editable */

		browser.setEditable ( false ) ;
		
		browser.setEditorKit(m_kit);
		

		/* Add a hyperlink listener so that when user clicks on a 
		 link then hyperlinkUpdate ( ) method is called and we
		 will load another page */

		browser.addHyperlinkListener ( this ) ;


		//Put the address text filed and button on the north of frame
		JPanel bp =new JPanel();
		bp.setLayout ( new GridLayout(1,5)) ;
		bp.setBorder(new CompoundBorder (
				new BevelBorder(BevelBorder.LOWERED),
				new EmptyBorder(2,2,2,2)));
		bp.add (goBack);
		bp.add (goFwd);
		bp.add ( addrLabel ) ;
		bp.add ( addrText ) ;
		bp.add ( goButton ) ;
		
		// Add the action listener to button and address text field so that
		// when user hits enter in address text filed or presses the Go button
		// then new page is displayed
		addrText.addActionListener ( this ) ;
		goButton.addActionListener ( this ) ;

		setLayout(new BorderLayout(5,5));
		add (bp, BorderLayout.NORTH);

		// Add the panel to the north
		browser.setSize(getWidth(),getHeight());
		add ( new JScrollPane ( browser, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.SOUTH) ;
		
	}


	/**
	 * @param string
	 */
	public JavaBrowser(URL url) {
		new JavaBrowser();
		try {
			this.displayPage(url.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public void displayPage ( String page ) throws Exception {
		// Check if user has specified any command line parameter
		if ( page != null && page.trim().length() > 0 ) {

			// Set this address
			addrText.setText ( page ) ;

			/* User may specify one of the following
			 1. A relative path for a local file
			 2. An absolute path for a local file
			 3. A URL 
			 Check for a valid user input
			 */

			File localFile = new File ( page ) ;

			// Chgeck if the file exists on the dist
			if ( localFile.exists ( ) && localFile.isFile () ) {
				/* Check if user specified the absolute path 
				 Add the file protocol in front of file name */

				page = "file:///" + localFile.getAbsolutePath ( ) ;
				try {
					browser.setPage ( page ) ;
				}
				catch ( Exception e1 ) {
					// Not a valid URL 
					browser.setText ( "Could not load file:" + page + "\n" + 
							"Error:" + e1.getMessage ( ) ) ;
				}
			}
			else {
				// Maybe user specified a URL
				try {
					URL url = new URL ( page ) ;
//					URL url = new URL ( "http://192.168.0.1/index.html" ) ;
//					URLConnection conn = url.openConnection();
//					Reader rd = new InputStreamReader(conn.getInputStream());
//					
//					// Parse the HTML
//					m_kit =new HTMLEditorKit();
//
//					m_doc =(HTMLDocument)m_kit.createDefaultDocument();
//					m_doc.putProperty("IgnoreCharsetDirective", new Boolean(false)); 
//					m_kit.read(rd, m_doc, 0);
//					m_context =m_doc.getStyleSheet();
//					browser.setDocument(m_doc);
//					rd.close();
					browser.setPage ( url ) ;
					browser.setFont(new Font("",0,20));
					
				}
				catch ( Exception e ) {
					//Not a valid URL 
					//m_context =m_doc.getStyleSheet();
					//browser.setDocument(m_doc);
					//rd.close();
					browser.setText ( "Could not load page:" + page + "\n" + 
							"Error:" + e.getLocalizedMessage()) ;
					
				}

			}

		}
		else {
			browser.setText ( "Could not load page:" + page ) ;
		}

	}



	public void hyperlinkUpdate ( HyperlinkEvent e ) {
		/* Get the event type for the link. There could be three types
		 of event generated by user actions on a link.When user's mouse
		 enters a link then ENTERED event is triggerd. When user clicks the
		 link the ACTIVATED is triggered and when user exits the link then
		 EXITED is triggerd. */
		// Das �ndern des Mauszeigers geht ab 
		// Java 1.3 auch automatisch 
		if (e.getEventType() == 
			HyperlinkEvent.EventType.ENTERED) {
			((JEditorPane) e.getSource()).setCursor(
					Cursor.getPredefinedCursor(
							Cursor.HAND_CURSOR));
		} else
			if (e.getEventType() == 
				HyperlinkEvent.EventType.EXITED) {
				((JEditorPane) e.getSource()).setCursor(
						Cursor.getPredefinedCursor(
								Cursor.DEFAULT_CURSOR));
			} else
				// Hier wird auf ein Klick reagiert
				if (e.getEventType() == 
					HyperlinkEvent.EventType.ACTIVATED) {
					JEditorPane pane = (JEditorPane) e.getSource(); 
					if (e instanceof HTMLFrameHyperlinkEvent) {
						HTMLFrameHyperlinkEvent evt = 
							(HTMLFrameHyperlinkEvent)e;
						HTMLDocument doc = 
							(HTMLDocument)pane.getDocument();
						doc.processHTMLFrameHyperlinkEvent(evt);
					} else try {
						// Normaler Link
						if (e.getDescription().toLowerCase().startsWith("ajfsp://")) {
							System.out.println(e.getDescription());
							ApplejuiceFassade.getInstance().processLink(e.getDescription());
						} else {
							System.out.println(e.getURL());
						
							pane.setPage(e.getURL());
						}
					} catch (Throwable t) {
						System.out.println(e.toString());
						t.printStackTrace();
					}
				}
	}
/*	
		if ( e.getEventType ( ) == HyperlinkEvent.EventType.ACTIVATED ) {
			try {
				// Loads the new page represented by link clicked
				URL url = e.getURL ( ) ;
				browser.setPage ( url ) ;
				addrText.setText ( url.toString ( ) ) ; 
			}
			catch ( Exception exc ) {
			}
		}

	}
*/
	public void actionPerformed ( ActionEvent e ) {
		// Load the new page
		String page = "" ;
		try {
			// Get the new url
			page = addrText.getText ( ) ;

			// User may eneter a file name or URL. displayPage handles both of them
			displayPage ( page ) ;
		}
		catch ( Exception exc ) {
			browser.setText ( "Page could not be loaded:" + page + "\n" + 
					"Error:" + exc.getMessage ( ) ) ;
		}
	}
} 

