package scraper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

class ScraperControls {

	static void controlFileFinally(FileInputStream file) {
		if (file != null) {
			try {
				file.close(); // OK
			} catch (java.io.IOException e3) {
				System.out.println("I/O Exception");
			}
		}
	}

	private static Elements createElements() {
		return new Elements();
	}

	private static Actions createAction(WebDriver driver) {
		return new Actions(driver);
	}

	private static void control2ForCollectUpToDate(Elements linksM, Elements fixtures,
			Date yesterday, Set<ExtendedFixture> set ,ArrayList<ExtendedFixture> result) {
		for (Element linkM : linksM) {
			if (isScore(linkM.text())) {
				fixtures.add(linkM);
			}
		}

		for (int i = fixtures.size() - 1; i >= 0; i--) {
			Document fixture = Jsoup.connect(BASE + fixtures.get(i).attr("href")).timeout(0).get();
			ExtendedFixture ef = getFixture(fixture, competition);
			if (ef != null && ef.date.before(yesterday)) {
				breakFlag = true;
				break;
			}
			result.add(ef);
			set.add(ef);
		}
	}

	private static void controlCollectUpToDate(WebDriver driver, Set<ExtendedFixture> set, Date yesterday,
			ArrayList<ExtendedFixture> result, String competition) {
		while (true) {
			int setSize = set.size();
			String html = driver.getPageSource();
			Document matches = Jsoup.parse(html);

			boolean breakFlag = false;
			Element list = matches.select("table[class=matches   ]").first();
			Elements linksM = list.select("a[href]");
			Elements fixtures = createElements();

			control2ForCollectUpToDate(linksM, fixtures, competition, yesterday, set, result);

			/**
			 * for (Element linkM : linksM) { if (isScore(linkM.text())) {
			 * fixtures.add(linkM); } }
			 * 
			 * for (int i = fixtures.size() - 1; i >= 0; i--) { Document fixture
			 * = Jsoup.connect(BASE +
			 * fixtures.get(i).attr("href")).timeout(0).get(); ExtendedFixture
			 * ef = getFixture(fixture, competition); if (ef != null &&
			 * ef.date.before(yesterday)) { breakFlag = true; break; }
			 * result.add(ef); set.add(ef); }
			 */

			if (breakFlag)
				break;
		}

		Actions actions = createAction(driver);
		actions.moveToElement(driver.findElement(By.className("previous"))).click().perform();
		Thread.sleep(1000);
		String htmlAfter = driver.getPageSource();

		if (html.equals(htmlAfter))
			break;
	}

