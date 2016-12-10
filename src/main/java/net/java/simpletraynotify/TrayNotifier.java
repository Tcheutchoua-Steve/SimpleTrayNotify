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
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.Timer;

import com.sun.awt.AWTUtilities;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

/**
 * 
 * @author bettmenn
 */

@SuppressWarnings("restriction")
public class TrayNotifier implements Runnable {
	private static final long serialVersionUID = 1L;
	private SimpleNotifyFrame frame;

	private Integer numPixelsFromScreenVertical = 0; // so the whole time its not
												// completely stuck on the right
												// side of the screen
	private Integer numPixelsFromScreenHorizontal = 0; // so it doesnt stop on the bottom
												// of the screen
	
	
	private Point startPosition;
	private Point endPosition;

	private float startTransparency = 0.0F;
	private float endTransparency = 1.0F;

	private int fps = 25;
	private float fadeInDuration = 0.5F; // seconds
	private float showDuration = 5F; // seocnds to show after fade in
	private float fadeOutDuration = 2F;
	private int numFadeInStepsTotal;
	private int timerDelay;

	private int currentFadeInStep;
	private float currentFraction;
	private int numFadeOutStepsTotal;
	private int currentFadeOutStep;

	private Timer currentTimer;
	private MouseListener mouseListener;
	private boolean transparencySupported = true;
	private ScreenPositionHorizontal screenPositionHorizontal;
	private ScreenPositionVertical screenPositionVertical;
	

	public TrayNotifier(SimpleNotifyFrame frame) {
		this.frame = frame;
	}

	public void setNumPixelsFromScreenHorizontal(int numPixels) {
		this.numPixelsFromScreenHorizontal = numPixels;
		setPositions();
	}

	public void setNumPixelsFromScreenVertical(int numPixels) {
		this.numPixelsFromScreenVertical = numPixels;
		setPositions();
	}

	public void setBaseVertical(ScreenPositionVertical position) {
		this.screenPositionVertical = position;
	}
	
	public void setBaseHorizontal(ScreenPositionHorizontal position) {
		this.screenPositionHorizontal = position;
	}

	public void setStartTransparency(float startTransparency) {
		this.startTransparency = startTransparency;
	}

	public void setEndTransparency(float endTransparency) {
		this.endTransparency = endTransparency;
	}

	public void setFps(int fps) {
		this.fps = fps;
		updateSteps();
	}

	public void setFadeInDuration(float duration) {
		this.fadeInDuration = duration;
		updateSteps();
	}

	public void setFadeOutDuration(float duration) {
		this.fadeOutDuration = duration;
		updateSteps();
	}

	public void setShowDuration(float duration) {
		this.showDuration = duration;
		updateSteps();
	}

	private void updateSteps() {
		numFadeInStepsTotal = (int) (fps * fadeInDuration);
		numFadeOutStepsTotal = (int) (fps * fadeOutDuration);
		timerDelay = 1000 / fps;
	}

	private void setStartPosition() {
		Dimension frameSize = this.frame.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		int x = 0;
		if (this.screenPositionVertical == ScreenPositionVertical.Left) {
			x = numPixelsFromScreenVertical;
		} else if (this.screenPositionVertical == ScreenPositionVertical.Right) {
			x = screenSize.width - numPixelsFromScreenVertical - frameSize.width;
		}
		
		int y = 0;
		if (this.screenPositionHorizontal == ScreenPositionHorizontal.Top) {
			y = 0;
		} else if (this.screenPositionHorizontal == ScreenPositionHorizontal.Bottom) {
			y = screenSize.height;
		}
	
		this.startPosition = new Point(x, y);
	}

	private void setEndPosition() {
		Dimension frameSize = this.frame.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		int x = 0;
		if (this.screenPositionVertical == ScreenPositionVertical.Left) {
			x = numPixelsFromScreenVertical;
		} else if (this.screenPositionVertical == ScreenPositionVertical.Right) {
			x = screenSize.width - numPixelsFromScreenVertical - frameSize.width;
		}

		Rectangle r = getWorkAreaRect();
		
		
		int y = 0;
		if (this.screenPositionHorizontal == ScreenPositionHorizontal.Top) {
			y = numPixelsFromScreenHorizontal;
		} else if (this.screenPositionHorizontal == ScreenPositionHorizontal.Bottom) {
			y = r.height - numPixelsFromScreenHorizontal - frameSize.height;
		}		
		

		Point end = new Point(x, y);

		this.endPosition = end;
	}

	private void setPositions() {
		setStartPosition();
		setEndPosition();
	}

	public void run() {
		setPositions();
		updateSteps();

		fadeIn();
	}

	private MouseListener getMouseListener() {
		if (this.mouseListener == null) {

			this.mouseListener = new MouseListener() {

				public void mouseClicked(MouseEvent arg0) {
				}


				public void mouseEntered(MouseEvent arg0) {
					if (currentTimer != null) {
						currentTimer.stop();
						currentTimer = null;
						setTransparency(1F);
					}
				}


				public void mouseExited(MouseEvent arg0) {
					// start stay/fadeOut phase when user leaves popup
					if (currentTimer == null) {
						currentFadeOutStep = 0;
						stay();
					}

				}


				public void mousePressed(MouseEvent arg0) {
				}


				public void mouseReleased(MouseEvent arg0) {
				}
			};
		}

		return this.mouseListener;
	}

