package dev.antimoxs.hypixelapi.objects.guild;

import dev.antimoxs.hypixelapi.config;
import dev.antimoxs.hypixelapi.exceptions.UnknownValueException;
import dev.antimoxs.hypixelapi.requests.MojangRequest;
import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;
import io.quickchart.QuickChart;

import java.util.*;
import java.util.stream.Collectors;

public class Guild {

    // Values are parsed via gson

    public String _id = "No data for 'guildID'";
    public String name = "No data for 'guildName'";
    public String name_lower = "No data for 'guildNameLower";
    public String tag = "No data for 'guildTag'";
    public String tagColor = "No data for 'guildTagColor'";
    public String description = "No data for 'guildDescription'";

    public Integer coins = -1;
    public Integer coinsEver = -1;
    public Integer exp = -1;
    public Integer legacyRanking = -1;

    public Long chatMute = 0L;
    public Long created = 0L;

    public Boolean joinable = false;
    public Boolean publiclyListed = false;

    public HashMap<String, Integer> guildExpByGameType = new HashMap<String, Integer>();
    public String[] preferredGames;
    public HashMap<String, Integer> achievements = new HashMap<String, Integer>();
    public GuildMember[] members;
    public GuildRank[] ranks;


    public int a0 = 0;
    public int a1 = 0;
    public int a2 = 0;
    public int a3 = 0;
    public int a4 = 0;
    public int a5 = 0;
    public int a6 = 0;
    public int aT = 0;
    public int aA = 0;

    public double[] scaled = new double[7];
    public double sT = 0;
    public double sA = 0;


    public Guild() {}


    public String getID()               { return this._id; }
    public String getName()             { return this.name; }
    public String getTag()              { return this.tag; }
    public String getTagColor()         { return this.tagColor; }
    public String getDescription()      { return this.description; }

    public Integer getCoins()           { return this.coins; }
    public Integer getCoinsEver()       { return this.coinsEver; }
    public Integer getExp()             { return this.exp; }
    public Integer getLegacyRank()      { return this.legacyRanking; }

    public Long getChatMute()           { return this.chatMute; }
    public Long getCreated()            { return this.created; }

    public Boolean getJoinable()        { return this.joinable; }
    public Boolean getPublicListed()    { return this.publiclyListed; }

    public HashMap<String, Integer> getGameXP()         { return this.guildExpByGameType; }
    public HashMap<String, Integer> getAchievements()   { return this.achievements; }

    public String[] getPreferredGames() { return this.preferredGames; }
    public GuildMember[] getMembers()   { return this.members; }
    public GuildRank[] getRanks()       { return this.ranks; }

    // get functions:

    public void setXPvals(){

        try {

            if (members.length == 0) {

                throw new UnknownValueException("Memberlist is empty.");

            }

            for (GuildMember m : this.members) {

                m.setXPvals();
                a0 = a0 + m.xp0;
                a1 = a1 + m.xp1;
                a2 = a2 + m.xp2;
                a3 = a3 + m.xp3;
                a4 = a4 + m.xp4;
                a5 = a5 + m.xp5;
                a6 = a6 + m.xp6;
                aT = aT + m.xpW;

            }

            double[] raw = new double[7];
            raw[0] = a0;
            raw[1] = a1;
            raw[2] = a2;
            raw[3] = a3;
            raw[4] = a4;
            raw[5] = a5;
            raw[6] = a6;

        /*for (int i = 0; i < raw.length; i++) {

            System.out.println("day " + i);

            if (raw[i] > 200000) {

                scaled[i] = scaled[i] + 200000;
                System.out.println("200000 * 100%");
                raw[i] = raw[i] - 200000;

                if (raw[i] > 50000) {

                    scaled[i] = scaled[i] + 5000;
                    System.out.println("50000 * 10%");
                    raw[i] = raw[i] - 5000;


                    scaled[i] = scaled[i] + (raw[i]*0.03);
                    System.out.println(raw[i] + " * 3%");

                } else {

                    scaled[i] = scaled[i] + (raw[i]*0.1);
                    System.out.println(raw[i] + " * 10%");

                }

            }
            else {

                scaled[i] = raw[i];
                System.out.println(raw[i] + " * 100%");

            }

        }*/

            for (int i = 0; i < raw.length; i++) {

                //System.out.println("day " + i);

                if (raw[i] > 200000) {

                    scaled[i] = scaled[i] + 200000;
                    //System.out.println("200000");
                    raw[i] = Math.ceil((raw[i] - 200000) * 0.1); // downscale to 10%

                    if (raw[i] > 50000) {

                        scaled[i] = scaled[i] + 50000;
                        //System.out.println("50000");
                        //System.out.println(raw[i] - 49999);
                        raw[i] = Math.round((raw[i] - 50000) * 0.30303); // downscale to 3%
                        //System.out.println(raw[i]);

                    }
                    scaled[i] = scaled[i] + (raw[i]);
                    //System.out.println(raw[i]);

                } else {

                    scaled[i] = raw[i];
                    //System.out.println(raw[i] + " * 100%");

                }

            }

            sT = scaled[0] + scaled[1] + scaled[2] + scaled[3] + scaled[4] + scaled[5] + scaled[6];
            sA = sT / 7;

            aA = (a0 + a1 + a2 + a3 + a4 + a5 + a6) / 7;

        }
        catch (UnknownValueException e) {

            AtmxLogger.log(AtmxLogType.WARNING, config.AppName, "Continuing without set values.");
            return;

        }

    }

