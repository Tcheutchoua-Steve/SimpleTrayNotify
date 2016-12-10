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
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class NotifyExample {

	public static void main(String[] args) {
		SimpleNotifyFrame frame = (new SimpleNotifyFrame())	// all SimpleNotifyFrame setters supports chaining
				.enableHeader("This is the header!")		// set the headers string
				.enableContent("This is some content text. Its a bit longer than the header, but this does not really matter! This is some content text. Its a bit longer than the header, but this does not really matter! This is some content text. Its a bit longer than the header, but this does not really matter! aaaaaaaaaaaaaaaaaa") // set the content string
				.enableIcon(MessageType.WARNING)			// uses the WARNING, ERROR, INFO and NONE enum fron TrayIcon
				.enableDraggable()							// SimpleNotifyFrame is by default undecorated and has a final, unmovable position. enableDraggable() will allow it to be dragged after the animation has been finished.
				.enableMacOsStyle()
				// add your own button and optional ActionListener
				.enableActionButton("Click here", new ActionListener() {


					public void actionPerformed(ActionEvent arg0) {
						JOptionPane.showMessageDialog(null, "Test Message");

					}

				})
				
				;

		TrayNotifier tn = new TrayNotifier(frame);
		tn.setNumPixelsFromScreenHorizontal(150);
		tn.setNumPixelsFromScreenVertical(150);
		tn.setBaseHorizontal(ScreenPositionHorizontal.Top);
		tn.setBaseVertical(ScreenPositionVertical.Left);
		tn.run();
	}

}