	private void addMouseListener() {

		this.frame.addMouseListener(this.getMouseListener());

	}

	private void fadeIn() {

		initInvisible();
		frame.setAlwaysOnTop(true);

		currentTimer = new Timer(timerDelay, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fadeIn((Timer) event.getSource());
			}
		});

		currentTimer.setRepeats(true);
		currentTimer.start();
	}

	private void stay() {

		addMouseListener();
		currentTimer = new Timer(0, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				stay((Timer) event.getSource());
			}
		});

		currentTimer.setInitialDelay((int) (showDuration * 1000));
		currentTimer.setRepeats(false);
		currentTimer.start();
	}

	private void stay(Timer timer) {

		fadeOut();
	}

	private void fadeOut() {
		currentTimer = new Timer(timerDelay, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fadeOut((Timer) event.getSource());
			}
		});

		currentTimer.setRepeats(true);
		currentTimer.start();
	}

	private void fadeOut(Timer timer) {
		setPositions();
		currentFadeOutStep++;
		currentFraction = (float) (currentFadeOutStep / (float) numFadeOutStepsTotal);

		float trans = getFadeOutTransparencyForCurrentStep();
		setTransparency(trans);

		if (currentFadeOutStep == numFadeOutStepsTotal) {
			timer.stop();
			// start timer for fade out effect
			frame.setVisible(false);
			frame.setAlwaysOnTop(true);
		}
	}

	private void fadeIn(Timer timer) {

		if (currentFadeInStep == 0) {
			frame.pack();
			System.out.println("framesize: " + frame.getSize());
		}

		setPositions();
		currentFadeInStep++;
		currentFraction = (float) (currentFadeInStep / (float) numFadeInStepsTotal);
		float trans = getFadeInTransparencyForCurrentStep();
		Point pos = getPositionForCurrentStep();

		setTransparency(trans);
		frame.setLocation(new Point(pos.x, pos.y));

		if (currentFadeInStep == numFadeInStepsTotal) {

			timer.stop();
			// start timer for fade out effect
			frame.setAlwaysOnTop(true);
			stay();
		}

	}

	private void initInvisible() {
		setTransparency(0F);

		frame.setLocation(new Point(10, 10));
		frame.setVisible(true);
	}

	private float getFadeInTransparencyForCurrentStep() {
		return interpolate(startTransparency, endTransparency, currentFraction);
	}

	private float getFadeOutTransparencyForCurrentStep() {
		return interpolate(endTransparency, startTransparency, currentFraction);
	}

	private float interpolate(float start, float end, float fraction) {
		float diff = end - start;
		float cur = diff * fraction;
		float result = start + cur;
		if (result < 0)
			return 0F;
		else
			return result;
	}

	private Point getPositionForCurrentStep() {
		int x = interpolate(startPosition.x, endPosition.x, currentFraction);
		int y = interpolate(startPosition.y, endPosition.y, currentFraction);

		return new Point(x, y);
	}

	private int interpolate(int start, int end, float fraction) {
		int diff = end - start;
		float cur = diff * fraction;
		int result = start + (int) cur;
		return result;
	}

	private void setTransparency(float f) {
		if (transparencySupported) {
			try {
				AWTUtilities.setWindowOpacity(this.frame, f);
			} catch (UnsupportedOperationException e) {
				transparencySupported = false;
			}	
		}
		
	}

	public interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.loadLibrary("User32", User32.class);
		int SPI_GETWORKAREA = 48;

		int SystemParametersInfoW(int uiAction, int uiParam, RECT pvParam,
				int fWinIni);

	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name");
		// System.out.println("os.name = " + os);
		return os.toLowerCase().contains("windows");
	}

	private static Rectangle getWorkAreaRect() {
		final RECT rect = new RECT();
		int SPI_GETWORKAREA = 48;

		Rectangle result = null;
		if (isWindows()) {
			User32.INSTANCE.SystemParametersInfoW(SPI_GETWORKAREA, 0, rect, 0);
			result = new Rectangle(rect.left, rect.top, rect.right - rect.left,
					rect.bottom - rect.top);
		} else {
			GraphicsConfiguration gc = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice()
					.getDefaultConfiguration();

			Rectangle bounds = gc.getBounds();

			Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

			Rectangle effectiveScreenArea = new Rectangle();

			effectiveScreenArea.x = bounds.x + screenInsets.left;
			effectiveScreenArea.y = bounds.y + screenInsets.top;
			effectiveScreenArea.height = bounds.height - screenInsets.top
					- screenInsets.bottom;
			effectiveScreenArea.width = bounds.width - screenInsets.left
					- screenInsets.right;
			

			result = effectiveScreenArea;
		}

		return result;
	}

	public void enableMacOsStyle() {
		frame.enableMacOsStyle();
	}
}
