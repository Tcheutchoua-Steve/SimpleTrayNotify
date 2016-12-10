package net.java.simpletraynotify;

/**
 * SimpleNotifyFrame
 * Copyright (c) 2011 Florian Gattung fgattug
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, merge, 
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons 
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.awt.AWTUtilities;

@SuppressWarnings("restriction")
public class SimpleNotifyFrame extends JWindow {
	private static final long serialVersionUID = 1L;
	private javax.swing.JButton jActionButton;
	private javax.swing.JButton jExitButton;
	private javax.swing.JLabel jIconLabel;
	private javax.swing.JLabel jHeaderLabel;
	private javax.swing.JLabel jContentLabel;
	private javax.swing.JPanel jMainPanel;
	private ActionListener actionListener;
	private boolean closeOnAction;

	/** Creates new form TrayNotification */
	public SimpleNotifyFrame() {
		// setUndecorated(true);
		initComponents();
		enableIcon(MessageType.ERROR);

		disableActionButton();
		disableHeader();
		disableContent();
		disableIcon();
		setLookAndFeel();
		this.initShape(this);

	}

	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(SimpleNotifyFrame.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(SimpleNotifyFrame.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(SimpleNotifyFrame.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(SimpleNotifyFrame.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	public SimpleNotifyFrame enableActionButton(String text,
			ActionListener listener) {
		return this.enableActionButton(text, listener, false);
	}

	public SimpleNotifyFrame enableActionButton(String text,
			ActionListener listener, boolean closeOnAction) {
		this.jActionButton.setText(text);
		this.actionListener = listener;
		this.jActionButton.setVisible(true);
		this.closeOnAction = closeOnAction;
		pack();
		return this;
	}

	private void initShape(final JWindow window) {
		this.addComponentListener(new ComponentAdapter() {
			private boolean transparencySupported = true;

			@Override
			public void componentResized(ComponentEvent evt) {

				if (transparencySupported) {
					try {
						Shape shape = null;
						shape = new RoundRectangle2D.Float(0, 0, getWidth(),
								getHeight(), 20, 20);
						AWTUtilities.setWindowShape(window, shape);
					} catch (UnsupportedOperationException e) {
						transparencySupported = false;
					}
				}

			}

		});
		
	}

	public SimpleNotifyFrame disableActionButton() {
		this.jActionButton.setVisible(false);
		this.actionListener = null;

		pack();
		return this;
	}

	public SimpleNotifyFrame enableHeader(String text) {
		if (text != null) {
			this.jHeaderLabel.setText("<html>" + text + "</html>");
			this.jHeaderLabel.setVisible(true);
		} else {
			this.disableHeader();
		}

		pack();
		return this;
	}

	public SimpleNotifyFrame disableHeader() {
		this.jHeaderLabel.setVisible(false);

		pack();
		return this;
	}

	public SimpleNotifyFrame enableContent(String text) {
		if (text != null) {
			this.jContentLabel.setVisible(false);
			this.jContentLabel.setText("<html>" + text + "</html>");
			this.jContentLabel.setVisible(true);

		} else {
			this.disableContent();
		}

		pack();
		return this;
	}

	public SimpleNotifyFrame disableContent() {
		this.jContentLabel.setVisible(false);

		pack();
		return this;
	}

	public SimpleNotifyFrame enableIcon(TrayIcon.MessageType messageType) {
		jIconLabel.setVisible(true);

		switch (messageType) {
		case ERROR:
			jIconLabel
					.setIcon(new javax.swing.ImageIcon(
							getClass()
									.getResource(
											"/net/java/simpletraynotify/resources/ico_error.png")));
			break;
		case INFO:
			jIconLabel
					.setIcon(new javax.swing.ImageIcon(
							getClass()
									.getResource(
											"/net/java/simpletraynotify/resources/ico_info.png")));
			break;
		case WARNING:
			jIconLabel
					.setIcon(new javax.swing.ImageIcon(
							getClass()
									.getResource(
											"/net/java/simpletraynotify/resources/ico_warn.png")));
			break;
		case NONE:
			this.disableIcon();
			break;
		}

		pack();
		return this;
	}

	public SimpleNotifyFrame disableIcon() {
		jIconLabel.setIcon(null);
		jIconLabel.setVisible(false);

		pack();
		return this;
	}

	private void initComponents() {
		jMainPanel = new javax.swing.JPanel();
		jIconLabel = new javax.swing.JLabel();
		jHeaderLabel = new javax.swing.JLabel();
		jContentLabel = new javax.swing.JLabel();
		jActionButton = new javax.swing.JButton();
		jExitButton = new javax.swing.JButton();

		jMainPanel.setBackground(new java.awt.Color(255, 255, 225));
		jHeaderLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
		jHeaderLabel.setText("This is the header");

		jContentLabel
				.setText("<html>This is some more or less lengthy content This is some more or less lengthy content This is some more or less lengthy content</html>");
		jActionButton.setText("Click");
		jActionButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jActionButtonActionPerformed(evt);
			}
		});

		jExitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/net/java/simpletraynotify/resources/ico_close.png")));
		jExitButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jExitButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jMainPanelLayout = new javax.swing.GroupLayout(
				jMainPanel);
		jMainPanel.setLayout(jMainPanelLayout);
		jMainPanelLayout
				.setHorizontalGroup(jMainPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								GroupLayout.Alignment.TRAILING,
								jMainPanelLayout
										.createSequentialGroup()
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												jMainPanelLayout
														.createParallelGroup(
																GroupLayout.Alignment.TRAILING)
														.addComponent(
																jActionButton)
														.addGroup(
																jMainPanelLayout
																		.createParallelGroup(
																				GroupLayout.Alignment.LEADING,
																				false)
																		.addComponent(
																				jContentLabel,
																				GroupLayout.PREFERRED_SIZE,
																				380,
																				GroupLayout.PREFERRED_SIZE)
																		.addGroup(
																				jMainPanelLayout
																						.createSequentialGroup()
																						.addComponent(
																								jIconLabel)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																						.addComponent(
																								jHeaderLabel,
																								GroupLayout.PREFERRED_SIZE,
																								233,
																								GroupLayout.PREFERRED_SIZE)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								jExitButton,
																								GroupLayout.PREFERRED_SIZE,
																								15,
																								GroupLayout.PREFERRED_SIZE))))
										.addGap(38, 38, 38)));
		jMainPanelLayout
				.setVerticalGroup(jMainPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								jMainPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jMainPanelLayout
														.createParallelGroup(
																GroupLayout.Alignment.TRAILING,
																false)
														.addGroup(
																GroupLayout.Alignment.LEADING,
																jMainPanelLayout
																		.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				jHeaderLabel,
																				GroupLayout.DEFAULT_SIZE,
																				21,
																				Short.MAX_VALUE)
																		.addComponent(
																				jExitButton,
																				GroupLayout.PREFERRED_SIZE,
																				15,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																jIconLabel,
																GroupLayout.Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addGap(7, 7, 7)
										.addComponent(jContentLabel, 0,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jActionButton)
										.addContainerGap(0, 0)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jMainPanel,
				GroupLayout.PREFERRED_SIZE, 406, GroupLayout.PREFERRED_SIZE));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jMainPanel, 0,
				GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		jActionButton.setVisible(false);
		pack();
	}

	private void jExitButtonActionPerformed(java.awt.event.ActionEvent evt) {
		this.dispose();
	}

	private void jActionButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (this.actionListener != null)
			this.actionListener.actionPerformed(evt);

		if (closeOnAction)
			this.dispose();
	}

	void dispatchOnSwingThread(Runnable r) {
		if (SwingUtilities.isEventDispatchThread()) {
			r.run();
		} else {
			try {
				SwingUtilities.invokeLater(r);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public SimpleNotifyFrame enableDraggable() {
		MoveMouseListener mml = new MoveMouseListener(this);
		this.addMouseListener(mml);
		this.addMouseMotionListener(mml);

		return this;
	}

	public SimpleNotifyFrame enableMacOsStyle() {
		jMainPanel.setBackground(new Color(224, 224, 226));
		jMainPanel.setForeground(new Color(64, 69, 73));
		jHeaderLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		jContentLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
		
		disableIcon();
		// frame.pack();
		return this;
	}

}