	static ArrayList<ExtendedFixture> collectUpToDate(String competition, int currentYear, Date yesterday, String add)
			throws IOException, ParseException, InterruptedException {
		ArrayList<ExtendedFixture> result = new ArrayList<>();
		Set<ExtendedFixture> set = new HashSet<>();
		String address;
		if (add == null) {
			address = EntryPoints.getLink(competition, currentYear);
			System.out.println(address);
		} else
			address = add;
		System.setProperty("webdriver.chrome.drive", "chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.navigate().to(address);

		// try {
		// WebDriverWait wait = new WebDriverWait(driver, 10);
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hstp_14536_interstitial_pub"))).click();
		// System.out.println("Successfully closed efbet add");
		// } catch (Exception e) {
		// System.out.println("Problem closing efbet add");
		// }

		controlCollectUpToDate(driver, set, yesterday, result, competition);
		/**
		 * while (true) { int setSize = set.size(); String html =
		 * driver.getPageSource(); Document matches = Jsoup.parse(html);
		 * 
		 * boolean breakFlag = false; Element list =
		 * matches.select("table[class=matches ]").first(); Elements linksM =
		 * list.select("a[href]"); Elements fixtures = createElements(); for
		 * (Element linkM : linksM) { if (isScore(linkM.text())) {
		 * fixtures.add(linkM); } }
		 * 
		 * for (int i = fixtures.size() - 1; i >= 0; i--) { Document fixture =
		 * Jsoup.connect(BASE + fixtures.get(i).attr("href")).timeout(0).get();
		 * ExtendedFixture ef = getFixture(fixture, competition); if (ef != null
		 * && ef.date.before(yesterday)) { breakFlag = true; break; }
		 * result.add(ef); set.add(ef); }
		 * 
		 * if (breakFlag) break;
		 * 
		 * Actions actions = createAction(driver);
		 * actions.moveToElement(driver.findElement(By.className("previous"))).click().perform();
		 * Thread.sleep(1000); String htmlAfter = driver.getPageSource();
		 * 
		 * if (html.equals(htmlAfter)) break;
		 */

		// Additional stopping condition - no new entries
		// if (set.size() == setSize)
		// break;

		driver.close();

		if (result.size() != set.size())
			System.out.println("size problem of shots data");

		ArrayList<ExtendedFixture> setlist = new ArrayList<>();
		set.addAll(result);
		setlist.addAll(set);
		return setlist;
	}

	static void controlList(ArrayList<ExtendedFixture> list, String competition, int collectYear, Date oldestTocheck) {
		int count = 0;
		int maxTries = 1;
		try {
			while (true) {
				list = collectUpToDate(competition, collectYear, Utils.getYesterday(oldestTocheck), null);
				break;
			}
		} catch (Exception e) {
			if (++count == maxTries)
				throw e;
		}
	}

	static void controlFirstDoubleFor(ArrayList<ExtendedFixture> all, ArrayList<ExtendedFixture> combined,
			ArrayList<ExtendedFixture> toAdd) {

		for (ExtendedFixture i : all) {
			boolean continueFlag = false;
			for (ExtendedFixture comb : combined) {
				if (i.homeTeam.equals(comb.homeTeam) && i.awayTeam.equals(comb.awayTeam)
						&& (Math.abs(i.date.getTime() - comb.date.getTime()) <= 24 * 60 * 60 * 1000)) {
					continueFlag = true;
				}
			}
			if (!continueFlag)
				toAdd.add(i);
		}
	}

	static void controlSecondDoubleFor(ArrayList<ExtendedFixture> toAdd, ArrayList<ExtendedFixture> next,
			ArrayList<ExtendedFixture> withNext) {
		for (ExtendedFixture i : toAdd) {
			boolean continueFlag = false;
			for (ExtendedFixture n : next) {
				if (i.homeTeam.equals(n.homeTeam) && i.awayTeam.equals(n.awayTeam)
						&& (Math.abs(i.date.getTime() - n.date.getTime()) <= 24 * 60 * 60 * 1000)) {
					continueFlag = true;
					break;
				}
			}
			if (!continueFlag)
				withNext.add(i);
		}
	}

	private static ArrayList<String> createArrayListString() {
		return new ArrayList<String>();
	}

	private static void control1of2ForOddsUpToDate(WebDriver driver, String competition,
			ArrayList<String> links,  Date yesterday, boolean breakFlag, Set<ExtendedFixture> result) {
		for (String i : links) {
			ExtendedFixture ef = getOddsFixture(driver, i, competition, false, OnlyTodayMatches.FALSE);

			if (ef != null && ef.date.before(yesterday)) {
				breakFlag = true;
				break;
			}
			result.add(ef);
		}
	}
	
	private static void control2ForOddsUpToDate(WebDriver driver, List<WebElement> list, String competition,
			ArrayList<String> links, boolean breakFlag, Date yesterday, Set<ExtendedFixture> result) {
		for (WebElement i : list) {
			// better logic here?
			Thread.sleep(100);
			if (i.getText().contains("-") && isFixtureLink(i.getAttribute("href")))
				links.add(i.getAttribute("href"));
			
			control1of2ForOddsUpToDate(links, driver, competition, yesterday, breakFlag, result);
		}
	}

	static void controlOddsUpToDate(WebDriver driver, String address, String competition, Date yesterday,
			Set<ExtendedFixture> result, int maxPage) {
		boolean breakFlag = false;
		try {
			for (int page = 1; page <= maxPage; page++) {
				driver.navigate().to(address + "/results/#/page/" + page + "/");

				String[] splitAddress = address.split("/");
				String leagueYear = splitAddress[splitAddress.length - 1];
				List<WebElement> list = driver.findElements(By.cssSelector("a[href*='" + leagueYear + "']"));
				ArrayList<String> links = createArrayListString();

				control2ForOddsUpToDate(driver, list, competition, links, breakFlag, yesterday, result);
				/**
				 * for (WebElement i : list) { // better logic here?
				 * Thread.sleep(100); if (i.getText().contains("-") &&
				 * isFixtureLink(i.getAttribute("href")))
				 * links.add(i.getAttribute("href"));
				 * 
				 * for (String i : links) { ExtendedFixture ef =
				 * getOddsFixture(driver, i, competition, false,
				 * OnlyTodayMatches.FALSE);
				 * 
				 * if (ef != null && ef.date.before(yesterday)) { breakFlag =
				 * true; break; } result.add(ef); } }
				 */
			}
		} catch (Exception e) {
			System.out.println("Something was wrong");
			// e.printStackTrace();
			page--;
			System.out.println("Starting over from page:" + page);
			driver.close();
			Thread.sleep(20000);
			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().window().maximize();

			driver.navigate().to(address + "/results/");
			login(driver);
			driver.navigate().to(address + "/results/");
		}
	}

	static void login(WebDriver driver) {
		String label = "Login";
		driver.findElement(By.xpath("//button[contains(.,'" + label + "')]")).click();

		String pass = "";
		Path wiki_path = Paths.get(new File("").getAbsolutePath(), "pass.txt");

		Charset charset = Charset.forName("ISO-8859-1");
		try {
			List<String> lines = Files.readAllLines(wiki_path, Charset.defaultCharset());

			for (String line : lines)
				pass += (line.trim());
		} catch (IOException e) {
			System.out.println("Something was wrong");
		}

		driver.findElement(By.id("login-username1")).sendKeys("Vortex84");
		driver.findElement(By.id("login-password1")).sendKeys(pass);

		// click the local login button
		driver.findElements(By.xpath("//button[contains(.,'" + label + "')]")).get(1).click();
	}

	static void controlPages(List<WebElement> pages, int maxPage) {

		for (WebElement i : pages) {
			if (isNumeric(i.getText()) && Integer.parseInt(i.getText().trim()) > maxPage)
				maxPage = Integer.parseInt(i.getText().trim());
		}
	}

	static void controlListLinks(List<WebElement> list, ArrayList<String> links) {
		for (WebElement i : list) {
			// better logic here?
			if (i.getText().contains("-") && isFixtureLink(i.getAttribute("href")))
				links.add(i.getAttribute("href"));
		}
	}

	private static boolean isFixtureLink(String attribute) {
		String[] split = attribute.split("/");
		String fixturePart = split[split.length - 1];
		String[] split2 = fixturePart.split("-");
		if (split2.length < 3)
			return false;
		else {
			if (split2[split2.length - 1].length() == 8)
				return true;
			else
				return false;
		}

	}

	static void controlLinks(ArrayList<String> links, WebDriver driver, String competition,
			Set<ExtendedFixture> result) {
		for (String i : links) {
			ExtendedFixture ef = getFastOddsFixture(driver, i, competition);
			if (ef != null)
				result.add(ef);

			break;
		}
	}
	
	static void controlOneResElement(WebElement resElement, String resString, boolean liveMatchesFlag){
		if (resElement != null && (resString.contains("penalties") || resString.contains("ET") || resString.contains("Postponed"))) {
			return null;
		}
		if (resElement != null && ((!liveMatchesFlag && resString.contains("already started")) || resString.contains("Abandoned"))) {
			return null;
		}
	}
	
	static void controlTwoResElement(WebElement resElement, String resString, Result fullResult, Result htResult){
		if (resElement != null && (resString.contains("awarded") && resString.contains(home))) {
			fullResult = new Result(3, 0);
			htResult = new Result(3, 0);
		}
		if (resElement != null && (resString.contains("awarded") && resString.contains(away))) {
			fullResult = new Result(0, 3);
			htResult = new Result(0, 3);
		}
	}

	static void controlPinnIndex(WebElement row, List<WebElement> customer, int pinnIndex){
		if (row.getText().contains("Pinnacle"))
			pinnIndex = customer.indexOf(row) + 1;
		if (row.getText().contains("bet365"))
			pinnIndex = customer.indexOf(row) + 1;
	}

	static void controlClick(WebElement tabs){
		for (WebElement t : tabs) {
			if (t.getText().contains("O/U")) {
				t.click();
				break;
			}
		}
	}
	
	static void controlDiv(List<WebElement> divs, WebElement div25, WebDriver driver){
		for (WebElement div : divs) {
			if (div.getText().contains("+2.5")) {
				// System.out.println(div.getText());
				div25 = div;
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("window.scrollBy(0,250)", "");
				div.click();
				break;
			}
		}
	}
	
	static void controlOdds(WebElement row, float overOdds, float underOdds, float overOdds365, float underOdds365){
		if (row.getText().contains("Pinnacle")) {
			String textOdds = row.getText();
			overOdds = Float.parseFloat(textOdds.split("\n")[2].trim());
			underOdds = Float.parseFloat(textOdds.split("\n")[3].trim());
			break;
		}
	
		if (row.getText().contains("bet365")) {
			String textOdds = row.getText();
			try {
				overOdds365 = Float.parseFloat(textOdds.split("\n")[2].trim());
				underOdds365 = Float.parseFloat(textOdds.split("\n")[3].trim());
			} catch (Exception e) {
				// nothing
			}
		}
	}
	
	static void controlContainsAh(List<WebElement> tabs){
		for (WebElement t : tabs) {
			if (t.getText().contains("AH")) {
				t.click();
				break;
			}
		}
	}
	
	static void controlSplit(WebElement div, float x, WebElement y){
		String text = div.getText();
		if (text.split("\n").length > 3) {
			float diff = Math.abs(Float.parseFloat(text.split("\n")[2].trim())
					- Float.parseFloat(text.split("\n")[3].trim()));
		}
		if (text.split("\n").length > 3 && diff < min) {
			x = diff;
			y = div;
		}
	}

	static void controlRow(List<WebElement> rowsAsian, float line, float asianHome, float asianAway){
		for (WebElement row : rowsAsian) {
			if (row.getText().contains("Pinnacle")) {
				String textOdds = row.getText();
				line = Float.parseFloat(textOdds.split("\n")[1].trim());
				asianHome = Float.parseFloat(textOdds.split("\n")[2].trim());
				asianAway = Float.parseFloat(textOdds.split("\n")[3].trim());
				break;
			}
		}
	}
	
	private static void controlWhileCollectFull(int fixtureCount, ArrayList<PlayerFixture> result) {
		while (true) {
			Document fixture = Jsoup.connect(BASE + linkM.attr("href")).timeout(30 * 1000).get();
			ArrayList<PlayerFixture> ef = getFixtureFull(fixture, competition);
			fixtureCount++;
			result.addAll(ef);
			break;
			}
	}
	
	static void controlForCollectFull(int fixtureCount, ArrayList<PlayerFixture> result,
			Elements linksM) {
		for (Element linkM : linksM && isScore(linkM.text())) {
			int count = 0;
			int maxTries = 10;
			try {
					controlWhileCollectFull(fixtureCount, result);
				} catch (Exception e) {
					if (++count == maxTries)
						throw e;
				}
		}
	}
	
	static void controlForGetFixture(Elements frames, int shotsHome, int shotsAway) {
		Document stats = Jsoup.connect(BASE + i.attr("src")).timeout(0).get();
		for (Element i : frames && i.attr("src").contains("/charts/statsplus")) {
			shotsHome = Integer.parseInt(
					stats.select("tr:contains(Shots on target)").get(1).select("td.legend.left.value").text());

			shotsAway = Integer.parseInt(
					stats.select("tr:contains(Shots on target)").get(1).select("td.legend.right.value").text());
		}
	}
	
	static void controlFirstForGetFixtureFull(Elements rowsH, ExtendedFixture fix, String Team) {
		for (int i = 1; i < rowsH.size(); i++) {// without coach
			Element row = rowsH.get(i);
			if (row.text().contains("Coach") || row.text().contains("coach") || row.text().isEmpty())
				continue;

			Elements cols = row.select("td");
			if (cols.size() < 2)
				continue;
			String name = "";
			name = Utils.replaceNonAsciiWhitespace(cols.get(cols.size() == 2 ? 0 : 1).text());
			PlayerFixture pf = new PlayerFixture(fix, Team, name, 90, true, false, 0, 0);
			playerFixtures.add(pf);

			}
	}
	
	private static void controlForInFirstIf(ArrayList<PlayerFixture> playerFixtures, String outPlayer, int minute) {
		for (PlayerFixture player : playerFixtures) {
			if (player.name.equals(outPlayer)) {
				player.minutesPlayed = minute;
				break;
			}
		}
	}
	
	private static void controlFirstIfInSecondForGetFixtureFull(String hATeam, ExtendedFixture fix, ArrayList<PlayerFixture> playerFixtures,
			String name) {
		if (name.contains(" for ")) {
			String inPlayer = name.split(" for ")[0].trim();
			String outPlayer = name.split(" for ")[1].split("[0-9]+'")[0].trim();
			
			int minute = 0;
			String cleanMinutes = name.split(" ")[name.split(" ").length - 1].split("'")[0];
			
			if (cleanMinutes.contains("+"))
				minute = Integer.parseInt(cleanMinutes.split("\\+")[0])
						+ Integer.parseInt(cleanMinutes.split("\\+")[1]);
			else
				minute = Integer.parseInt(cleanMinutes);
			
			PlayerFixture pf = new PlayerFixture(fix, homeTeam, inPlayer, 90 - minute, false, true, 0, 0);
			playerFixtures.add(pf);

			controlForInFirstIf(playerFixtures, outPlayer, minute);

			if (verbose)
				System.out.println(inPlayer + " for " + outPlayer + " in " + minute);

		} else {
			PlayerFixture pf = new PlayerFixture(fix, hATeam, name, 0, false, false, 0, 0);
			playerFixtures.add(pf);
			if (verbose)
				System.out.println(shirtNumber + " " + name);
		}
	}
	
	static void controlSecondForGetFixtureFull(Elements rowsHA, String hATeam, ExtendedFixture fix,
			ArrayList<PlayerFixture> playerFixtures, String homeTeam) {
		for (int i = 1; i < rowsHA.size(); i++) {
			Element row = rowsHA.get(i);
			Elements cols = row.select("td");
			String shirtNumber = cols.get(0).text();
			String name = Utils.replaceNonAsciiWhitespace(cols.get(cols.size() == 2 ? 0 : 1).text());

			controlFirstIfInSecondForGetFixtureFull(hATeam, fix, playerFixtures, name);
		}
	}
	
	static void controlHomeGoal(ArrayList<PlayerFixture> playerFixtures, String[] splitByMinute) {
		String goalScorer = splitByMinute[0].trim();
		goalScorer = Utils.replaceNonAsciiWhitespace(goalScorer).trim();
		if (goalScorer.contains("(PG)")) {
			goalScorer = goalScorer.replace("(PG)", "").trim();
		}

		if (!goalScorer.contains("(OG)"))
			updatePlayer(goalScorer, playerFixtures, true);

		if (splitByMinute.length > 1) {
			// Extra info like assistedS by, PG or OG
			String extraString = splitByMinute[1];

			if (extraString.contains("assist by")) {
				String assister = splitByMinute[1].split("\\(assist by ")[1].trim();
				assister = Utils.replaceNonAsciiWhitespace(assister);
				assister = assister.substring(0, assister.length() - 1).trim();
				updatePlayer(assister, playerFixtures, false);
			}
		}
	}
	
	/**
	 * Updates the goals or assists (by 1) of the player in the playerFixtures
	 * collection
	 * 
	 * @param name
	 * @param playerFixtures
	 * @param goals
	 *            true if updating goals, false if updating assists
	 */

	private static void updatePlayer(String name, ArrayList<PlayerFixture> playerFixtures, boolean goals) {
		boolean updated = false;
		for (PlayerFixture player : playerFixtures) {
			if (player.name.equals(name)) {
				if (goals)
					player.goals++;
				else
					player.assists++;
				updated = true;
				break;
			}
		}
		// Utils.printPlayers(playerFixtures);

		if (!updated)
			System.err.println("Problem in updating " + (goals ? "goals " : "assists ") + "for " + name);

	}
	
	private static void control2IfAwayGoal(ArrayList<PlayerFixture> playerFixtures, String[] splitByMinute) {
		String goalScorer = splitByMinute[1].replace("(PG)", "").trim();
		if (goalScorer.contains("+"))
			goalScorer = goalScorer.replace("+", "").replaceAll("\\d", "").trim();

		goalScorer = Utils.replaceNonAsciiWhitespace(goalScorer).trim();

		if (goalScorer.contains("assist by")) {
			String assister = goalScorer.split("\\(assist by ")[1].trim();
			assister = Utils.replaceNonAsciiWhitespace(assister);
			assister = assister.substring(0, assister.length() - 1);
			updatePlayer(assister, playerFixtures, false);
			goalScorer = goalScorer.split("\\(assist by ")[0].trim();
		}
		updatePlayer(goalScorer, playerFixtures, true);
	}
	
	static void controlAwayGoal(ArrayList<PlayerFixture> playerFixtures, String[] splitByMinute) {
	
		if (splitByMinute[1].contains("(PG)")) {
			control2IfAwayGoal(playerFixtures, splitByMinute);
		} 
		if (splitByMinute[1].contains("assist by")) {
			String goalScorer = splitByMinute[1].split("\\(assist by ")[0].trim();
			goalScorer = Utils.replaceNonAsciiWhitespace(goalScorer).trim();
			if (goalScorer.contains("+"))// scored in
				goalScorer = goalScorer.replace("+", "").replaceAll("\\d", "").trim();
	
			updatePlayer(goalScorer, playerFixtures, true);
	
			String assister = splitByMinute[1].split("\\(assist by ")[1].trim();
			assister = Utils.replaceNonAsciiWhitespace(assister);
			assister = assister.substring(0, assister.length() - 1).trim();
			updatePlayer(assister, playerFixtures, false);
		} 
		if (!splitByMinute[1].contains("(OG)")) { // Solo goal no assists, no PG,no OG
			String goalScorer = Utils.replaceNonAsciiWhitespace(splitByMinute[1].trim()).trim();
			if (goalScorer.contains("+"))
				goalScorer = goalScorer.replace("+", "").replaceAll("\\d", "").trim();
			
			updatePlayer(goalScorer, playerFixtures, true);
		}
	}
	
	static void controlForGoals(Elements cols, boolean verbose, ArrayList<PlayerFixture> playerFixtures) {
		for (Element j : cols) {
			if (!isScore(j.text()) && !j.text().isEmpty()) {
				controlVerbose(verbose, j.text());
				String[] splitByMinute = j.text().split("[0-9]+'");
				controlVerbose(verbose, splitByMinute.length);
				if (!splitByMinute[0].isEmpty()) { // Home goal
					ScraperControls.controlHomeGoal(playerFixtures, splitByMinute);
				} else { // Away goal
					ScraperControls.controlAwayGoal(splitByMinute, playerFixtures);
				}
			}
		}
	}
	
	static void controlFirstForOdds(List<WebElement> list, ArrayList<String> links) {
		for (WebElement i : list) {
			String href = i.getAttribute("href");
			if (i.getText().contains("-") && isFixtureLink(href)) {
				links.add(href);
	
			}
		}
	}
	
	static void controlSecondForOdds(ArrayList<String> links, Set<ExtendedFixture> result) {
		for (String i : links) {
			ExtendedFixture ef = getOddsFixture(driver, i, competition, false, OnlyTodayMatches.FALSE);
			if (ef != null)
				result.add(ef);
		}
	}
	
	static void controlFirstForFullOdds(List<WebElement> el, ArrayList<String> links) {
		for (WebElement i : el) {
			String href = i.getAttribute("href");
			if (i.getText().contains("-") && isFixtureLink(href))
				links.add(href);
		}
	}
	
	static void controlSecondForFullOdds(ArrayList<String> links, Set<FullFixture> result) {
		for (String i : links) {
			FullFixture ef = getFullFixtureTest(driver, i, competition);
			if (ef != null)
				result.add(ef);
		}
	}
	
	static void controlFirstForOddsByPage(List<WebElement> list, ArrayList<String> links) {
		for (WebElement i : list) {
			if (i.getText().contains("-") && isFixtureLink(i.getAttribute("href")))
				links.add(i.getAttribute("href"));
		}
	}
	
	static void controlSecondForOddsByPage(ArrayList<String> links, Set<FullFixture> result) {
		for (WebElement i : list) {
			if (i.getText().contains("-") && isFixtureLink(i.getAttribute("href")))
				links.add(i.getAttribute("href"));
		}
	}

	static void controlVerbose(boolean verbose, String s) {
		if (verbose)
			System.out.println(s);
	}
	
	static void controlResElement(WebElement resElement, String resString, Result fullResult, Result htResult){
		if (resElement != null && (resString.contains("penalties") || resString.contains("ET"))) {
			return null;
		}
		if (resElement != null && (resString.contains("awarded") && resString.contains(home))) {
			fullResult = new Result(3, 0);
			htResult = new Result(3, 0);
		}
		if (resElement != null && (resString.contains("awarded") && resString.contains(away))) {
			fullResult = new Result(0, 3);
			htResult = new Result(0, 3);
		}
	}
	
	static void controlText(String text, float min, WebElement opt, WebElement div){
		if (text.split("\n").length > 3) {
			float diff = Math.abs(Float.parseFloat(text.split("\n")[2].trim())
					- Float.parseFloat(text.split("\n")[3].trim()));
		}
		if (text.split("\n").length > 3 && diff < min) {
			min = diff;
			opt = div;
		}
	}

	private static void controlIfRowAsianOverPinnacle(WebElement row, String rowText){
		if (row.getText().contains("Average"))
			break;
		String[] oddsArray = rowText.split("\n");
		// System.out.println(rowText);
		if (oddsArray.length != 5)
			continue;
		

		if (Arrays.asList(MinMaxOdds.FAKEBOOKS).contains(bookmaker) || bookmaker.isEmpty())
			continue;
	}
	
	static void controlRowAsianOverPinnacle(List<WebElement> rowsGoals, float line, float x, float y, ArrayList<Odds> matchOdds, Odds pinnOdds){
		for (WebElement row : rowsGoals) {
			String rowText = row.getText();
			String bookmaker = oddsArray[0].trim();
			controlIfRowAsianOverPinnacle(row, rowText, bookmaker);
			
			line = Float.parseFloat(oddsArray[1].trim());
			x = Float.parseFloat(oddsArray[2].trim());
			y = Float.parseFloat(oddsArray[3].trim());
			
	
			Odds modds = new AsianOdds(bookmaker, new Date(), line, x, y);
			matchOdds.add(modds);
	
			if (bookmaker.equals("Pinnacle"))
				pinnOdds = modds;
	
		}
	}

	static void controlColumns(List<WebElement> columns){
		if (columns.size() < 4)
			continue;
		String bookmaker = columns.get(0).getText().trim();
		if (Arrays.asList(MinMaxOdds.FAKEBOOKS).contains(bookmaker))
			continue;
	}
	
	static void controlRowsGoals(List<WebElement> x, float line, float y, float z){
		for (int r = x.size() - 1; r >= 0; r--) {
			WebElement row = x.get(r);
			if (row.getText().contains("Pinnacle")) {
				String textOdds = row.getText();
				line = Float.parseFloat(textOdds.split("\n")[1].trim());
				y = Float.parseFloat(textOdds.split("\n")[2].trim());
				z = Float.parseFloat(textOdds.split("\n")[3].trim());
				break;
			}
	
			if (row.getText().contains("Average"))
				break;
		}
	}
	
	static void controlOver(float over, ArrayList<main.Line> goalLines, float line, float over, float under, main.Line twoAndHalf, WebElement currentDiv, Actions actions){
		if (over != -1f)
			goalLines.add(new main.Line(line, over, under, "Pinn"));
		
		if (over != -1f && Float.compare(line, 2.5f)==0)
			twoAndHalf = new main.Line(line, over, under, "Pinn");
	
		List<WebElement> closeLink = currentDiv.findElements(By.className("odds-co"));
		if (!closeLink.isEmpty())
			actions.moveToElement(closeLink.get(0)).click().perform();
	
		if (goalLines.size() == 6)
			break;
	}
	
	static void controlDiff(ArrayList<main.Line> goalLines, float minDiff, int indexMinDiff){
		for (int l = 0; l < goalLines.size(); l++) {
			float diff = Math.abs(goalLines.get(l).home - goalLines.get(l).away);
			if (diff < minDiff) {
				minDiff = diff;
				indexMinDiff = l;
			}
		}
	}
	
	static void controlGoalLines(ArrayList<main.Line>  goalLines, int expectedCaseSize, int start, int end, int indexMinDiff, GoalLines GLS){
		if (goalLines.size()  && expectedCaseSize == 5) {
			ArrayList<main.Line> bestLines = new ArrayList<>();
			for (int c = start; c <= end; c++)
				bestLines.add(goalLines.get(c));
			
			GLS = new GoalLines(bestLines.get(0), bestLines.get(1), bestLines.get(2), bestLines.get(3),
					bestLines.get(4));
			}
		if (goalLines.size()  && expectedCaseSize == 4 && indexMinDiff - 2 < 0) {
			GLS = new GoalLines(goalLines.get(indexMinDiff - 1), goalLines.get(indexMinDiff),
					goalLines.get(indexMinDiff + 1), goalLines.get(indexMinDiff + 2),
					goalLines.get(indexMinDiff + 3));
		}
		if (goalLines.size()  && expectedCaseSize == 4 && indexMinDiff - 2 >= 0 && indexMinDiff + 2 > goalLines.size() - 1) {
			GLS = new GoalLines(goalLines.get(indexMinDiff - 3), goalLines.get(indexMinDiff - 2),
					goalLines.get(indexMinDiff - 1), goalLines.get(indexMinDiff),
					goalLines.get(indexMinDiff + 1));
		}
	}
	
	static void controlTwoGoalLines(ArrayList<main.Line> goalLines, int indexMinDiff, GoalLines GLS){
		if (goalLines.size() <= 5 && goalLines.size() == 4 && indexMinDiff - 2 < 0) {
			GLS = new GoalLines(new main.Line(-1, -1, -1, "Pinn"), goalLines.get(0), goalLines.get(1),
					goalLines.get(2), goalLines.get(3));
		} else {
			GLS = new GoalLines(goalLines.get(0), goalLines.get(1), goalLines.get(2), goalLines.get(3),
				  new main.Line(-1, -1, -1, "Pinn"));
		}
		
		if (goalLines.size() <= 5 && goalLines.size() == 1)
			GLS.main = goalLines.get(0);
		if (goalLines.size() <= 5 && goalLines.size() != 4) {
			System.out.println("tuka");
		}
	}
	
	static void controlForOverUnder(ArrayList<main.Line> goalLines, float overOdds, float underOdds){
		for (main.Line line : goalLines) {
			if (line.line == 2.5) {
				overOdds = line.home;
				underOdds = line.away;
				break;
			}
		}
	}
	
	static void controlOverTwo(float asianHome, ArrayList<main.Line> lines, float line, float asianAway, WebElement currentDiv, Actions actions){
		if (asianHome != -1f)
			lines.add(new main.Line(line, asianHome, asianAway, "Pinn"));
	
		List<WebElement> closeLink = currentDiv.findElements(By.className("odds-co"));
		if (!closeLink.isEmpty()) {
			actions.moveToElement(closeLink.get(0)).click().perform();
		}
	
		if (lines.size() == 6)
			break;
	}

	static void controlAsianLines(ArrayList<main.Line> lines, int expectedCaseSize, int start, int end, int indexMinDiff, AsianLines asianLines){
		if (lines.size() > 5 && expectedCaseSize == 5) {
			ArrayList<main.Line> bestLines = new ArrayList<>();
			for (int c = start; c <= end; c++) {
				bestLines.add(lines.get(c));
				}
			asianLines = new AsianLines(lines.get(0), lines.get(1), lines.get(2), lines.get(3), lines.get(4));
			} 
		if (lines.size() > 5 && expectedCaseSize == 4 && indexMinDiff - 2 < 0) {
			asianLines = new AsianLines(lines.get(indexMinDiff - 1), lines.get(indexMinDiff),
					lines.get(indexMinDiff + 1), lines.get(indexMinDiff + 2), lines.get(indexMinDiff + 3));
		} 
		if (lines.size() > 5 && expectedCaseSize == 4 && indexMinDiff + 2 > lines.size() - 1) {
			asianLines = new AsianLines(lines.get(indexMinDiff - 3), lines.get(indexMinDiff - 2),
					lines.get(indexMinDiff - 1), lines.get(indexMinDiff), lines.get(indexMinDiff + 1));
		}
	}
	
	static void controlTwoAsianLines(ArrayList<main.Line> lines, int indexMinDiff, AsianLines asianLines){
		if (lines.size() == 4 && indexMinDiff - 2 < 0) {
			asianLines = new AsianLines(new main.Line(-1, -1, -1, "Pinn"), lines.get(0), lines.get(1),
					lines.get(2), lines.get(3));
		} 
		if(lines.size() == 4 && indexMinDiff - 2 >= 0) {
			asianLines = new AsianLines(lines.get(0), lines.get(1), lines.get(2), lines.get(3),
					new main.Line(-1, -1, -1, "Pinn"));
		} 
		if (lines.size() != 4 && lines.size() == 1)
			asianLines.main = lines.get(0);
	}
	
	

}//di classe
