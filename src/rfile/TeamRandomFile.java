package rfile;

import java.io.*;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static rfile.JodaDT.parseYYMMDD;
import static rfile.RandomFileClass.*;
import static rfile.SortMatches.*;

/**
 * TeamRandomFile Examen Corregido v1.1
 * <p>
 * Copyright 2016 Manuel Martínez <ManuMtz@icloud.com> / <ManuMtz@hotmail.co.uk>
 * <p>
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */

public class TeamRandomFile {

    static final String DEFAULT_DIR_TEAM = "data";
    static final String D_FILE_NAME = "default";
    static final String D_FILE_EXT = ".cfg";
    static final String DEFAULT_DIR = "lang";
    static final String DEFAULT_FILE_NAME_LANG = "en_uk";
    static final String D_FILE_EXT_LANG = ".json";
    static final String DEFAULT_FILE = D_FILE_NAME + D_FILE_EXT;
    static final String DEFAULT_LANG = DEFAULT_FILE_NAME_LANG + D_FILE_EXT_LANG;

    static final String SP = File.separator;

    static Properties prop = new Properties();

    static File defaultFilecfg = new File(DEFAULT_DIR_TEAM + SP + DEFAULT_FILE);

    static File defaultFileLang = new File(DEFAULT_DIR + SP + DEFAULT_LANG);

    static String current;

    static HashSet<String> langExist = new HashSet<>();

    static Scanner scan = new Scanner(System.in);

    private static final int MAX_POINTS = 200;

    private static final int REFEREE_LENGTH = 30;

    static final int NAME_TEAM_LENGTH = 12;

    private static final int DC_TEAM_LENGTH = 120;

    static JSONObject jsonObject;

