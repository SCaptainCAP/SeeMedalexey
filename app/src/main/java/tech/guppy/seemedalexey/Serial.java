package tech.guppy.seemedalexey;

/**
 * Created by serych on 19.10.2017.
 */

public class Serial {

    private String name, prev, videolink, episode, lang, favorite, season;

    public Serial(String name, String prev, String videolink, String episode, String lang, String favorite, String season) {
        this.name = name;
        this.prev = prev;
        this.videolink = videolink;
        this.episode = episode;
        this.lang = lang;
        this.favorite = favorite;
        this.season = season;
    }

    public String getName() {
        return name;
    }

    public String getPrev() {
        return prev;
    }

    public String getVideolink() {
        return videolink;
    }

    public String getEpisode() {
        return episode;
    }

    public String getLang() {
        return lang;
    }

    public String getFavorite() {
        return favorite;
    }

    public String getSeason() {
        return season;
    }
}
