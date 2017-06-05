package masinew.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import masinew.bean.Schedule;
import masinew.bean.SynchronizationData;
import masinew.bean.TimeControlling;
import masinew.services.DB2Service;

public class WorkerThread extends Thread implements Runnable {
//	private Connection connect;
	private IntervalWorker intervalWorker = null;
	private ScheduleWorker scheduleWorker = null;
	public WorkerThread(String threadName) {
		this.setName(threadName);
//		this.connect = connect;
	}
	
	public void stopWorking() {
		if (intervalWorker != null) {
			intervalWorker.stopWorking();
		}
		
		if (scheduleWorker != null) {
			scheduleWorker.stopWorking();
		}
	}
	
	
	public void run() {
		ObjectMapper jsonMapper = new ObjectMapper();
		String timeControllingJsonString = getTextFile("timeControlling.json");
		TimeControlling timeControlling = null;
		try {
			timeControlling = jsonMapper.readValue(timeControllingJsonString, TimeControlling.class);
		} catch (Exception e) {
			// TODO: Can not map data in timeControlling.json file to TimeControlling Object
		}
		
		if (timeControlling != null) {
			if (timeControlling.isEnableInterval()) {
				intervalWorker = new IntervalWorker(timeControlling.getIntervalInMilliseconds());
				new Thread(intervalWorker, "IntervalWorker").start();
			}
			
			if (timeControlling.isEnableSchedule()) {
				scheduleWorker = new ScheduleWorker(timeControlling.getSchedules());
				new Thread(scheduleWorker, "ScheduleWorker").start();
			}
		}
	}
	
	private class IntervalWorker implements Runnable {
		private long intervalInMilliseconds;
		private boolean isWorking = true;
		public IntervalWorker(long intervalInMilliseconds) {
			this.intervalInMilliseconds = intervalInMilliseconds;
		}
		
		public void stopWorking() {
			isWorking = false;
		}
		
		public void run() {
			while (isWorking) {
				List<SynchronizationData> synchronizationDataList = DB2Service.getSynchronizationData();
				if (!synchronizationDataList.isEmpty()) {
					DB2Service.doSynchronizationToAnotherServer(synchronizationDataList);
				}
				
				try { Thread.sleep(intervalInMilliseconds); } catch (InterruptedException e) { }
			}
		}
	}
	
	private class ScheduleWorker implements Runnable {
		private List<Schedule> schedules;
		private boolean isWorking = true;
		public ScheduleWorker(List<Schedule> schedules) {
			this.schedules = schedules;
		}
		
		public void stopWorking() {
			isWorking = false;
		}
		
		public void run() {
			while (isWorking) {
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int minute = cal.get(Calendar.MINUTE);
				for (Schedule schedule : schedules) {
					if (hour == schedule.getHour() && minute == schedule.getMinute()) {
						List<SynchronizationData> synchronizationDataList = DB2Service.getSynchronizationData();
						if (!synchronizationDataList.isEmpty()) {
							DB2Service.doSynchronizationToAnotherServer(synchronizationDataList);
						}
					}
				}
				
				try { Thread.sleep(30000); } catch (InterruptedException e) { }
			}
		}
	}
	
	private static String getTextFile(String fileName) {
		try {
			@SuppressWarnings("resource")
			BufferedReader input = new BufferedReader(
						new InputStreamReader(
							new FileInputStream(fileName), "UTF-8"));
			StringBuilder stb = new StringBuilder();
			try {
				String tmpStr;
				while((tmpStr = input.readLine()) != null) {
					stb.append(tmpStr);
				}
			} catch (IOException e1) {
				return null;
			}
			
			return stb.toString();
		} catch (Exception e1) {
			return null;
		}
	}
}