    public String getXPGraphURLWeekly(boolean setVals) {

        if (setVals) {

            setXPvals();

        }

        QuickChart chart = new QuickChart();
        chart.setBackgroundColor("transparent");
        chart.setWidth(1000);
        chart.setHeight(500);
        chart.setConfig("{type:'line',data:{labels:['6ago','5ago','4ago','3ago','2ago','1ago','today'],datasets:[{type:'line',label:'Experience',data:["+a6+","+a5+","+a4+","+a3+","+a2+","+a1+","+a0+"],lineTension: '0.3',backgroundColor:getGradientFillHelper('vertical',['rgba(235,54,57,0.15)','rgba(163,54,235,0.15)','rgba(54,162,235,0.15)']),borderColor:getGradientFillHelper('vertical',['#eb3639','#a336eb','#36a2eb']),yAxisID:'y',fill:'start'},{type:'line',label:'Average',data:["+aA+","+aA+","+aA+","+aA+","+aA+","+aA+","+aA+",],steppedLine:'true',pointRadius:'0',backgroundColor:'rgba(182,0,194,0.5)',borderColor:'rgba(182,0,194,0.4)',yAxisID:'y',fill:'false'}]},options:{title:{display:'true',text:'GuildXP'},scales:{yAxes:[{id:'y',type:'linear',display:'true',ticks:{beginAtZero:'true',suggestedMin:'0'},position:'left'}]}}}");
        /*chart.setConfig("{"
                + "    type: 'line',"
                + "    data: {"
                + "        labels: ['7ago', '6ago', '5ago', '4ago', '3ago', '2ago', 'today'],"
                + "        datasets: ["
                + "        {"
                + "            type: 'line',"
                + "            label: 'Experience',"
                + "            data: ["+a6+","+a5+","+a4+","+a3+","+a2+","+a1+","+a0+"],"
                + "            lineTension: '0.3',"
                + "            backgroundColor: getGradientFillHelper('vertical', ['rgba(235, 54, 57, 0.15)', 'rgba(163, 54, 235, 0.15)', 'rgba(54, 162, 235, 0.15)']),"
                + "            borderColor: getGradientFillHelper('vertical', ['#eb3639', '#a336eb', '#36a2eb']),"
                + "            yAxisID: 'y',"
                + "            fill: 'start'"
                + "        },"
                + "        {"
                + "            type: 'line',"
                + "            label: 'Average',"
                + "            data: ["+aA+","+aA+","+aA+","+aA+","+aA+","+aA+","+aA+",],"
                + "            steppedLine: 'true',"
                + "            pointRadius: '0',"
                + "            backgroundColor: 'rgba(182, 0, 194, 0.5)',"
                + "            borderColor: 'rgba(182, 0, 194, 0.4)',"
                + "            yAxisID: 'y',"
                + "            fill: 'false'"
                + "        }"
                + "     ]"
                + "    },"
                + "    options: {"
                + "            title: {"
                + "                display: 'true',"
                + "                text: 'GuildXP'"
                + "            },"
                + "            scales: {"
                + "                yAxes: ["
                + "                    {"
                + "                        id: 'y',"
                + "                        type: 'linear',"
                + "                        display: 'true',"
                + "                        ticks: {"
                + "                                beginAtZero: 'true',"
                + "                                suggestedMin: '0'"
                + "                        },"
                + "                        position: 'left'"
                + "                    } "
                + "                ]"
                + "            }"
                + "    }"
                + "}"
        );*/

        return chart.getUrl().replaceAll("\\+","");

    }

    public String getXPGraphDataset(boolean setVals, String color, String label) {

        if (setVals) this.setXPvals();

        return "{type:'line',label:'" + label + "',data:["+a6+","+a5+","+a4+","+a3+","+a2+","+a1+","+a0+"],lineTension: '0.3'," +
                "backgroundColor:getGradientFillHelper('vertical',['" + color + "','" + color + "','" + color + "'])," +
                "backgroundColor:getGradientFillHelper('vertical',['" + color + "','" + color + "','" + color + "'])," +
                "yAxisID:'y',fill:'start'}";

    }

    // Get GuildMember
    private GuildMember getMember           (String uuid) throws UnknownValueException {

        if (members == null) {

            throw new UnknownValueException("MemberList is empty.");

        }


        for (GuildMember member : members) {

            if (member.getUUID().equals(uuid)) {

                return member;

            }

        }

        throw new UnknownValueException("Can't find Member.");

    }
    public  GuildMember getMemberByName     (String name) {

        String uuid = MojangRequest.getUUID(name);

        try {
            return getMember(uuid);
        } catch (UnknownValueException e) {
            return new GuildMember();
        }

    }
    public  GuildMember getMemberByUUID     (String uuid) {

        try {
            return getMember(uuid);
        } catch (UnknownValueException e) {
            return new GuildMember();
        }

    }

