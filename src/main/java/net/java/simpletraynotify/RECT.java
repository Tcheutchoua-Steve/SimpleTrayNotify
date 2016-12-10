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

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class RECT extends Structure {

    public int left;
    public int top;
    public int right;
    public int bottom;

    public int Width() {
        return this.right - this.left;
    }

    public int Height() {
        return this.bottom - this.top;
    }

    public void OffsetRect(final int dx, final int dy) {
        this.left += dx;
        this.right += dx;
        this.top += dy;
        this.bottom += dy;
    }

    protected List getFieldOrder() {
      return Arrays.asList(new String[] {"left", "top", "bottom", "right"} );
    }
}