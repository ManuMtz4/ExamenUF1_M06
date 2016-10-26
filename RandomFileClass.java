package rfile;

import java.io.*;

import static rfile.TeamRandomFile.*;


/**
 * RandomFileClass Examen Corregido v1.1
 * <p>
 * Copyright 2016 Manuel Martínez <ManuMtz@icloud.com> / <ManuMtz@hotmail.co.uk>
 * <p>
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */

public class RandomFileClass {

    static File fileLeague = new File("league.bin");
    static final int EACH_MATCH = 528; // max team name
    static int teams = 10;

    static long leagueSize = teams * teams * EACH_MATCH;

    static long eachLocal = leagueSize / teams;

    /**
     * File league exist?
     *
     * @return
     */
    static boolean fileExist() {
        return fileLeague.exists();
    }

    /**
     * Resets the league
     */
    static void resetLeague() {
        fileLeague.delete();
        newRandomFile();
    }

    /**
     * Creates a league
     */
    static void newRandomFile() {

        File t1 = new File(DEFAULT_DIR_TEAM + SP + "t1");
        if (!t1.exists()) {
            t1.mkdirs();
        }

        File t2 = new File(DEFAULT_DIR_TEAM + SP + "t2");
        if (!t2.exists()) {
            t2.mkdirs();
        }

        File t3 = new File(DEFAULT_DIR_TEAM + SP + "t3");
        if (!t3.exists()) {
            t3.mkdirs();
        }

        File t4 = new File(DEFAULT_DIR_TEAM + SP + "t4");
        if (!t4.exists()) {
            t4.mkdirs();
        }

        File t5 = new File(DEFAULT_DIR_TEAM + SP + "t5");
        if (!t5.exists()) {
            t5.mkdirs();
        }

        File t6 = new File(DEFAULT_DIR_TEAM + SP + "t6");
        if (!t6.exists()) {
            t6.mkdirs();
        }

        File t7 = new File(DEFAULT_DIR_TEAM + SP + "t7");
        if (!t7.exists()) {
            t7.mkdirs();
        }

        File t8 = new File(DEFAULT_DIR_TEAM + SP + "t8");
        if (!t8.exists()) {
            t8.mkdirs();
        }

        File t9 = new File(DEFAULT_DIR_TEAM + SP + "t9");
        if (!t9.exists()) {
            t9.mkdirs();
        }

        File t10 = new File(DEFAULT_DIR_TEAM + SP + "t10");
        if (!t10.exists()) {
            t10.mkdirs();
        }

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "rw");
             OutputStream output = new FileOutputStream(defaultFilecfg)) {

            raf.seek(0);
            raf.setLength(leagueSize);

            String sponsorName = "";

            while (sponsorName.length() > 30 || sponsorName.isEmpty()) {
                System.out.print(jsonObject.get("sponsor") + " - (30 chars): ");
                sponsorName = scan.nextLine();
                if (sponsorName.equals("q")) {
                    return;
                }
            }

            prop.setProperty("sponsor", sponsorName);
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Writes a match
     *
     * @param teaml
     * @param teamv
     * @param pteam1
     * @param pteam2
     * @param rName
     * @param date
     */
    static void writeRandom(int teaml, int teamv, short pteam1, short pteam2, String rName, String date) {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "rw")) {

            long pos = ((teaml - 1) * eachLocal + teamv * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            raf.writeShort(pteam1); // 2
            raf.writeShort(pteam2); // 2
            raf.writeUTF(rName); // 4x30
            raf.writeUTF(date); // 6 - only numbers

            // 130 bytes total

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Writes a Team
     *
     * @param team
     * @param name
     * @param desc
     */
    static void writeRandomTeamName(int team, String name, String desc) {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "rw")) {

            long pos = ((team - 1) * eachLocal + team * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            raf.writeUTF(name); // 4x12
            raf.writeUTF(desc); // 1*120 - only numbers

            // 132 bytes total

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Reads a match
     *
     * @param teaml
     * @param teamv
     */
    static void readRandom(int teaml, int teamv) {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "r")) {

            long pos = ((teaml - 1) * eachLocal + teamv * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            short p1 = raf.readShort();
            short p2 = raf.readShort();
            String r = raf.readUTF();
            String d = raf.readUTF();

            System.out.println();
            if (p1 >= 0 && p2 >= 0 && !d.isEmpty() && !r.isEmpty()) {
                System.out.println(teaml + " " + jsonObject.get("pteam") + ": " + p1);
                System.out.println(teamv + " " + jsonObject.get("pteam") + ": " + p2);
                System.out.println(jsonObject.get("rname") + ": " + r);
                System.out.println(jsonObject.get("date") + ": " + d);
            } else {
                System.out.println(jsonObject.get("noplayed"));
            }
            System.out.println();

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    static void readRandomTeamName(int team) {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "r")) {

            long pos = ((team - 1) * eachLocal + team * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            String r = raf.readUTF();
            String d = raf.readUTF();

            if (!d.isEmpty() && !r.isEmpty()) {
                System.out.print(jsonObject.get("ctname") + ": " + r + " | ");
                System.out.println(jsonObject.get("cdteam") + ": " + d);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Reads all matches
     *
     * @param teaml
     * @param teamv
     */
    static void readAllRandom(int teaml, int teamv) {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "r")) {

            long pos = ((teaml - 1) * eachLocal + teamv * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            short p1 = raf.readShort();
            short p2 = raf.readShort();
            String r = raf.readUTF();
            String d = raf.readUTF();

            if (p1 >= 0 && p2 >= 0 && !d.isEmpty() && !r.isEmpty()) {
                System.out.print("[");
                System.out.print(teaml + " " + jsonObject.get("pteam") + ": " + p1 + " | ");
                System.out.print(teamv + " " + jsonObject.get("pteam") + ": " + p2 + " | ");
                System.out.print(jsonObject.get("rname") + ": " + r + " | ");
                System.out.print(jsonObject.get("date") + ": " + d);
                System.out.println("]");
            }

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Readas all teams
     *
     * @param team
     */
    static void readAllRandomTeams(int team) {

        try (RandomAccessFile raf = new RandomAccessFile(fileLeague, "r")) {

            long pos = ((team - 1) * eachLocal + team * EACH_MATCH) - EACH_MATCH;

            raf.seek(pos);

            String r = raf.readUTF();
            String d = raf.readUTF();

            if (!d.isEmpty() && !r.isEmpty()) {
                System.out.print("[");
                System.out.print(jsonObject.get("tname") + ": " + r + " | ");
                System.out.print(jsonObject.get("dteam") + ": " + d);
                System.out.println("]");
            }

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}