    // Get GuildRank
    private GuildRank   getGuildRank        (String name) throws UnknownValueException {

        if (ranks == null || ranks.length == 0) {

            throw new UnknownValueException("RankList is empty.");

        }

        for (GuildRank rank : this.ranks) {

            if (rank.getName().equals(name)) {

                return rank;

            }

        }

        throw new UnknownValueException("Can't find Rank.");

    }
    public  GuildRank   getGuildRankByName  (String name) {

        try {
            return getGuildRank(name);
        } catch (UnknownValueException e) {
            return new GuildRank();
        }

    }

    // Get GameXP
    private Integer getGameXP(String game) throws UnknownValueException {

        if (guildExpByGameType == null || guildExpByGameType.isEmpty()) {

            throw new UnknownValueException("GuildGameList is empty.");

        }

        for (String s : guildExpByGameType.keySet()) {

            if (s.equals(game)) {

                return guildExpByGameType.get(s);

            }

        }

        throw new UnknownValueException("Can't find Game.");

    }
    public Integer getXPforGame(GuildXPGame game) {

        try {
            return getGameXP(game.name());
        } catch (UnknownValueException e) {
            return -1;
        }

    }

    public Integer getAchievementByName(String name) {

        for (String s : this.achievements.keySet()) {

            if (s.equals(name)) {

                return achievements.get(s);

            }

        }

        return -1;

    }


    public ArrayList<GuildMember> getMembersSorted(GuildMemberXPDate date) {

        try {

            if (members.length == 0) {

                throw new UnknownValueException("Memberlist is empty.");

            }

            switch (date) {

                case TODAY:
                    return Arrays.stream(members).sorted(Comparator.comparing(GuildMember::getXP0).reversed()).collect(Collectors.toCollection(ArrayList::new));
                case YESTERDAY:
                    return Arrays.stream(members).sorted(Comparator.comparing(GuildMember::getXP1).reversed()).collect(Collectors.toCollection(ArrayList::new));
                case TWO_DAYS_AGO:
                    return Arrays.stream(members).sorted(Comparator.comparing(GuildMember::getXP2).reversed()).collect(Collectors.toCollection(ArrayList::new));
                case THREE_DAYS_AGO:
                    return Arrays.stream(members).sorted(Comparator.comparing(GuildMember::getXP3).reversed()).collect(Collectors.toCollection(ArrayList::new));
                case FOUR_DAYS_AGO:
                    return Arrays.stream(members).sorted(Comparator.comparing(GuildMember::getXP4).reversed()).collect(Collectors.toCollection(ArrayList::new));
                case FIVE_DAYS_AGO:
                    return Arrays.stream(members).sorted(Comparator.comparing(GuildMember::getXP5).reversed()).collect(Collectors.toCollection(ArrayList::new));
                case SIX_DAYS_AGO:
                    return Arrays.stream(members).sorted(Comparator.comparing(GuildMember::getXP6).reversed()).collect(Collectors.toCollection(ArrayList::new));
                case WEEK:
                    return Arrays.stream(members).sorted(Comparator.comparing(GuildMember::getXPweekTotal).reversed()).collect(Collectors.toCollection(ArrayList::new));

                default:
                    return Arrays.stream(members).sorted(Comparator.comparing(GuildMember::getXP0).reversed()).collect(Collectors.toCollection(ArrayList::new));

            }


        }
        catch (UnknownValueException e) {

            AtmxLogger.log(AtmxLogType.WARNING, config.AppName, "Continuing with new ArrayList...");
            ArrayList<GuildMember> g = new ArrayList<GuildMember>();
            g.add(new GuildMember());
            return g;

        }


    }

    // Check functions:
    public Boolean hasMember(String name, Boolean caseSensitive) {

        for (GuildMember member : members) {

            String mn = member.getName();
            String mn2 = name;

            if (!caseSensitive) {

                mn = mn.toLowerCase();
                mn2 = mn2.toLowerCase();

            }

            if (mn.equals(mn2)) {

                return true;

            }

        }
        return false;

    }

    /*public void setKey(String key) {

        this.key = key;

        for (GuildMember member : members) {

            member.setKey(key);

        }

    }*/


    public synchronized void fetchNames() {

        ThreadGroup fetching = new ThreadGroup("namefetchers");

        new Thread(fetching, () -> {

            for (GuildMember m : Arrays.copyOfRange(members,0,members.length/2)) {

                new Thread(fetching,() -> m.memberName = MojangRequest.getName(m.uuid),m.uuid).start();

            }

        }).start();
        new Thread(fetching, () -> {

            for (GuildMember m : Arrays.copyOfRange(members,members.length/2, members.length)) {

                new Thread(fetching,() -> m.memberName = MojangRequest.getName(m.uuid),m.uuid).start();

            }

        }).start();


        while (fetching.activeCount() != 0) {}
        fetching.destroy();

    }



}
