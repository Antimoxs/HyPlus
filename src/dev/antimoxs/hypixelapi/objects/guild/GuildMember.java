package dev.antimoxs.hypixelapi.objects.guild;

import com.google.gson.annotations.SerializedName;
import dev.antimoxs.hypixelapi.exceptions.UnknownValueException;
import dev.antimoxs.hypixelapi.requests.MojangRequest;
import dev.antimoxs.hypixelapi.util.UnixConverter;
import io.quickchart.QuickChart;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;

public class GuildMember {

    private boolean request = false;

    @SerializedName("uuid")
    public String uuid = "No data for 'memberUUID'";
    @SerializedName("rank")
    public String rank = "No data for 'memberRank'";
    @SerializedName("questParticipation")
    public Integer questParticipation = -1;
    @SerializedName("joined")
    private Long joined = 0L;
    @SerializedName("expHistory")
    private HashMap<String, Integer> exp = new HashMap<String, Integer>();
    @SerializedName("mutedTill")
    private Long mutedTill = 0L;

    public String memberName = "No data for 'memberName'";


    public Integer xp0 = 0;
    public Integer xp1 = 0;
    public Integer xp2 = 0;
    public Integer xp3 = 0;
    public Integer xp4 = 0;
    public Integer xp5 = 0;
    public Integer xp6 = 0;
    public Integer xpW = 0;
    public Integer xpA = 0;



    /*public GuildMember(String name, String uuid, String rank, Integer joined, Integer mutedTill, Integer questParticipation, HashMap<String, Integer> exp) {

        this.uuid = uuid.replaceAll("\"", "").replaceAll("//", "");
        this.name = name;
        this.rank = rank;
        this.questParticipation = questParticipation;
        this.joined = joined;
        this.mutedTill = mutedTill;
        this.exp = exp;

        if (mutedTill == null) { this.mutedTill = 0; }
        request = true;

    }*/

    public GuildMember() {


    }

    public String getName() {

        return MojangRequest.getName(uuid);

    }
    public String getUUID() { return uuid; }
    public String getRank() { return rank; }
    public Long getMutedTill() { return mutedTill; }
    public Long getJoined() { return joined; }
    public String getJoinedAsDate() { return UnixConverter.toDate(joined); }
    public Integer getQuestParticipation() { return questParticipation; }


    public void setXPvals() {


        LocalDate locDate = LocalDate.now(ZoneId.of("UTC-05:00"));

        xp0 = exp.get(locDate.toString());
        xp1 = exp.get(locDate.minusDays(1L).toString());
        xp2 = exp.get(locDate.minusDays(2L).toString());
        xp3 = exp.get(locDate.minusDays(3L).toString());
        xp4 = exp.get(locDate.minusDays(4L).toString());
        xp5 = exp.get(locDate.minusDays(5L).toString());
        xp6 = exp.get(locDate.minusDays(6L).toString());
        xpW = xp0+xp1+xp2+xp3+xp4+xp5+xp6;
        xpA = xpW/7;



    }

    public String getXPGraphURL(boolean setXp, int active, int inactive) {

        if (setXp) this.setXPvals();

        QuickChart chart = new QuickChart();
        chart.setBackgroundColor("transparent");
        chart.setWidth(1000);
        chart.setHeight(500);
        chart.setConfig("{"
                + "    type: 'line',"
                + "    data: {"
                + "        labels: ['6ago', '5ago', '4ago', '3ago', '2ago', '1ago', 'today'],"
                + "        datasets: ["
                + "        {"
                + "            type: 'line',"
                + "            label: 'Experience',"
                + "            data: ["+xp6+","+xp5+","+xp4+","+xp3+","+xp2+","+xp1+","+xp0+"],"
                + "            lineTension: '0.3',"
                + "            backgroundColor: getGradientFillHelper('vertical', ['rgba(235, 54, 57, 0.15)', 'rgba(163, 54, 235, 0.15)', 'rgba(54, 162, 235, 0.15)']),"
                + "            borderColor: getGradientFillHelper('vertical', ['#eb3639', '#a336eb', '#36a2eb']),"
                + "            yAxisID: 'y',"
                + "            fill: 'start'"
                + "        },"
                + "        {"
                + "            type: 'line',"
                + "            label: 'Average',"
                + "            data: ["+xpA+","+xpA+","+xpA+","+xpA+","+xpA+","+xpA+","+xpA+",],"
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
                + "                    }"
                + "                ]"
                + "            }"
                + "    }"
                + "}"
        );

        return chart.getUrl().replaceAll("\\+","");

    }

    public Integer getXPforDate(GuildMemberXPDate date) {

        try {

            if (exp == null || exp.isEmpty()) {

                throw new UnknownValueException("EXP list empty.");

            }

            String src = "";
            LocalDate locDate;
            ZoneId z = ZoneId.of("UTC-05:00");
            locDate = LocalDate.now(z);


            switch (date) {

                case TODAY:
                    src = (locDate.toString());
                    break;
                case YESTERDAY:
                    src = (locDate.minusDays(1L).toString());
                    break;
                case TWO_DAYS_AGO:
                    src = (locDate.minusDays(2L).toString());
                    break;
                case THREE_DAYS_AGO:
                    src = (locDate.minusDays(3L).toString());
                    break;
                case FOUR_DAYS_AGO:
                    src = (locDate.minusDays(4L).toString());
                    break;
                case FIVE_DAYS_AGO:
                    src = (locDate.minusDays(5L).toString());
                    break;
                case SIX_DAYS_AGO:
                    src = (locDate.minusDays(6L).toString());
                    break;
                case WEEK:
                    Integer i = getXPweekTotal();
                    //TBCLogger.log(TBCLoggingType.INFORMATION, config.AppName, i + "");
                    return i;

            }

            if (exp.get(src) == null) {

                return 0;

            }

            return exp.get(src);

        } catch (UnknownValueException e) {

            return -1;

        }

    }
    public Integer getXP0() {

        return getXPforDate(GuildMemberXPDate.TODAY);
    }
    public Integer getXP1() {

        return getXPforDate(GuildMemberXPDate.YESTERDAY);

    }
    public Integer getXP2() {

        return getXPforDate(GuildMemberXPDate.TWO_DAYS_AGO);

    }
    public Integer getXP3() {

        return getXPforDate(GuildMemberXPDate.THREE_DAYS_AGO);

    }
    public Integer getXP4() {

        return getXPforDate(GuildMemberXPDate.FOUR_DAYS_AGO);

    }
    public Integer getXP5() {

        return getXPforDate(GuildMemberXPDate.FIVE_DAYS_AGO);

        }
    public Integer getXP6() {

        return getXPforDate(GuildMemberXPDate.SIX_DAYS_AGO);

    }
    public Integer getXPweekTotal() {

        Integer i = 0;

        for (GuildMemberXPDate date : GuildMemberXPDate.values()) {

            if (date == GuildMemberXPDate.WEEK) {

                continue;

            }

            if (getXPforDate(date) == null || getXPforDate(date) == -1) {



            } else {

                i = i + getXPforDate(date);

            }

        }

        return i;


    }



}
