package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONException;

import constants.MinMaxOdds;
import entries.AsianEntry;
import entries.FinalEntry;
import results.Results;
import runner.Runner;
import runner.RunnerAggregateInterval;
import runner.RunnerAllLines;
import runner.RunnerAsian;
import runner.RunnerAsianFinals;
import runner.RunnerDraws;
import runner.RunnerFinals;
import runner.RunnerIntersect;
import runner.RunnerOptimals;
import settings.Settings;
import settings.SettingsAsian;
import utils.Utils;
import xls.AsianUtils;
import xls.XlSUtils;

/**
 * PJDCC - Summary for class responsabilities.
 *
 * @author fourplus <fourplus1718@gmail.com>
 * @since 1.0
 * @version 11 Changes done
 */
public class Test {

	public static void main(String[] args) throws JSONException, IOException, InterruptedException, ExecutionException {
		long start = System.currentTimeMillis();

		// Results.eval("estimateBoth");
		// Results.eval("smooth");
		// Results.eval("test");

		// asianPredictions();

		// float total = 0f;
		// for (int year = 2005; year <= 2015; year++)
		// total += asian(year);
		// System.out.println("Avg profit is " + (total / 11));

		// System.out.println(Utils.pValueCalculator(11880, 0.04f, 1.8f));
		// makePredictions();

		// float total = 0f;
		// int startY = 2011;
		// int end = 2016;
		// for (int year = startY; year <= end; year++)
		// total += simulationAllLines(year, true /*DataType.ALLEURODATA*/);
		// System.out.println("Avg profit is " + (total / (end - startY + 1)));

		// for (int i = 2005; i <= 2015; i++)
		// XlSUtils.populateScores(i);

		// accumulators(2015, 2015);

		// analysis(2005, 2016, DataType.ALLEURODATA);

		// aggregateInterval();

		// stats();

		// Utils.optimalHTSettings(2005, 2016, DataType.ALLEURODATA,
		// MaximizingBy.BOTH);

		// Utils.fastSearch(2005, 2016, DataType.ALLEURODATA,
		// MaximizingBy.BOTH);

		System.out.println((System.currentTimeMillis() - start) / 1000d + "sec");

	}

	private static void analysis(int start, int end, DataType type)
			throws InterruptedException, ExecutionException, IOException {
		ArrayList<FinalEntry> all = new ArrayList<>();
		HashMap<String, HashMap<Integer, ArrayList<FinalEntry>>> byLeagueYear = new HashMap<>();

		// populateForAnalysis(start, end, all, byLeagueYear, type);
		populateForAnalysisFromDB(start, end, all, byLeagueYear, type, "shots");

		// ArrayList<FinalEntry> ita = (ArrayList<FinalEntry>)
		// byLeagueYear.get("I1").values().stream()
		// .flatMap(List::stream).collect(Collectors.toList());

		// HashMap<String, ArrayList<FinalEntry>> byLeague =
		// Utils.byLeague(all);
		// for (java.util.Map.Entry<String, ArrayList<FinalEntry>> i :
		// byLeague.entrySet()) {
		// System.out.println(i.getKey());
		// System.out.println(i.getValue().size());
		// Utils.analysys(i.getValue(), 3000, false);
		// }

		// ArrayList<FinalEntry> withTH1 = Utils.withBestThreshold(byLeagueYear,
		// 1, MaximizingBy.OVERS);
		// ArrayList<FinalEntry> withTH2 = Utils.withBestThreshold(byLeagueYear,
		// 2, MaximizingBy.OVERS);
		// ArrayList<FinalEntry> withTH3 = Utils.withBestThreshold(byLeagueYear,
		// 3, MaximizingBy.OVERS);
		// ArrayList<FinalEntry> withTH4 = Utils.withBestThreshold(byLeagueYear,
		// 4, MaximizingBy.OVERS);

		// ArrayList<FinalEntry> withTH5 = Utils.withBestThreshold(byLeagueYear,
		// 1, MaximizingBy.BOTH);
		// ArrayList<FinalEntry> withTH6 = Utils.withBestThreshold(byLeagueYear,
		// 2, MaximizingBy.BOTH);
		// ArrayList<FinalEntry> withTH7 = Utils.withBestThreshold(byLeagueYear,
		// 3, MaximizingBy.BOTH);
		// ArrayList<FinalEntry> withTH8 = Utils.withBestThreshold(byLeagueYear,
		// 4, MaximizingBy.BOTH);

		// ArrayList<FinalEntry> withTH9 = Utils.withBestThreshold(byLeagueYear,
		// 1, MaximizingBy.UNDERS);
		// ArrayList<FinalEntry> withTH10 =
		// Utils.withBestThreshold(byLeagueYear, 2, MaximizingBy.UNDERS);
		// ArrayList<FinalEntry> withTH11 =
		// Utils.withBestThreshold(byLeagueYear, 3, MaximizingBy.UNDERS);
		// ArrayList<FinalEntry> withTH12 =
		// Utils.withBestThreshold(byLeagueYear, 4, MaximizingBy.UNDERS);

		Utils.fullAnalysys(all, "all");

		// Utils.fullAnalysys(withTH1, "maxByThOvers(1)");

		// ArrayList<FinalEntry> restricted =
		// Utils.filterByOdds(Utils.cotRestrict(Utils.onlyUnders(all), 0.175f),
		// 1f,
		// 2.2f);
		//
		// all = Utils.filterByOdds(Utils.onlyUnders(Utils.noequilibriums(all)),
		// 1.55f, 1.87f);
		// System.out.println("from " + all.size());
		// System.out.println(Utils.getNormalizedProfit(all));
		// System.out.println(Utils.getNormalizedYield(all));
		// System.out.println("1 in " + Utils.evaluateRecordNormalized(all));
		// LineChart.draw(Utils.createProfitMovementData(all), 3000);

		// ArrayList<FinalEntry> withTH1 = Utils.withBestSettings(byLeagueYear,
		// 4);

	}
	
