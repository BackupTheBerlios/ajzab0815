/*
 * Created on 03.01.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package de.charts.updownchart;

import java.util.ArrayList;



/**
 * @author Zab
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class UpDownData {
	public class ArrayData {
		private long i_up;
		private long i_down;
		private long i_credits;
		private long i_time;

		public ArrayData(long up, long down, long credits, long time) {
			i_up=up;
			i_down=down;
			i_credits=credits;
			i_time=time;
		}
		
		public long getUp() { return i_up; }
		public long getDown() { return i_down; }
		public long getCredits() { return i_credits; }
		public long getTime() { return i_time; }
	} 
	
	private ArrayList Data = new ArrayList();
	private long maxUp=0;
	private long maxDown=0;
	private long maxCred=0;
	
	public UpDownData() {}
	public UpDownData(int size) {
		Data = new ArrayList(size);
	}
	
	public void add(int upRate, int downRate, long time) {
		Data.add(new ArrayData(upRate, downRate, 0, time));
		if (upRate > maxUp) maxUp = upRate;
		if (downRate > maxDown) maxDown = downRate;
	}
	
	public void add(int upRate, int downRate, long credits, long time) {
		Data.add(new ArrayData(upRate, downRate, credits, time));
		if (upRate > maxUp) maxUp = upRate;
		if (downRate > maxDown) maxDown = downRate;
		if (credits > maxCred) maxCred = credits;
	}

	public void add(long upRate, long downRate, long credits, long time) {
		Data.add(new ArrayData(upRate, downRate, credits, time));
		if (upRate > maxUp) maxUp = upRate;
		if (downRate > maxDown) maxDown = downRate;
		if (credits > maxCred) maxCred = credits;
	}
	
	public void clear() {
		Data.clear();
		maxUp = 0;
		maxDown = 0;
		maxCred = 0;
	}
	public long getMaxUp() { return maxUp; }
	public long getMaxDown() { return maxDown; }
	public long getMaxCredits() { return maxCred; }
	
	
	public ArrayData get(int index) {
		return (ArrayData)Data.get( index);
	}
	
	public void remove(int index) {
		Data.remove(index);
	}
	
	public Object[] toArray() {
		return Data.toArray();
	}
	
	public int size() {
		return Data.size();
	}
	
	public UpDownData getInstance() {
		return this;
	} 
}
