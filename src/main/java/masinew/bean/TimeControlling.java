package masinew.bean;

import java.util.List;

public class TimeControlling {
	private boolean enableInterval;
	private long intervalInMilliseconds;
	private boolean enableSchedule;
	private List<Schedule> schedules;
	
	public boolean isEnableInterval() {
		return enableInterval;
	}
	
	public void setEnableInterval(boolean enableInterval) {
		this.enableInterval = enableInterval;
	}
	
	public long getIntervalInMilliseconds() {
		return intervalInMilliseconds;
	}
	
	public void setIntervalInMilliseconds(long intervalInMilliseconds) {
		this.intervalInMilliseconds = intervalInMilliseconds;
	}
	
	public boolean isEnableSchedule() {
		return enableSchedule;
	}
	
	public void setEnableSchedule(boolean enableSchedule) {
		this.enableSchedule = enableSchedule;
	}
	
	public List<Schedule> getSchedules() {
		return schedules;
	}
	
	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}
	
	@Override
	public String toString() {
		return "TimeControlling [enableInterval=" + enableInterval + ", intervalInMilliseconds="
				+ intervalInMilliseconds + ", enableSchedule=" + enableSchedule + ", schedules=" + schedules
				+ "]";
	}
	
}
