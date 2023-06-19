package com.nsdl.beckn.lm.registry.utl;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommonUtl {

	public static String parseDomainNamefromUrl(String url) {
//	    if (url == null) return null;
//	    URL rawUrl = null;
//	    try {
//	        rawUrl = new URL(url);
//	    } catch (MalformedURLException e) {
//	        System.out.println("ERROR - failed to create URL from url=" + url);
//	        System.out.println(e.getMessage());
//	        e.printStackTrace();
//	        return url;
//	    }
//	    String host = rawUrl.getHost();
//	    InternetDomainName idn = InternetDomainName.from(host);
//	    while (idn.isTopPrivateDomain() == false && (idn.hasParent()) ) {
//	        idn = idn.parent();
//	    }
//	    String domain = idn.toString();
//	    if (idn.isUnderPublicSuffix()) {
//	        domain =idn.topPrivateDomain().toString();
//	    }
//	    System.out.println("Parsed domain: " + domain);
//	    return domain;
		try {

			URL aURL = new URL(url);
			return aURL.getAuthority();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";

	}

	public static Map<String, Object> mapClone(Map<String, Object> map) {
		Map<String, Object> ret = new HashMap<>(map);
		return ret;
	}

	public static String convertDatetoString(LocalDateTime dt) {
		if (dt == null) {
			return "";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd hh:mm:ss.SSS a", Locale.ENGLISH);
		return formatter.format(dt);
	}

	public static String convertDatetoString(OffsetDateTime dt) {
		if (dt == null) {
			return "";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd hh:mm:ss.SSS a", Locale.ENGLISH);
		return formatter.format(dt);
	}

	public static String convert24DatetoString(OffsetDateTime dt) {
		if (dt == null) {
			return "";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss.SSS", Locale.ENGLISH);
		return formatter.format(dt);
	}
	public static List<OffsetDateTime> convertOffsetToLocalDateTime(List<LocalDateTime> al) {
		List<OffsetDateTime> list = new ArrayList<>();
		al.forEach(item -> {
			list.add(convertOffsetToLocalDateTime(item));
		});
		return list;
	}

	public static OffsetDateTime convertOffsetToLocalDateTime(LocalDateTime dt) {
		return OffsetDateTime.of(dt, ZoneOffset.UTC);

	}

	public static LocalDateTime convertStartStringtoDate(String date) {
		LocalDateTime dt = null;
		if (date == null) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.ENGLISH);
		try {
			dt = LocalDateTime.parse(date + " 00:00", formatter);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return dt;
	}

	public static LocalDateTime convertEndStringtoDate(String date) {
		LocalDateTime dt = null;
		if (date == null) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.ENGLISH);
		try {
			dt = LocalDateTime.parse(date + " 24:00", formatter);

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return dt;
	}

	public static List<LocalDateTime> getLastWeeks(LocalDateTime dt, Integer lastWeekCount) {
		dt = dt.toLocalDate().atStartOfDay();
//		System.out.println(dt);
//		System.out.println("Weeks");
		List<LocalDateTime> al = new ArrayList<>();
		for (int i = 0; i < lastWeekCount; i++) {
			LocalDateTime monday = LocalDateTime.from(dt), sunday = LocalDateTime.from(dt);

			while (!monday.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
				monday = monday.minusDays(1);
			}
			// Go forward to get Sunday

			while (!sunday.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				sunday = sunday.plusDays(1);
			}
			dt = monday.minusDays(7);
			al.add(monday);
			sunday = sunday.plusDays(1);
			al.add(sunday);
			// System.out.println(monday + ":" + sunday);
		}
		return al;
	}

	public static List<LocalDateTime> getLastWeeksWithEndDate(LocalDateTime start, LocalDateTime end) {
		// Long
		// lastWeekCount=Math.round(Period.between(start.toLocalDate(),end.toLocalDate()).getDays()/7.0);

		end = end.toLocalDate().atStartOfDay();
//		System.out.println(end);
//		System.out.println("Weeks");
		List<LocalDateTime> al = new ArrayList<>();

		if (end.isAfter(start)) {
			for (int i = 0; end.isAfter(start); i++) {
				LocalDateTime monday = LocalDateTime.from(end), sunday = LocalDateTime.from(end);

				while (!monday.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
					monday = monday.minusDays(1);
				}
				// Go forward to get Sunday

				while (!sunday.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
					sunday = sunday.plusDays(1);
				}
				end = monday.minusDays(7);
				al.add(monday);
				sunday = sunday.plusDays(1);
				al.add(sunday);
				// System.out.println(monday + ":" + sunday);
			}
		}
		return al;
	}

	public static List<LocalDateTime> getLastDays(LocalDateTime dt, Integer lastDaysCount) {
		dt = dt.toLocalDate().atStartOfDay();
//		System.out.println(dt);
//		System.out.println("Weeks");
		dt.plusDays(1);
		List<LocalDateTime> al = new ArrayList<>();
		for (int i = 0; i < lastDaysCount; i++) {
			LocalDateTime end = LocalDateTime.from(dt);
			dt = dt.minusDays(1);
			LocalDateTime start = LocalDateTime.from(dt);
			al.add(start);

			al.add(end);
			// System.out.println(start + ":" + end);
		}
		return al;
	}

	public static String getDateString(OffsetDateTime date) {
		if ((date == null)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime st;
		try {

			return formatter.format(date);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return null;
	}

	public static List<LocalDateTime> getLastMonths(LocalDateTime dt, Integer lastMonthCount) {
		dt = dt.toLocalDate().atStartOfDay();
		// System.out.println("Months");
		List<LocalDateTime> al = new ArrayList<>();
		for (int i = 0; i < lastMonthCount; i++) {

//			YearMonth month = YearMonth.from(dt);
//			LocalDate start = month.atDay(1);
//			LocalDate end = month.atEndOfMonth();

			LocalDateTime start = dt.with(TemporalAdjusters.firstDayOfMonth());

			LocalDateTime end = dt.with(TemporalAdjusters.lastDayOfMonth());

			dt = dt.minusMonths(1);
			al.add(LocalDateTime.from(start));
			end = end.plusDays(1);
			al.add(LocalDateTime.from(end));

			// System.out.println(start + ":" + end);
		}
		return al;
	}

	public static List<LocalDateTime> getLastMonthsWithEndDate(LocalDateTime start, LocalDateTime end) {
		end = end.toLocalDate().atStartOfDay();
		// System.out.println("Months");
		List<LocalDateTime> al = new ArrayList<>();
		for (int i = 0; end.isAfter(start); i++) {

			LocalDateTime startMonth = end.with(TemporalAdjusters.firstDayOfMonth());

			LocalDateTime endMonth = end.with(TemporalAdjusters.lastDayOfMonth());

			end = end.minusMonths(1);
			al.add(LocalDateTime.from(startMonth));
			end = end.plusDays(1);
			al.add(LocalDateTime.from(endMonth));

			// System.out.println(start + ":" + end);
		}
		return al;
	}

	public static List<LocalDateTime> getLastYears(LocalDateTime dt, Integer lastYearCount) {
		dt = dt.toLocalDate().atStartOfDay();
//		System.out.println(dt);
//		System.out.println("Years");
		List<LocalDateTime> al = new ArrayList<>();
		for (int i = 0; i < lastYearCount; i++) {

			LocalDateTime first = dt.with(TemporalAdjusters.firstDayOfYear());

			LocalDateTime last = dt.with(TemporalAdjusters.lastDayOfYear());
			dt = dt.minusYears(1);
			last = last.plusDays(1);
			// System.out.println(first + ":" + last);
			al.add(first);
			al.add(last);
		}
		return al;
	}

	public static List<LocalDateTime> getLastYearsWithEndDated(LocalDateTime start, LocalDateTime end) {
		end = end.toLocalDate().atStartOfDay();
//		System.out.println(end);
//		System.out.println("Years");
		List<LocalDateTime> al = new ArrayList<>();
		for (int i = 0; end.isAfter(start); i++) {

			LocalDateTime first = end.with(TemporalAdjusters.firstDayOfYear());

			LocalDateTime last = end.with(TemporalAdjusters.lastDayOfYear());
			end = end.minusYears(1);
			last = last.plusDays(1);
			// System.out.println(first + ":" + last);
			al.add(first);
			al.add(last);
		}
		return al;
	}

	public static LocalDateTime setSatrtDate(LocalDateTime start, String select, String type) {
		if ("auto".equals(select)) {
			if (Constants.dash_type_WEEKS.equals(type)) {
				while (!start.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
					start = start.minusDays(1);
				}
				return start;
				// return start.with(TemporalAdjusters.firstDayOfYear());
			} else if (Constants.dash_type_MONTHS.equals(type)) {

				return start.with(TemporalAdjusters.firstDayOfMonth());
			} else if (Constants.dash_type_YEARS.equals(type)) {
				return start.with(TemporalAdjusters.firstDayOfYear());
			} else { // days
				return start;
			}
		} else {
			return start;
		}
	}
}