    /**
     * Makes a default cfg file
     */
    public static void DefaultCfgFile() {

        File defaultCfgDir = new File(DEFAULT_DIR_TEAM);

        if (!defaultCfgDir.exists()) {
            defaultCfgDir.mkdirs();
        }

        File defaultLangDir = new File(DEFAULT_DIR);

        if (!defaultLangDir.exists()) {
            defaultLangDir.mkdirs();
        }

        if (!defaultFilecfg.exists()) {

            try (OutputStream output = new FileOutputStream(defaultFilecfg)) {

                // set the properties value
                prop.setProperty("lang", "en_uk");

                // save properties to project root folder
                prop.store(output, null);

            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        jsonLang();
    }

    /**
     * Makes a default json english lang
     */
    public static void jsonLang() {
        if (!defaultFileLang.exists()) {
            JSONObject defaultMenu = new JSONObject();

            defaultMenu.put("changelang", "Change language");
            defaultMenu.put("onelang", "Only there is one language");
            defaultMenu.put("newleague", "New League");
            defaultMenu.put("sponsor", "League Sponsor");
            defaultMenu.put("viewmatch", "View a match");
            defaultMenu.put("viewallmatches", "View all matches");
            defaultMenu.put("viewsorteddate", "View all matches sorted by date");
            defaultMenu.put("viewsortedpoints", "View all matches sorted by total points");
            defaultMenu.put("viewsortedpointsoneteam", "View for one team all matches sorted by favorable points - Ex 2");
            defaultMenu.put("welcomeleague", "Welcome to the league");
            defaultMenu.put("noplayed", "No played yet");
            defaultMenu.put("queuedmatch", "Next match queued");
            defaultMenu.put("reset", "Reset league");
            defaultMenu.put("slang", "Select language");
            defaultMenu.put("clang", "Current language");
            defaultMenu.put("select", "Select");
            defaultMenu.put("selectteam", "Select Team");
            defaultMenu.put("selectlteam", "Select Local Team");
            defaultMenu.put("selectvteam", "Select Visitor Team");
            defaultMenu.put("cantbeequals", "Both teams can't be the same, choose another one");
            defaultMenu.put("pteam", "Team points");
            defaultMenu.put("rname", "Referee name");
            defaultMenu.put("date", "Date");
            defaultMenu.put("addteam", "Add or change a team - Ex 1");
            defaultMenu.put("tname", "Team name");
            defaultMenu.put("dteam", "Team description");
            defaultMenu.put("ctname", "Current team name");
            defaultMenu.put("cdteam", "Current team description");
            defaultMenu.put("listteams", "List all teams - Ex 1");
            defaultMenu.put("menu", "See again main menu");
            defaultMenu.put("sjsonteam", "Show json team - Ex 4");
            defaultMenu.put("noplayers", "No players");
            defaultMenu.put("allplayers", "All players");
            defaultMenu.put("exit", "Exit");
            defaultMenu.put("bye", "Bye!");

            try (FileWriter fileJSON = new FileWriter(defaultFileLang)) {

                fileJSON.write(defaultMenu.toString());
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    /**
     * Finds all .json langs
     */
    public static void langFiles() {
        File langFiles = new File(DEFAULT_DIR);
        File[] rutaDir = langFiles.listFiles();

        String fileName = D_FILE_EXT_LANG;

        ArrayList<File> langF = new ArrayList<>(Arrays.asList(rutaDir));

        for (File file : langF) {
            if (file.isFile() && file.getName().contains(fileName)) {
                langExist.add(file.getName());
            }
        }
    }

    /**
     * Runs cfg file
     */
    public static void loadCfg() {
        DefaultCfgFile();

        try (InputStream input = new FileInputStream(defaultFilecfg)) {

            // load a properties file
            prop.load(input);

            // get the property value
            current = prop.getProperty("lang");

        } catch (IOException io) {
            io.printStackTrace();
        }
        resetlang();
        loadJson();
    }

    /**
     * Resets to default language
     */
    public static void resetlang() {
        //If current language doesn't exist, current language will be set to default

        File cLang = new File(DEFAULT_DIR + SP + current + D_FILE_EXT_LANG);

        if (!cLang.exists()) {
            try (OutputStream output = new FileOutputStream(defaultFilecfg)) {

                // set the properties value
                prop.setProperty("lang", DEFAULT_FILE_NAME_LANG);
                current = DEFAULT_FILE_NAME_LANG;

                // save properties to project root folder
                prop.store(output, null);

            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    /**
     * Sets another language
     */
    public static void changeLang() {
        langFiles();

        System.out.println(jsonObject.get("clang") + ": " + current);

        if (langExist.size() > 1) {
            int i = 0;
            System.out.print("[");
            for (String c : langExist) {
                System.out.print(c.substring(0, c.length() - 5));
                if (i < langExist.size() - 1) {
                    System.out.print(", ");
                }
                i++;
            }
            System.out.print("]");
            System.out.println();
        } else if (langExist.size() < 2) {
            System.out.println(jsonObject.get("onelang"));
            return;
        }
        System.out.print(jsonObject.get("slang") + ": ");
        String lang = scan.nextLine().toLowerCase();

        if (langExist.contains(lang + D_FILE_EXT_LANG)) {

            try (OutputStream output = new FileOutputStream(defaultFilecfg)) {

                // set the properties value
                prop.setProperty("lang", lang);

                // save properties to project root folder
                prop.store(output, null);

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        loadCfg();
    }

    /**
     * App Menu
     */
    public static void menu() {
        System.out.println("\n****************************************");
        System.out.println("[1] " + jsonObject.get("changelang"));
        if (fileExist()) {
            System.out.println("[2] " + jsonObject.get("queuedmatch"));
            System.out.println("[3] " + jsonObject.get("viewmatch"));
            System.out.println("[4] " + jsonObject.get("viewallmatches"));
            System.out.println("[5] " + jsonObject.get("viewsorteddate"));
            System.out.println("[6] " + jsonObject.get("viewsortedpoints"));
            System.out.println("[7] " + jsonObject.get("addteam"));
            System.out.println("[8] " + jsonObject.get("listteams"));
            System.out.println("[9] " + jsonObject.get("viewsortedpointsoneteam"));
            System.out.println("[s] " + jsonObject.get("sjsonteam"));
            System.out.println("[r] " + jsonObject.get("reset"));
        } else {
            System.out.println("[2] " + jsonObject.get("newleague"));
        }
        System.out.println("[q] " + jsonObject.get("exit"));
        System.out.println("****************************************\n");
        System.out.print(jsonObject.get("select") + ": ");
    }

    /**
     * Shows menu
     */
    public static void showMenu() {
        System.out.println("[m] " + jsonObject.get("menu"));
        System.out.print(jsonObject.get("select") + ": ");

    }

    /**
     * Shows bye message
     */
    public static void showBye() {
        System.out.println("\n" + jsonObject.get("bye"));
        scan.close();

    }

    /**
     * Adds a new match
     */
    public static void qMatch() {

        String teaml_tmp;
        int teaml = 0;
        while (teaml > teams || teaml < 1) {
            System.out.print(jsonObject.get("selectlteam") + ": ");
            teaml_tmp = scan.nextLine();
            teaml = parseIntN(teaml_tmp);
            if (teaml_tmp.equals("q")) {
                return;
            }
        }

        String teamv_tmp;
        int teamv = 0;
        while (teamv > teams || teamv < 1 || teamv == teaml) {
            System.out.print(jsonObject.get("selectvteam") + ": ");
            teamv_tmp = scan.nextLine();
            teamv = parseIntN(teamv_tmp);
            if (teamv == teaml) {
                System.out.println(jsonObject.get("cantbeequals"));
            }
            if (teamv_tmp.equals("q")) {
                return;
            }
        }

        String pteam1_tmp;
        short pTeam1 = -1;
        while (!(pTeam1 <= MAX_POINTS) || !(pTeam1 >= 0)) {
            System.out.print(jsonObject.get("pteam") + " " + teaml + " (" + MAX_POINTS + "): ");
            pteam1_tmp = scan.nextLine();
            pTeam1 = parseShortN(pteam1_tmp);
            if (pteam1_tmp.equals("q")) {
                return;
            }
        }

        String pteam2_tmp;
        short pTeam2 = -1;
        while (!(pTeam2 <= MAX_POINTS) || !(pTeam2 >= 0)) {
            System.out.print(jsonObject.get("pteam") + " " + teamv + " (" + MAX_POINTS + "): ");
            pteam2_tmp = scan.nextLine();
            pTeam2 = parseShortN(pteam2_tmp);
            if (pteam2_tmp.equals("q")) {
                return;
            }
        }

        String refereeName = "";
        while (refereeName.length() > REFEREE_LENGTH || refereeName.isEmpty() || refereeName.length() < 2) {
            System.out.print(jsonObject.get("rname") + " - (" + REFEREE_LENGTH + " chars): ");
            refereeName = scan.nextLine();
            if (refereeName.equals("q")) {
                return;
            }
        }

        String date = "";
        while (String.valueOf(date).length() != 6 || !parseDate(String.valueOf(date))) {
            System.out.print(jsonObject.get("date") + " - (YYMMDD): ");
            date = scan.nextLine();
            if (date.equals("q")) {
                return;
            }
        }

        writeRandom(teaml, teamv, pTeam1, pTeam2, refereeName, date);
    }

    /**
     * Adds a team
     */
    public static void addTeam() {

        String team_tmp;
        int team = 0;
        while (team > teams || team < 1) {
            System.out.print(jsonObject.get("selectteam") + ": ");
            team_tmp = scan.nextLine();
            team = parseIntN(team_tmp);
            if (team_tmp.equals("q")) {
                return;
            }
        }

        readRandomTeamName(team);

        String teamName = "";
        while (teamName.length() > NAME_TEAM_LENGTH || teamName.isEmpty() || teamName.length() < 1) {
            System.out.print(jsonObject.get("tname") + " - (" + NAME_TEAM_LENGTH + " chars): ");
            teamName = scan.nextLine();
            if (teamName.equals("q")) {
                return;
            }
        }

        String teamDesc = "";
        while (teamDesc.length() > DC_TEAM_LENGTH || teamDesc.isEmpty() || teamDesc.length() < 1) {
            System.out.print(jsonObject.get("dteam") + " - (" + DC_TEAM_LENGTH + " chars): ");
            teamDesc = scan.nextLine();
            if (teamDesc.equals("q")) {
                return;
            }
        }

        writeRandomTeamName(team, teamName, teamDesc);
    }

    /**
     * Shows a match
     */
    public static void viewMatch() {

        String teaml_tmp;
        int teaml = 0;
        while (teaml > teams || teaml < 1) {
            System.out.print(jsonObject.get("selectlteam") + ": ");
            teaml_tmp = scan.nextLine();
            teaml = parseIntN(teaml_tmp);
            if (teaml_tmp.equals("q")) {
                return;
            }
        }

        String teamv_tmp;
        int teamv = 0;
        while (teamv > teams || teamv < 1 || teamv == teaml) {
            System.out.print(jsonObject.get("selectvteam") + ": ");
            teamv_tmp = scan.nextLine();
            teamv = parseIntN(teamv_tmp);
            if (teamv == teaml) {
                System.out.println(jsonObject.get("cantbeequals"));
            }
            if (teamv_tmp.equals("q")) {
                return;
            }
        }

        readRandom(teaml, teamv);
    }


    /**
     * Views all teams
     */
    public static void viewTeams() {

        int team = 1;
        while (team <= teams) {
            readAllRandomTeams(team);
            team++;
        }
        System.out.println();
    }

    /**
     * Shows al non empty matches
     */
    public static void viewAllMatches() {

        System.out.println();

        System.out.println(jsonObject.get("welcomeleague") + ": " + prop.getProperty("sponsor"));

        int teaml = 1;
        while (teaml <= teams) {
            int teamv = 1;
            while (teamv <= teams) {
                if (teaml != teamv) {
                    readAllRandom(teaml, teamv);
                }
                teamv++;
            }
            teaml++;
        }
        System.out.println();

    }

    /**
     * Is numeric?
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Int parser
     *
     * @param str
     * @return
     */
    public static int parseIntN(String str) {
        if (isNumeric(str)) {
            return Integer.parseInt(str);
        } else {
            return -1;
        }
    }

    /**
     * Short parser
     *
     * @param str
     * @return
     */
    public static short parseShortN(String str) {
        if (isNumeric(str)) {
            return Short.parseShort(str);
        } else {
            return -1;
        }
    }

    /**
     * Date parser checker
     *
     * @param str
     * @return
     */
    public static boolean parseDate(String str) {
        try {
            parseYYMMDD(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Loads JSON language
     */
    static void loadJson() {
        try (FileReader cLang = new FileReader(DEFAULT_DIR + SP + current + D_FILE_EXT_LANG)) {

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(cLang);
            jsonObject = (JSONObject) obj;

        } catch (IOException io) {
            io.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    /**
     * Loads JSON teams
     */
    static void loadJsonTeam() {

        String teaml_tmp;
        int teaml = 0;
        while (teaml > teams || teaml < 1) {
            System.out.print(jsonObject.get("selectteam") + ": ");
            teaml_tmp = scan.nextLine();
            teaml = parseIntN(teaml_tmp);
            if (teaml_tmp.equals("q")) {
                return;
            }
        }

        File teamFiles = new File(DEFAULT_DIR_TEAM + SP + "t" + teaml);
        File[] rutaDir = teamFiles.listFiles();

        String fileName = D_FILE_EXT_LANG;

        ArrayList<File> player = new ArrayList<>(Arrays.asList(rutaDir));

        HashSet<String> pExist = new HashSet<>();

        for (File file : player) {
            if (file.isFile() && file.getName().contains(fileName)) {
                pExist.add(file.getName());
            }
        }

        if (pExist.size() == 0) {
            System.out.println(jsonObject.get("noplayers"));
        } else {
            System.out.println(jsonObject.get("allplayers") + ":");
            for (String c : pExist) {

                try (FileReader cp = new FileReader(DEFAULT_DIR_TEAM + SP + "t" + teaml + SP + c)) {

                    JSONParser parser = new JSONParser();

                    Object obj = parser.parse(cp);

                    JSONObject jsonT;
                    jsonT = (JSONObject) obj;

                    System.out.println(jsonT.get("name") + " " + jsonT.get("surname"));

                } catch (IOException io) {
                    io.printStackTrace();
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        loadCfg();
        menu();
        String menuItem;
        do {
            menuItem = scan.nextLine();
            switch (menuItem) {
                case "1":
                    changeLang();
                    showMenu();
                    break;
                case "2":
                    if (fileExist()) {
                        qMatch();
                    } else {
                        newRandomFile();
                    }
                    showMenu();
                    break;
                case "3":
                    if (fileExist()) {
                        viewMatch();
                    }
                    showMenu();
                    break;
                case "4":
                    if (fileExist()) {
                        viewAllMatches();
                    }
                    showMenu();
                    break;
                case "5":
                    if (fileExist()) {
                        allMatchesByDate();
                    }
                    showMenu();
                    break;
                case "6":
                    if (fileExist()) {
                        allMatchesByTotalPoints();
                    }
                    showMenu();
                    break;
                case "7":
                    if (fileExist()) {
                        addTeam();
                    }
                    showMenu();
                    break;
                case "8":
                    if (fileExist()) {
                        viewTeams();
                    }
                    showMenu();
                    break;
                case "9":
                    if (fileExist()) {
                        allMatchesByTotalPointsOne();
                    }
                    showMenu();
                    break;
                case "r":
                    if (fileExist()) {
                        resetLeague();
                    }
                    showMenu();
                    break;
                case "s":
                    loadJsonTeam();
                    showMenu();
                    break;
                case "m":
                    menu();
                    break;
                case "q":
                    showBye();
                    break;
            }
        } while (!menuItem.equals("q"));
    }
}