	private static ArrayList<FinalEntry> createFinalEntry(){
		return new ArrayList<FinalEntry>();
	}

	private static void populateForAnalysisFromDB(int start, int end, ArrayList<FinalEntry> all,
			HashMap<String, HashMap<Integer, ArrayList<FinalEntry>>> byLeagueYear, DataType type, String description)
					throws InterruptedException {
		for (int i = start; i <= end; i++) {
			ArrayList<FinalEntry> finals = createFinalEntry();
			for (String comp : Arrays.asList(MinMaxOdds.SHOTS)) {
				finals.addAll(SQLiteJDBC.selectFinals(comp, i, description));
			}

			HashMap<String, ArrayList<FinalEntry>> byLeague = Utils.byLeague(finals);
			for (java.util.Map.Entry<String, ArrayList<FinalEntry>> league : byLeague.entrySet()) {
				if (!byLeagueYear.containsKey(league.getKey()))
					byLeagueYear.put(league.getKey(), new HashMap<>());

				byLeagueYear.get(league.getKey()).put(i, league.getValue());

			}

			all.addAll(finals);
		}

	}

	private static void populateForAnalysis(int start, int end, ArrayList<FinalEntry> all,
			HashMap<String, HashMap<Integer, ArrayList<FinalEntry>>> byLeagueYear, DataType type)
					throws InterruptedException, ExecutionException, IOException {
		for (int i = start; i <= end; i++) {
			ArrayList<FinalEntry> finals = finals(i, type);
			HashMap<String, ArrayList<FinalEntry>> byLeague = Utils.byLeague(finals);
			for (java.util.Map.Entry<String, ArrayList<FinalEntry>> league : byLeague.entrySet()) {
				if (!byLeagueYear.containsKey(league.getKey()))
					byLeagueYear.put(league.getKey(), new HashMap<>());

				byLeagueYear.get(league.getKey()).put(i, league.getValue());

			}

			all.addAll(finals);
		}
	}

