package com.tsfn.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;


public class FileInfo {
    @SerializedName("name")
    private String name;

    @SerializedName("path")
    private String path;

    @SerializedName("sha")
    private String sha;

    @SerializedName("size")
    private int size;

    @SerializedName("url")
    private String url;

    @SerializedName("html_url")
    private String htmlUrl;

    @SerializedName("git_url")
    private String gitUrl;

    @SerializedName("download_url")
    private String downloadUrl;

    @SerializedName("type")
    private String type;

    @SerializedName("_links")
    private Links links;

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getSha() {
        return sha;
    }

    public int getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getType() {
        return type;
    }

    public Links getLinks() {
        return links;
    }

    public static FileInfo[] fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, FileInfo[].class);
    }

    public static class Links {
        @SerializedName("self")
        private String self;

        @SerializedName("git")
        private String git;

        @SerializedName("html")
        private String html;

        public String getSelf() {
            return self;
        }

        public String getGit() {
            return git;
        }

        public String getHtml() {
            return html;
        }
    }
}
