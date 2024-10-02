package net.lenhat.musicapplication.data.model.artist;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ArtistList {
    @SerializedName("artists")
    public List<Artist> artists = new ArrayList<>();
}
