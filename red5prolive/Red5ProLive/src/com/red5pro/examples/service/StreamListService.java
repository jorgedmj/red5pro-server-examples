package com.red5pro.examples.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.scope.IScope;
import org.springframework.core.io.Resource;

public class StreamListService {
	
	private MultiThreadedApplicationAdapter app ;

	public StreamListService( MultiThreadedApplicationAdapter owner){
		app=owner;
	}


	public List<String> getLiveStreams(){		
		System.out.println("get live streams");
		ArrayList<String> ret = new ArrayList<String>();

		Set<String>streamNames=app.getBroadcastStreamNames(app.getScope());
		System.out.println(app.getScope());
		Iterator<String> iterator = streamNames.iterator();

		while(iterator.hasNext()){
			ret.add(iterator.next());
		}

		return ret;
	}
	
	public Map<String, Map<String, Object>> getListOfAvailableFLVs() {
		IScope scope = app.getScope();
		Map<String, Map<String, Object>> filesMap = new HashMap<String, Map<String, Object>>();
		try {

			Resource[] flvs = scope.getResources("streams/*.flv");
			addToMap(filesMap, flvs);

			Resource[] mp3s = scope.getResources("streams/*.mp4");
			addToMap(filesMap, mp3s);


		} catch (IOException e) {
			e.printStackTrace();
		}
		return filesMap;
	}
	
	private String formatDate(Date date) {
		SimpleDateFormat formatter;
		String pattern = "dd/MM/yy H:mm:ss";
		Locale locale = new Locale("en", "US");
		formatter = new SimpleDateFormat(pattern, locale);
		return formatter.format(date);
	}




	private void addToMap(Map<String, Map<String, Object>> filesMap, Resource[] files)
			throws IOException {
		if (files != null) {
			for (Resource flv : files) {
				File file = flv.getFile();
				Date lastModifiedDate = new Date(file.lastModified());
				String lastModified = formatDate(lastModifiedDate);
				String flvName = flv.getFile().getName();
				String flvBytes = Long.toString(file.length());

				Map<String, Object> fileInfo = new HashMap<String, Object>();
				fileInfo.put("name", flvName);
				fileInfo.put("lastModified", lastModified);
				fileInfo.put("size", flvBytes);
				filesMap.put(flvName, fileInfo);
			}
		}
	}






}
