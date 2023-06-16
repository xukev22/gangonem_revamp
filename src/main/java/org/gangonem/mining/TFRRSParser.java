package org.gangonem.mining;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gangonem.model.Competitor;
import org.gangonem.model.Event;
import org.gangonem.model.Meet;
import org.gangonem.utils.GeneralUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TFRRSParser {
	// Return a list of all the meets on TFRRS database from lower page to upper
	// page
	public List<Meet> meetData(int pageLowerBound, int pageUpperBound, String url) throws IOException {
		// create list of stored meetData's
		List<Meet> listOfMeetData = new ArrayList<>();

		// loop through each page from lower bound to upper bound
		for (int pageIndex = pageLowerBound; pageIndex <= pageUpperBound; pageIndex++) {

			// select all the meets on the page
			String pageURL = url + "?page=" + pageIndex;
			Document page = GeneralUtils.fetchDocument(pageURL);
			Elements meets = page.select("table tr");

			// remove the header row
			meets.remove(0);

			// loop through each meet on one page
			for (Element meet : meets) {
				// create meetData instance
				Meet meetData = new Meet();

				// select the td's in each meet element
				Elements columns = meet.select("td");

				// loop through each column for a given meet row
				for (int i = 0; i < columns.size(); i++) {
					// get the value in column i
					Element column = columns.get(i);
					String text = column.text();

					// store in certain field depending on index i
					switch (i) {

					case 0:
						meetData.setDate(text);
						break;
					case 1:
						meetData.setMeet(text);

						// get the event url to request
						String eventURL = column.child(0).attr("abs:href");
						// fill the events field
						meetData.setEvents(eventData(eventURL));

						break;
					case 2:
						meetData.setSport(text);
						break;
					case 3:
						meetData.setStateOrProv(text);
						break;
					}
				}

				// add meetData instance to the list of meetData's
				listOfMeetData.add(meetData);
			}

		}
		return listOfMeetData;
	}

	// Return the list of events in a single meet
	public List<Event> eventData(String url) throws IOException {
		// initialize the list of events
		List<Event> listOfEventData = new ArrayList<>();

		// select the compiled links
		Document page = GeneralUtils.fetchDocument(url);
		Elements compiledEvents = page.select("span.panel-heading-normal-text a");

		// initializing values to help with meet-case determination
		String link1;
		String link2;
		boolean exception1 = false;
		boolean exception2 = false;

		// trying to get the first link
		try {
			link1 = compiledEvents.get(0).attr("abs:href");
		} catch (Exception e) {
			link1 = e.toString();
			exception1 = true;
		}

		// trying to get the second link
		try {
			link2 = compiledEvents.get(1).attr("abs:href");
		} catch (Exception e) {
			link2 = e.toString();
			exception2 = true;
		}

		// case by case handling for certain meet types
		if (url.contains("/results/xc/")) {
			listOfEventData = eventDataXC(page);
		} else if (exception1 || exception2) {
			listOfEventData = eventDataTFexception(link1, link2, exception1, exception2);
		} else {
			listOfEventData = eventDataTF(url, link1, link2);
		}

		return listOfEventData;

	}

	// Return the list of events for a standard TF call
	private List<Event> eventDataTF(String url, String link1, String link2) throws IOException {
		// initialize the list of events
		List<Event> listOfEventData = new ArrayList<>();

		// since its a normal call with no xc or TF exceptions, we can just
		// call the function for both links
		listOfEventData.addAll(eventDataTFMW(link1));
		listOfEventData.addAll(eventDataTFMW(link2));

		return listOfEventData;
	}

	// Return the list of events for a standard TF call, male and female called
	// separately
	private List<Event> eventDataTFMW(String link) throws IOException {

		/*
		 * REFERENCE CASE: duplicate meet upload, bad link...
		 * 
		 * because these meet officials cant upload sometimes smh final String
		 * SOUTH_DAKOTA_M_ERROR =
		 * "https://www.tfrrs.org/results/76859/m/South_Dakota_Kickoff"; final String
		 * SOUTH_DAKOTA_F_ERROR =
		 * "https://www.tfrrs.org/results/76859/f/South_Dakota_Kickoff"; if
		 * (link.equals(SOUTH_DAKOTA_M_ERROR) || link.equals(SOUTH_DAKOTA_F_ERROR)) {
		 * return new ArrayList<EventData>(); }
		 */

		// initializing the list of events
		List<Event> listOfEventData = new ArrayList<>();

		// get the events
		Document page = GeneralUtils.fetchDocument(link);
		Elements events = page.select("div.row > div.col-lg-12");

		// loop through the events on the meet page
		for (Element event : events) {
			// initialize the event instance
			Event eventData = new Event();

			// grab the event titles and smaller not emphasized titles
			String title = event.select("div.custom-table-title > h3, div.custom-table-title > h5").text();
			// CHECK FOR H5 div div maybe HERE

			// set the titles
			eventData.setName(title);

			// set the competitors
			eventData.setCompetitors(competitorData(event));

			// add the event instance to the list of events
			listOfEventData.add(eventData);
		}

		return listOfEventData;
	}

	// Return the list of competitors in a single event
	private List<Competitor> competitorData(Element event) {
		// initialize the list of competitors
		List<Competitor> listOfCompetitorData = new ArrayList<>();

		// select the competitors on a given event
		Elements competitors = event.select("table tr");

		// remove the header row
		competitors.remove(0);

		// loop through the competitors on the event
		for (Element competitor : competitors) {
			Competitor competitorData = new Competitor();
			// select the td's in each competitors element

			// make sure they are a valid competitor
			if (!validCompetitor(competitor)) {
				continue;
			}

			// grab the columns for the competitor
			Elements columns = competitor.select("td");

			// loop through the competitor's columns and assign accordingly
			for (int i = 0; i < columns.size(); i++) {
				// get the value in column i
				Element column = columns.get(i);
				String text = column.text();

				// store depending on index i
				switch (i) {

				case 0:
					competitorData.setField1(text);
					break;
				case 1:
					competitorData.setField2(text);
					break;
				case 2:
					competitorData.setField3(text);
					break;
				case 3:
					competitorData.setField4(text);
					break;
				case 4:
					competitorData.setField5(text);
					break;
				case 5:
					competitorData.setField6(text);
					break;
				case 6:
					competitorData.setField7(text);
					break;
				case 7:
					competitorData.setField8(text);
					break;
				case 8:
					competitorData.setField9(text);
					break;
				case 9:
					competitorData.setField10(text);
					break;
				case 10:
					competitorData.setField11(text);
					break;
				case 11:
					competitorData.setField12(text);
					break;
				}

			}
			listOfCompetitorData.add(competitorData);
		}

		return listOfCompetitorData;
	}

	// returns whether the competitor is a valid competitor
	private boolean validCompetitor(Element competitor) {
		return !competitor.child(0).className().equals("border-right-0");
	}

	// Returns the list of events when there is at least one exception
	private List<Event> eventDataTFexception(String link1, String link2, boolean exception1, boolean exception2)
			throws IOException {
		// if the exception is for link 1, then run it for just link 2
		// if the exception is for link 2, then run it for just link 1
		// if somehow both are exceptions, return null for now
		if (exception1 && !exception2) {
			return eventDataTFMW(link2);
		} else if (exception2 && !exception1) {
			return eventDataTFMW(link1);
		} else {
			return null;
		}
	}

	// Returns the list of events when it is an XC meet
	private List<Event> eventDataXC(Document page) throws IOException {
		// initialize the list of events
		List<Event> listOfEventData = new ArrayList<>();

		// select a xc table event
		Elements events = page.select("div.panel-body > div.row > div.col-lg-12");

		// loop through the events on one page
		for (Element event : events) {
			// initialize a eventData instance
			Event eventData = new Event();

			// get the title of the event
			String title = event.select("div.custom-table-title > h3").text();
			eventData.setName(title);

			// set the competitors field
			eventData.setCompetitors(competitorData(event));

			// add the instance to the list of events
			listOfEventData.add(eventData);
		}
		return listOfEventData;
	}

}
