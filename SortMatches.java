package rfile;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import static rfile.JodaDT.parseYYMMDD;
import static rfile.RandomFileClass.*;
import static rfile.TeamRandomFile.*;

/**
 * SortMatches Examen Corregido v1.1
 * <p>
 * Copyright 2016 Manuel Martínez <ManuMtz@icloud.com> / <ManuMtz@hotmail.co.uk>
 * <p>
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */

public class SortMatches {

    static List<Object[]> sortedMatches = new ArrayList<>();
    static List<Object[]> sortedMatchesOneTeam = new ArrayList<>();

    /**
     * Adds matches to ArrayList  Object[]
     *
     * @param teaml
     * @param teamv
     */
    static void readAddArray(int teaml, int teamv) {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "r")) {

            long eachLocal = leagueSize / teams; // each local space

            long pos = ((teaml - 1) * eachLocal + teamv * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            short p1 = raf.readShort();
            short p2 = raf.readShort();
            String r = raf.readUTF();
            String d = raf.readUTF();

            Object[] array = new Object[6];

            if (p1 >= 0 && p2 >= 0 && !d.isEmpty() && !r.isEmpty()) {
                array[0] = p1;
                array[1] = p2;
                array[2] = r;
                array[3] = d;
                array[4] = teaml;
                array[5] = teamv;
                sortedMatches.add(array);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Local team
     *
     * @param teaml
     * @param teamv
     */
    static void readAddArrayOneTeam1(int teaml, int teamv) {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "r")) {

            long eachLocal = leagueSize / teams; // each local space

            long pos = ((teaml - 1) * eachLocal + teamv * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            short p1 = raf.readShort();
            short p2 = raf.readShort();
            String r = raf.readUTF();
            String d = raf.readUTF();

            Object[] array = new Object[7];

            if (p1 >= 0 && p2 >= 0 && !d.isEmpty() && !r.isEmpty()) {
                array[0] = p1;
                array[1] = p2;
                array[2] = r;
                array[3] = d;
                array[4] = teaml;
                array[5] = teamv;
                array[6] = "l";
                sortedMatchesOneTeam.add(array);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Visitant team
     *
     * @param teaml
     * @param teamv
     */
    static void readAddArrayOneTeam2(int teaml, int teamv) {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "r")) {

            long eachLocal = leagueSize / teams; // each local space

            long pos = ((teaml - 1) * eachLocal + teamv * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            short p1 = raf.readShort();
            short p2 = raf.readShort();
            String r = raf.readUTF();
            String d = raf.readUTF();

            Object[] array = new Object[7];

            if (p1 >= 0 && p2 >= 0 && !d.isEmpty() && !r.isEmpty()) {
                array[0] = p1;
                array[1] = p2;
                array[2] = r;
                array[3] = d;
                array[4] = teaml;
                array[5] = teamv;
                array[6] = "v";
                sortedMatchesOneTeam.add(array);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Builds all matches
     */
    public static void allMatches() {
        if (!sortedMatches.isEmpty()) {
            sortedMatches.clear();
        }
        int teaml = 1;
        while (teaml <= teams) {
            int teamv = 1;
            while (teamv <= teams) {
                if (teaml != teamv) {
                    readAddArray(teaml, teamv);
                }
                teamv++;
            }
            teaml++;
        }
    }

    /**
     * Builds all matches for one team
     */
    public static void allMatchesOne() {
        if (!sortedMatchesOneTeam.isEmpty()) {
            sortedMatchesOneTeam.clear();
        }

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

        int teamv = 1;
        while (teamv <= teams) {
            if (team != teamv) {
                readAddArrayOneTeam1(team, teamv);
            }
            teamv++;
        }

        int teaml = 1;
        while (teaml <= teams) {
            if (team != teaml) {
                readAddArrayOneTeam2(teaml, team);
            }
            teaml++;
        }
    }

    /**
     * Gets all matches sorted by date
     */
    public static void allMatchesByDate() {
        allMatches();

        if (sortedMatches.size() > 1) {
            Collections.sort(sortedMatches, new DateComparator());
        }

        System.out.println();

        System.out.println(jsonObject.get("welcomeleague") + ": " + prop.getProperty("sponsor"));

        if (sortedMatches.size() > 0) {
            for (Object[] sortedMatch : sortedMatches) {

                for (int i = 0; i < 1; i++) {
                    System.out.print("[");
                    System.out.print(sortedMatch[4] + " " + jsonObject.get("pteam") + ": " + sortedMatch[0] + " | ");
                    System.out.print(sortedMatch[5] + " " + jsonObject.get("pteam") + ": " + sortedMatch[1] + " | ");
                    System.out.print(jsonObject.get("rname") + ": " + sortedMatch[2] + " | ");
                    System.out.print(jsonObject.get("date") + ": " + sortedMatch[3]);
                    System.out.println("]");
                }
            }
        }
        System.out.println();
    }

    /**
     * Gets all matches sorted by total points
     */
    public static void allMatchesByTotalPoints() {
        allMatches();

        if (sortedMatches.size() > 1) {
            Collections.sort(sortedMatches, new PointsComparator());
        }

        System.out.println();

        System.out.println(jsonObject.get("welcomeleague") + ": " + prop.getProperty("sponsor"));

        if (sortedMatches.size() > 0) {
            for (Object[] sortedMatch : sortedMatches) {

                for (int i = 0; i < 1; i++) {
                    System.out.print("[");
                    System.out.print(sortedMatch[4] + " " + jsonObject.get("pteam") + ": " + sortedMatch[0] + " | ");
                    System.out.print(sortedMatch[5] + " " + jsonObject.get("pteam") + ": " + sortedMatch[1] + " | ");
                    System.out.print(jsonObject.get("rname") + ": " + sortedMatch[2] + " | ");
                    System.out.print(jsonObject.get("date") + ": " + sortedMatch[3]);
                    System.out.println("]");
                }
            }
        }
        System.out.println();
    }

    /**
     * Gets all matches sorted by favour points
     */
    public static void allMatchesByTotalPointsOne() {
        allMatchesOne();
        if (sortedMatchesOneTeam.size() > 1) {
            Collections.sort(sortedMatchesOneTeam, new PointsFavourComparator());
        }

        System.out.println();
        System.out.println(jsonObject.get("welcomeleague") + ": " + prop.getProperty("sponsor"));

        if (sortedMatchesOneTeam.size() > 0) {
            for (Object[] sortedMatch : sortedMatchesOneTeam) {

                for (int i = 0; i < 1; i++) {
                    System.out.print("[");
                    System.out.print(sortedMatch[4] + " " + jsonObject.get("pteam") + ": " + sortedMatch[0] + " | ");
                    System.out.print(sortedMatch[5] + " " + jsonObject.get("pteam") + ": " + sortedMatch[1] + " | ");
                    System.out.print(jsonObject.get("rname") + ": " + sortedMatch[2] + " | ");
                    System.out.print(jsonObject.get("date") + ": " + sortedMatch[3]);
                    System.out.println("]");
                }
            }
        }
        System.out.println();
    }
}

/**
 * Date Comparator Class
 */
class DateComparator implements Comparator<Object[]> {

    @Override
    public int compare(Object[] x, Object[] y) {
        String ax = String.valueOf(x[3]);
        String bx = String.valueOf(y[3]);
        DateTime a = parseYYMMDD(ax);
        DateTime b = parseYYMMDD(bx);
        return a.toLocalDate().compareTo(b.toLocalDate());
    }
}

/**
 * Points Comparator Class
 */
class PointsComparator implements Comparator<Object[]> {

    @Override
    public int compare(Object[] x, Object[] y) {
        int ax = Integer.parseInt(String.valueOf(x[0])) + Integer.parseInt(String.valueOf(x[1]));
        int bx = Integer.parseInt(String.valueOf(y[0])) + Integer.parseInt(String.valueOf(y[1]));
        return bx - ax;
    }
}

/**
 * PointsFavour Comparator Class
 */
class PointsFavourComparator implements Comparator<Object[]> {

    @Override
    public int compare(Object[] x, Object[] y) {

        int a1 = Integer.parseInt(String.valueOf(x[0]));
        int a2 = Integer.parseInt(String.valueOf(x[1]));

        int b1 = Integer.parseInt(String.valueOf(y[0]));
        int b2 = Integer.parseInt(String.valueOf(y[1]));

        int c1 = 0;
        if (x[6].equals("l")) {
            c1 = a1 - a2;
        } else if (x[6].equals("v")) {
            c1 = a2 - a1;
        }

        int d1 = 0;
        if (y[6].equals("l")) {
            d1 = b1 - b2;
        } else if (y[6].equals("v")) {
            d1 = b2 - b1;
        }

        return d1 - c1;
    }
}