	public static float simulationAllLines(int year, boolean parsedLeagues)
			throws InterruptedException, ExecutionException, IOException {
		String base = new File("").getAbsolutePath();
		// ArrayList<String> dont = new
		// ArrayList<String>(Arrays.asList(MinMaxOdds.DONT));
		try {
			FileInputStream file;
			if (!parsedLeagues)
				file = new FileInputStream(new File(base + "\\data\\all-euro-data-" + year + "-" + (year + 1) + ".xls"));
			else
				file = new FileInputStream(new File(base + "\\data\\fullodds" + year + ".xls"));
	
			HSSFWorkbook workbook = new HSSFWorkbook(file);
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (file != null) {
				try {
					file.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}
		Iterator<Sheet> sheet = workbook.sheetIterator();
		float totalProfit = 0.0f;

		ExecutorService pool = Executors.newFixedThreadPool(3);
		ArrayList<Future<Float>> threadArray = new ArrayList<Future<Float>>();
		while (sheet.hasNext()) {
			HSSFSheet sh = (HSSFSheet) sheet.next();
			// if (!sh.getSheetName().equals("IT"))
			// continue;
			// if (!Arrays.asList(MinMaxOdds.FULL).contains(sh.getSheetName()))
			// continue;

			// if
			// (!Arrays.asList(MinMaxOdds.MANUAL).contains(sh.getSheetName()))
			// continue;

			threadArray.add(pool.submit(new RunnerAllLines(sh, year)));
		}

		for (Future<Float> fd : threadArray) {
			totalProfit += fd.get();
			// System.out.println("Total profit: " + String.format("%.2f",
			// totalProfit));
		}
		System.out.println("Total profit for season " + year + " is " + String.format("%.2f", totalProfit));
		workbook.close();
		pool.shutdown();
		return totalProfit;
	}

	public static float asian(int year, boolean parsedLeagues)
			throws IOException, InterruptedException, ExecutionException {
		String base = new File("").getAbsolutePath();
		try {
			FileInputStream file;
			if (!parsedLeagues)
				file = new FileInputStream(new File(base + "\\data\\all-euro-data-" + year + "-" + (year + 1) + ".xls"));
			else
				file = new FileInputStream(new File(base + "\\data\\odds" + year + ".xls"));
			HSSFWorkbook workbook = new HSSFWorkbook(file);
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (file != null) {
				try {
					file.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}
		Iterator<Sheet> sheet = workbook.sheetIterator();
		float totalProfit = 0.0f;

		ExecutorService pool = Executors.newFixedThreadPool(3);
		ArrayList<Future<Float>> threadArray = new ArrayList<Future<Float>>();
		while (sheet.hasNext()) {
			HSSFSheet sh = (HSSFSheet) sheet.next();
			if (!sh.getSheetName().equals("SPA2"))
				continue;
			// if (Arrays.asList(MinMaxOdds.SHOTS).contains(sh.getSheetName()))
			// continue;

			threadArray.add(pool.submit(new RunnerAsian(sh, year)));
		}

		for (Future<Float> fd : threadArray) {
			totalProfit += fd.get();
			// System.out.println("Total profit: " + String.format("%.2f",
			// totalProfit));
		}
		System.out.println("Total profit for season " + year + " is " + String.format("%.2f", totalProfit));
		workbook.close();
		pool.shutdown();
		return totalProfit;
	}

	public static float draws(int year, boolean b) throws IOException, InterruptedException, ExecutionException {
		String base = new File("").getAbsolutePath();
		try {
			FileInputStream file;
			if (!b)
				file = new FileInputStream(new File(base + "\\data\\all-euro-data-" + year + "-" + (year + 1) + ".xls"));
			else
				file = new FileInputStream(new File(base + "\\data\\odds" + year + ".xls"));
			HSSFWorkbook workbook = new HSSFWorkbook(file);
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (file != null) {
				try {
					file.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}
		Iterator<Sheet> sheet = workbook.sheetIterator();
		float totalProfit = 0.0f;

		ExecutorService pool = Executors.newFixedThreadPool(3);
		ArrayList<Future<Float>> threadArray = new ArrayList<Future<Float>>();
		while (sheet.hasNext()) {
			HSSFSheet sh = (HSSFSheet) sheet.next();
			if (!sh.getSheetName().equals("BRA"))
				continue;
			// if (!Arrays.asList(MinMaxOdds.SHOTS).contains(sh.getSheetName()))
			// continue;

			threadArray.add(pool.submit(new RunnerDraws(sh, year)));
		}

		for (Future<Float> fd : threadArray) {
			totalProfit += fd.get();
			// System.out.println("Total profit: " + String.format("%.2f",
			// totalProfit));
		}
		System.out.println("Total profit for season " + year + " is " + String.format("%.2f", totalProfit));
		workbook.close();
		pool.shutdown();
		return totalProfit;
	}

	public static float asianFinals(int year) throws IOException, InterruptedException, ExecutionException {
		String base = new File("").getAbsolutePath();
		
		FileInputStream file;
		try {
			file = new FileInputStream(
					new File(base + "\\data\\all-euro-data-" + year + "-" + (year + 1) + ".xls"));
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (file != null) {
				try {
					file.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		Iterator<Sheet> sheet = workbook.sheetIterator();
		ArrayList<AsianEntry> all = new ArrayList<>();
		float totalProfit = 0.0f;

		ExecutorService pool = Executors.newFixedThreadPool(3);
		ArrayList<Future<ArrayList<AsianEntry>>> threadArray = new ArrayList<Future<ArrayList<AsianEntry>>>();
		while (sheet.hasNext()) {
			HSSFSheet sh = (HSSFSheet) sheet.next();
			// if (!sh.getSheetName().equals("G1"))
			// continue;
			// if(!Arrays.asList(MinMaxOdds.SHOTS).contains(sh.getSheetName()))
			// continue;

			threadArray.add(pool.submit(new RunnerAsianFinals(sh, year)));
		}

		for (Future<ArrayList<AsianEntry>> fd : threadArray) {
			// totalProfit += fd.get();
			all.addAll(fd.get());
			// System.out.println("Total profit: " + String.format("%.2f",
			// totalProfit));
		}

		AsianUtils.analysis(all);

		// System.out.println("Total profit for season " + year + " is " +
		// String.format("%.2f", totalProfit));
		workbook.close();
		pool.shutdown();
		return totalProfit;
	}
	
	private static String createString(){
		return new File("").getAbsolutePath();
	}
	
	private static FileInputStream createFileInputStream(String base,int year){
		return new FileInputStream(new File(base + "\\data\\all-euro-data-" + year + "-" + (year + 1) + ".xls"));
	}

	public static final void singleMethod() throws IOException, ParseException {

		float totalTotal = 0f;
		for (int year = 2005; year <= 2015; year++) {
			float total = 0f;
			String base = createString();
			FileInputStream file;
			try {
				file = createFileInputStream(base,year);
			} catch (Exception e) {
				System.out.println("Something was wrong");
			} finally {
				if (file != null) {
					try {
						file.close (); // OK
					} catch (java.io.IOException e3) {
						System.out.println("I/O Exception");
	               }
				}
			}
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			Iterator<Sheet> sheet = workbook.sheetIterator();
			while (sheet.hasNext()) {
				HSSFSheet sh = (HSSFSheet) sheet.next();
				if (!Arrays.asList(MinMaxOdds.SHOTS).contains(sh.getSheetName()))
					continue;
				float profit = XlSUtils.singleMethod(sh, XlSUtils.selectAll(sh, 10), year);
				// System.out.println(sh.getSheetName() + " " + year + " " +
				// profit);
				total += profit;
			}
			System.out.println("Total for " + year + ": " + total);
			workbook.close();
			totalTotal += total;
		}
		System.out.println("Avg is: " + totalTotal / 11);
	}

	public static void stored24() throws InterruptedException {
		int bestPeriod = 0;
		float bestProfit = Float.NEGATIVE_INFINITY;
		int period = 3;
		float total = 0f;
		int sizeTotal = 0;
		float totalStake = 0f;

		ArrayList<FinalEntry> all = new ArrayList<>();

		for (int i = 2005 + period; i <= 2014; i++) {
			float curr = 0f;
			int size = 0;
			float staked = 0f;
			for (String league : Results.LEAGUES) {
				if (!Arrays.asList(MinMaxOdds.DONT).contains(league)) {
					ArrayList<FinalEntry> list = XlSUtils.bestCot(league, i, period, "realdouble15");
					// System.out.println("Profit for: " + league + " last: " +
					// i + " is: " + Results.format(pr));

					curr += Utils.getScaledProfit(list, 0f)[0];
					size += list.size();
					staked += Utils.getScaledProfit(list, 0f)[1];
					all.addAll(list);
				}
			}

			System.out.println(
					"For " + i + ": " + curr + "  yield: " + Results.format((curr / staked) * 100) + " from: " + size);
			total += curr;
			sizeTotal += size;
			totalStake += staked;

			if (curr > bestProfit) {
				bestProfit = curr;
				bestPeriod = i;
			}

		}

		System.out.println(
				"Total avg: " + total / (10 - period) + " avg yield: " + Results.format(100 * (total / totalStake)));
				// Utils.drawAnalysis(all);

		// System.out.println("Best period: " + bestPeriod + " with profit: " +
		// bestProfit);
	}

	public static final void aggregateInterval() throws IOException, InterruptedException, ExecutionException {
		ArrayList<String> dont = new ArrayList<String>(Arrays.asList(MinMaxOdds.DONT));
		String base = new File("").getAbsolutePath();
		FileInputStream file;
		try {
			file = new FileInputStream(new File(base + "\\data\\all-euro-data-" + 2014 + "-" + 2015 + ".xls"));
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (file != null) {
				try {
					file.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}

		ExecutorService pool = Executors.newFixedThreadPool(7);
		ArrayList<Future<Settings>> threadArray = new ArrayList<Future<Settings>>();
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		Iterator<Sheet> sheet = workbook.sheetIterator();
		while (sheet.hasNext()) {
			HSSFSheet sh = (HSSFSheet) sheet.next();
			if (dont.contains(sh.getSheetName()))
				continue;
			threadArray.add(pool.submit(new RunnerAggregateInterval(2005, 2007, sh)));
		}

		for (Future<Settings> fd : threadArray)
			fd.get();

		workbook.close();
		pool.shutdown();
	}

	public static final void aggregate(int year, int n) throws IOException, InterruptedException, ExecutionException {
		ArrayList<String> dont = new ArrayList<String>(Arrays.asList(MinMaxOdds.DONT));
		String base = new File("").getAbsolutePath();
		FileInputStream file;
		try {
			file = new FileInputStream(
					new File(base + "\\data\\all-euro-data-" + year + "-" + (year + 1) + ".xls"));
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (file != null) {
				try {
					file.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}

		ExecutorService pool = Executors.newFixedThreadPool(3);
		ArrayList<Future<Settings>> threadArray = new ArrayList<Future<Settings>>();
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		Iterator<Sheet> sheet = workbook.sheetIterator();
		while (sheet.hasNext()) {
			HSSFSheet sh = (HSSFSheet) sheet.next();
			// if (dont.contains(sh.getSheetName()))
			// continue;
			// if (sh.getSheetName().equals("D1"))
			threadArray.add(pool.submit(new RunnerAggregateInterval(year - n, year - 
					1, sh)));
		}

		HashMap<String, Settings> optimals = new HashMap<>();

		for (Future<Settings> fd : threadArray) {
			Settings result = fd.get();
			optimals.put(result.league, result);
			SQLiteJDBC.storeSettings(result, year, n);
		}

		// // TESTING
		// float totalProfit = 0f;
		// Iterator<Sheet> sheets = workbook.sheetIterator();
		// while (sheets.hasNext()) {
		// HSSFSheet sh = (HSSFSheet) sheets.next();
		// // if (sh.getSheetName().equals("D1")) {
		// ArrayList<FinalEntry> list = XlSUtils.runWithSettingsList(sh,
		// XlSUtils.selectAll(sh),
		// optimals.get(sh.getSheetName()));
		// float profit = Utils.getProfit(list,
		// optimals.get(sh.getSheetName()));
		// totalProfit += profit;
		// System.out.println(sh.getSheetName() + ": " + profit);
		// // }
		// }

		// System.out.println("Total for " + year + " : " + totalProfit);

		workbook.close();
		pool.shutdown();
	}

	public static void stats() throws IOException, ParseException {
		for (int year = 2005; year <= 2015; year++) {
			String base = createString();

			FileInputStream file;
			try {
				file = createFileInputStream(base, year);
			} catch (Exception e) {
				System.out.println("Something was wrong");
			} finally {
				if (file != null) {
					try {
						file.close (); // OK
					} catch (java.io.IOException e3) {
						System.out.println("I/O Exception");
	               }
				}
			}
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheet("E0");
			ArrayList<ExtendedFixture> all = XlSUtils.selectAllAll(sheet);
			System.out.println(year + " over: " + Utils.countOverGamesPercent(all) + "% AVG: " + Utils.findAvg(all));
			System.out.println("Overs when draw: " + Utils.countOversWhenDraw(all));
			System.out.println("Overs when win/loss: " + Utils.countOversWhenNotDraw(all));
			Utils.byWeekDay(all);
			System.out.println();
			workbook.close();
		}
	}

	public static float simulation(int year, DataType alleurodata)
			throws InterruptedException, ExecutionException, IOException {
		String base = new File("").getAbsolutePath();
		// ArrayList<String> dont = new
		// ArrayList<String>(Arrays.asList(MinMaxOdds.DONT));
		try {
			FileInputStream file;
			if (alleurodata.equals(DataType.ALLEURODATA))
				file = new FileInputStream(new File(base + "\\data\\all-euro-data-" + year + "-" + (year + 1) + ".xls"));
			else
				file = new FileInputStream(new File(base + "\\data\\odds" + year + ".xls"));
	
			HSSFWorkbook workbook = new HSSFWorkbook(file);
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (file != null) {
				try {
					file.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}
		Iterator<Sheet> sheet = workbook.sheetIterator();
		float totalProfit = 0.0f;

		ExecutorService pool = Executors.newFixedThreadPool(3);
		ArrayList<Future<Float>> threadArray = new ArrayList<Future<Float>>();
		while (sheet.hasNext()) {
			HSSFSheet sh = (HSSFSheet) sheet.next();
			// if (!sh.getSheetName().equals("E0"))
			// continue;
			if (!Arrays.asList(MinMaxOdds.SHOTS).contains(sh.getSheetName()))
				continue;

			// if (!Arrays.asList(MinMaxOdds.PFS).contains(sh.getSheetName()))
			// continue;
			// if
			// (sh.getSheetName().equals("D1")||sh.getSheetName().equals("SP1"))
			// continue;

			threadArray.add(pool.submit(new Runner(sh, year)));
		}

		for (Future<Float> fd : threadArray) {
			totalProfit += fd.get();
			// System.out.println("Total profit: " + String.format("%.2f",
			// totalProfit));
		}
		System.out.println("Total profit for season " + year + " is " + String.format("%.2f", totalProfit));
		workbook.close();
		pool.shutdown();
		return totalProfit;
	}

	public static ArrayList<FinalEntry> finals(int year, DataType type)
			throws InterruptedException, ExecutionException, IOException {
		String base = new File("").getAbsolutePath();
		ArrayList<String> dont = new ArrayList<String>(Arrays.asList(MinMaxOdds.DONT));
		ArrayList<String> draw = new ArrayList<String>(Arrays.asList(MinMaxOdds.DRAW));
		
		try {
			FileInputStream file;
			if (type.equals(DataType.ALLEURODATA))
				file = new FileInputStream(new File(base + "\\data\\all-euro-data-" + year + "-" + (year + 1) + ".xls"));
			else
				file = new FileInputStream(new File(base + "\\data\\odds" + year + ".xls"));
			HSSFWorkbook workbook = new HSSFWorkbook(file);
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (file != null) {
				try {
					file.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}
		Iterator<Sheet> sheet = workbook.sheetIterator();
		ArrayList<FinalEntry> all = new ArrayList<>();

		ExecutorService pool = Executors.newFixedThreadPool(3);
		ArrayList<Future<ArrayList<FinalEntry>>> threadArray = new ArrayList<Future<ArrayList<FinalEntry>>>();
		while (sheet.hasNext()) {
			HSSFSheet sh = (HSSFSheet) sheet.next();
			// if (!Arrays.asList(MinMaxOdds.SHOTS).contains(sh.getSheetName()))
			// continue;
			// if
			// (!Arrays.asList(MinMaxOdds.MANUAL).contains(sh.getSheetName()))
			// continue;

			// if (!sh.getSheetName().equals("I1"))
			// continue;
			threadArray.add(pool.submit(new RunnerFinals(sh, year)));
		}

		for (Future<ArrayList<FinalEntry>> fd : threadArray) {
			all.addAll(fd.get());
		}

		workbook.close();
		pool.shutdown();

		// Utils.predictionCorrelation(all);

		// Utils.analysys(all, year);
		// Utils.drawAnalysis(all);
		// ArrayList<FinalEntry> overs = Utils.onlyOvers(all);
		// Utils.analysys(overs, year);
		// Utils.hyperReal(overs, year, 1000f, 0.05f);
		// Utils.evaluateRecord(all);
		// LineChart.draw(Utils.createProfitMovementData(all), year);

		float[] profits = new float[8];
		// System.out.println(year);
		// for (int i = 3; i <= 10; i++)
		// profits[i - 3] = Utils.bestNperWeek(all, i);

		return all;

	}

	public static float simulationIntersect(int year) throws InterruptedException, ExecutionException, IOException {
		String base = new File("").getAbsolutePath();
		// ArrayList<String> dont = new
		// ArrayList<String>(Arrays.asList(MinMaxOdds.DONT));

		FileInputStream file;
		try {
			file = new FileInputStream(
					new File(base + "\\data\\all-euro-data-" + year + "-" + (year + 1) + ".xls"));
			HSSFWorkbook workbook = new HSSFWorkbook(file);
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (file != null) {
				try {
					file.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}
		
		Iterator<Sheet> sheet = workbook.sheetIterator();
		float totalProfit = 0.0f;

		ExecutorService pool = Executors.newFixedThreadPool(3);
		ArrayList<Future<Float>> threadArray = new ArrayList<Future<Float>>();
		while (sheet.hasNext()) {
			HSSFSheet sh = (HSSFSheet) sheet.next();
			// if (dont.contains(sh.getSheetName()))
			// continue;
			threadArray.add(pool.submit(new RunnerIntersect(sh, year)));
		}

		for (Future<Float> fd : threadArray) {
			totalProfit += fd.get();
			// System.out.println("Total profit: " + String.format("%.2f",
			// totalProfit));
		}
		System.out.println("Total profit for season " + year + " is " + String.format("%.2f", totalProfit));
		workbook.close();
		pool.shutdown();
		return totalProfit;
	}
	
	private static ArrayList<Future<Float>> createFutureFloat(){
		new ArrayList<Future<Float>>();
	}

	public static void optimals() throws IOException, InterruptedException, ExecutionException {
		String basePath = new File("").getAbsolutePath();
		float totalTotal = 0f;

		for (int year = 2015; year <= 2015; year++) {
			float total = 0f;
			ExecutorService pool = Executors.newFixedThreadPool(1);
			ArrayList<Future<Float>> threadArray = createFutureFloat();
			FileInputStream filedata;
			try {
				filedata = createFileInputStream(basePath, year);
				HSSFWorkbook workbookdata = new HSSFWorkbook(filedata);
			} catch (Exception e) {
				System.out.println("Something was wrong");
			} finally {
				if (filedata != null) {
					try {
						filedata.close (); // OK
					} catch (java.io.IOException e3) {
						System.out.println("I/O Exception");
	               }
				}
			}
			
			Iterator<Sheet> sh = workbookdata.sheetIterator();
			while (sh.hasNext()) {
				HSSFSheet i = (HSSFSheet) sh.next();
				// if (i.getSheetName().equals("SP2"))
				threadArray.add(pool.submit(new RunnerOptimals(i, year)));
			}

			for (Future<Float> fd : threadArray) {
				total += fd.get();
			}

			System.out.println("Total profit for " + year + " is: " + total);

			totalTotal += total;
			workbookdata.close();
			pool.shutdown();
		}
		System.out.println("Average is:" + totalTotal / 11);
	}

	public static void optimalsbyCompetition() throws IOException, ParseException {

		HashMap<String, ArrayList<Settings>> optimals = new HashMap<>();
		String basePath = new File("").getAbsolutePath();

		for (int year = 2015; year <= 2015; year++) {
			FileInputStream filedata;
			try {
				filedata = createFileInputStream(basePath, year);
				HSSFWorkbook workbookdata = new HSSFWorkbook(filedata);
			} catch (Exception e) {
				System.out.println("Something was wrong");
			} finally {
				if (filedata != null) {
					try {
						filedata.close (); // OK
					} catch (java.io.IOException e3) {
						System.out.println("I/O Exception");
	               }
				}
			}

			Iterator<Sheet> sh = workbookdata.sheetIterator();
			while (sh.hasNext()) {
				HSSFSheet i = (HSSFSheet) sh.next();
				Settings set = XlSUtils.predictionSettings(i, year);
				if (optimals.get(i.getSheetName()) != null)
					optimals.get(i.getSheetName()).add(set);
				else {
					optimals.put(i.getSheetName(), new ArrayList<>());
					optimals.get(i.getSheetName()).add(set);
				}
			}

			workbookdata.close();
		}
		
		float totalPeriod = 0f;

		ArrayList<String> dont = new ArrayList<String>(Arrays.asList(MinMaxOdds.DONT));

		for (int year = 2006; year <= 2015; year++) {
			float total = 0f;
			FileInputStream filedata;
			try {
				filedata = createFileInputStream(basePath, year);
				HSSFWorkbook workbookdata = new HSSFWorkbook(filedata);
			} catch (Exception e) {
				System.out.println("Something was wrong");
			} finally {
				if (filedata != null) {
					try {
						filedata.close (); // OK
					} catch (java.io.IOException e3) {
						System.out.println("I/O Exception");
	               }
				}
			}

			Iterator<Sheet> sh = workbookdata.sheetIterator();
			while (sh.hasNext()) {
				HSSFSheet i = (HSSFSheet) sh.next();
				if (dont.contains(i.getSheetName()))
					continue;
				ArrayList<Settings> setts = optimals.get(i.getSheetName());
				Settings set = Utils.getSettings(setts, year - 1);
				ArrayList<FinalEntry> fes = XlSUtils.runWithSettingsList(i, XlSUtils.selectAllAll(i), set);
				float profit = Utils.getProfit(fes);
				total += profit;
			}
			
			totalPeriod += total;
			System.out.println("Total for " + year + " : " + total);
			workbookdata.close();
			filedata.close();
		}

		System.out.println("Avg profit per year using last year best setts: " + totalPeriod / 10);
	}

	public static void makePredictions() throws IOException, InterruptedException, ParseException {
		String basePath = new File("").getAbsolutePath();
		FileInputStream file;
		try {
			file = new FileInputStream(new File("fixtures.xls"));
			HSSFWorkbook workbook = new HSSFWorkbook(file);
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (file != null) {
				try {
					file.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}
		
		HSSFSheet sheet = workbook.getSheetAt(0);
		ArrayList<ExtendedFixture> fixtures = XlSUtils.selectForPrediction(sheet);

		FileInputStream filedata;
		try {
			filedata = new FileInputStream(new File("all-euro-data-2015-2016.xls"));
			HSSFWorkbook workbookdata = new HSSFWorkbook(filedata);
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (filedata != null) {
				try {
					filedata.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}

		HashMap<String, Settings> optimal = new HashMap<>();
		Iterator<Sheet> sh = workbookdata.sheetIterator();
		while (sh.hasNext()) {
			HSSFSheet i = (HSSFSheet) sh.next();
			if (i.getSheetName().equals("SP2"))
				optimal.put(i.getSheetName(), XlSUtils.predictionSettings(i, 2015));
		}

		for (ExtendedFixture f : fixtures) {
			HSSFSheet league = workbookdata.getSheet(f.competition);
			XlSUtils.makePrediction(sheet, league, f, optimal.get(league.getSheetName()));
		}
		workbook.close();
		workbookdata.close();
	}

	public static void asianPredictions() throws IOException, InterruptedException, ParseException {
		String basePath = new File("").getAbsolutePath();
		FileInputStream file;
		try {
			file = new FileInputStream(new File("fixtures.xls"));
			HSSFWorkbook workbook = new HSSFWorkbook(file);
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (file != null) {
				try {
					file.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}
		
		HSSFSheet sheet = workbook.getSheetAt(0);
		ArrayList<ExtendedFixture> fixtures = XlSUtils.selectForPrediction(sheet);

		FileInputStream filedata;
		try {
			filedata = new FileInputStream(new File("all-euro-data-2015-2016.xls"));
		} catch (Exception e) {
			System.out.println("Something was wrong");
		} finally {
			if (filedata != null) {
				try {
					filedata.close (); // OK
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
               }
			}
		}
		HSSFWorkbook workbookdata = new HSSFWorkbook(filedata);

		ArrayList<AsianEntry> all = new ArrayList<>();
		HashMap<String, SettingsAsian> optimal = new HashMap<>();
		Iterator<Sheet> sh = workbookdata.sheetIterator();
		while (sh.hasNext()) {
			HSSFSheet i = (HSSFSheet) sh.next();
			optimal.put(i.getSheetName(), XlSUtils.asianPredictionSettings(i, 2015));
		}

		for (ExtendedFixture f : fixtures) {
			HSSFSheet league = workbookdata.getSheet(f.competition);
			all.addAll(AsianUtils.makePrediction(sheet, league, f, optimal.get(league.getSheetName())));
		}

		all.sort(new Comparator<AsianEntry>() {

			@Override
			public int compare(AsianEntry o1, AsianEntry o2) {
				return ((Float) o2.expectancy).compareTo((Float) o1.expectancy);
			}
		});

		System.out.println(all);
		workbook.close();
		workbookdata.close();
	}

	public static void printSuccessRate(ArrayList<FinalEntry> list, String listName) {
		int successOver50 = 0, failureOver50 = 0;
		for (FinalEntry fe : list) {
			if (fe.success())
				successOver50++;
			else
				failureOver50++;
		}
		System.out.println("success" + listName + ": " + successOver50 + "failure" + listName + ": " + failureOver50);
		System.out
				.println("Rate" + listName + ": " + String.format("%.2f", ((float) successOver50 / list.size()) * 100));
		System.out.println("Profit" + listName + ": " + String.format("%.2f", successOver50 * 0.9 - failureOver50));
	}

	public static float basic1(ExtendedFixture f) {
		ArrayList<ExtendedFixture> lastHomeTeam = SQLiteJDBC.selectLastAll(f.homeTeam, 5, 2014, f.matchday,
				f.competition);
		ArrayList<ExtendedFixture> lastAwayTeam = SQLiteJDBC.selectLastAll(f.awayTeam, 5, 2014, f.matchday,
				f.competition);
		ArrayList<ExtendedFixture> lastHomeHomeTeam = SQLiteJDBC.selectLastHome(f.homeTeam, 5, 2014, f.matchday,
				f.competition);
		ArrayList<ExtendedFixture> lastAwayAwayTeam = SQLiteJDBC.selectLastAway(f.awayTeam, 5, 2014, f.matchday,
				f.competition);
		float allGamesAVG = (Utils.countOverGamesPercent(lastHomeTeam) + Utils.countOverGamesPercent(lastAwayTeam)) / 2;
		float homeAwayAVG = (Utils.countOverGamesPercent(lastHomeHomeTeam)
				+ Utils.countOverGamesPercent(lastAwayAwayTeam)) / 2;
		float BTSAVG = (Utils.countBTSPercent(lastHomeTeam) + Utils.countBTSPercent(lastAwayTeam)) / 2;

		return 0.4f * allGamesAVG + 0.4f * homeAwayAVG + 0.2f * BTSAVG;
	}

	public static float last10only(ExtendedFixture f, int n) {
		ArrayList<ExtendedFixture> lastHomeTeam = SQLiteJDBC.selectLastAll(f.homeTeam, n, 2014, f.matchday,
				f.competition);
		ArrayList<ExtendedFixture> lastAwayTeam = SQLiteJDBC.selectLastAll(f.awayTeam, n, 2014, f.matchday,
				f.competition);

		float allGamesAVG = (Utils.countOverGamesPercent(lastHomeTeam) + Utils.countOverGamesPercent(lastAwayTeam)) / 2;
		return allGamesAVG;
	}

	public static float last5HAonly(ExtendedFixture f) {
		ArrayList<ExtendedFixture> lastHomeHomeTeam = SQLiteJDBC.selectLastHome(f.homeTeam, 5, 2014, f.matchday,
				f.competition);
		ArrayList<ExtendedFixture> lastAwayAwayTeam = SQLiteJDBC.selectLastAway(f.awayTeam, 5, 2014, f.matchday,
				f.competition);

		float homeAwayAVG = (Utils.countOverGamesPercent(lastHomeHomeTeam)
				+ Utils.countOverGamesPercent(lastAwayAwayTeam)) / 2;
		return homeAwayAVG;
	}

	public static float last10BTSonly(ExtendedFixture f) {
		ArrayList<ExtendedFixture> lastHomeTeam = SQLiteJDBC.selectLastAll(f.homeTeam, 10, 2014, f.matchday,
				f.competition);
		ArrayList<ExtendedFixture> lastAwayTeam = SQLiteJDBC.selectLastAll(f.awayTeam, 10, 2014, f.matchday,
				f.competition);

		float BTSAVG = (Utils.countBTSPercent(lastHomeTeam) + Utils.countBTSPercent(lastAwayTeam)) / 2;
		return BTSAVG;
	}

	public static float basic2(ExtendedFixture f, int year, float d, float e, float z) {
		ArrayList<ExtendedFixture> lastHomeTeam = SQLiteJDBC.selectLastAll(f.homeTeam, 10, year, f.matchday,
				f.competition);
		ArrayList<ExtendedFixture> lastAwayTeam = SQLiteJDBC.selectLastAll(f.awayTeam, 10, year, f.matchday,
				f.competition);
		ArrayList<ExtendedFixture> lastHomeHomeTeam = SQLiteJDBC.selectLastHome(f.homeTeam, 5, year, f.matchday,
				f.competition);
		ArrayList<ExtendedFixture> lastAwayAwayTeam = SQLiteJDBC.selectLastAway(f.awayTeam, 5, year, f.matchday,
				f.competition);
		float allGamesAVG = (Utils.countOverGamesPercent(lastHomeTeam) + Utils.countOverGamesPercent(lastAwayTeam)) / 2;
		float homeAwayAVG = (Utils.countOverGamesPercent(lastHomeHomeTeam)
				+ Utils.countOverGamesPercent(lastAwayAwayTeam)) / 2;
		float BTSAVG = (Utils.countBTSPercent(lastHomeTeam) + Utils.countBTSPercent(lastAwayTeam)) / 2;

		return d * allGamesAVG + e * homeAwayAVG + z * BTSAVG;
	}

	public static float poisson(ExtendedFixture f, int year) {
		ArrayList<ExtendedFixture> lastHomeTeam = SQLiteJDBC.selectLastAll(f.homeTeam, 10, year, f.matchday,
				f.competition);
		ArrayList<ExtendedFixture> lastAwayTeam = SQLiteJDBC.selectLastAll(f.awayTeam, 10, year, f.matchday,
				f.competition);
		float lambda = Utils.avgFor(f.homeTeam, lastHomeTeam);
		float mu = Utils.avgFor(f.awayTeam, lastAwayTeam);
		return Utils.poissonOver(lambda, mu);
	}

	public static float poissonWeighted(ExtendedFixture f, int year) {
		float leagueAvgHome = SQLiteJDBC.selectAvgLeagueHome(f.competition, year, f.matchday);
		float leagueAvgAway = SQLiteJDBC.selectAvgLeagueAway(f.competition, year, f.matchday);
		float homeAvgFor = SQLiteJDBC.selectAvgHomeTeamFor(f.competition, f.homeTeam, year, f.matchday);
		float homeAvgAgainst = SQLiteJDBC.selectAvgHomeTeamAgainst(f.competition, f.homeTeam, year, f.matchday);
		float awayAvgFor = SQLiteJDBC.selectAvgAwayTeamFor(f.competition, f.awayTeam, year, f.matchday);
		float awayAvgAgainst = SQLiteJDBC.selectAvgAwayTeamAgainst(f.competition, f.awayTeam, year, f.matchday);

		float lambda = homeAvgFor * awayAvgAgainst / leagueAvgAway;
		float mu = awayAvgFor * homeAvgAgainst / leagueAvgHome;
		return Utils.poissonOver(lambda, mu);
	}

	public enum DataType {
		ALLEURODATA, ODDSPORTAL
	}

}