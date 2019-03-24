package com.example.mymoviecatalogue;

import android.os.Parcel;
import android.os.Parcelable;

class MainModel {

    static class Movie implements Parcelable {

        private int photo;
        private String title;
        private String description;
        private String release;
        private String directors;

        String getRelease() {
            return release;
        }

        void setRelease(String release) {
            this.release = release;
        }

        String getDirectors() {
            return directors;
        }

        void setDirectors(String directors) {
            this.directors = directors;
        }

        int getPhoto() {
            return photo;
        }

        void setPhoto(int photo) {
            this.photo = photo;
        }

        String getTitle() {
            return title;
        }

        void setTitle(String title) {
            this.title = title;
        }

        String getDescription() {
            return description;
        }

        void setDescription(String description) {
            this.description = description;
        }

        Movie() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.photo);
            dest.writeString(this.title);
            dest.writeString(this.description);
            dest.writeString(this.release);
            dest.writeString(this.directors);
        }

        Movie(Parcel in) {
            this.photo = in.readInt();
            this.title = in.readString();
            this.description = in.readString();
            this.release = in.readString();
            this.directors = in.readString();
        }

        public static final Creator<Movie> CREATOR = new Creator<Movie>() {
            @Override
            public Movie createFromParcel(Parcel source) {
                return new Movie(source);
            }

            @Override
            public Movie[] newArray(int size) {
                return new Movie[size];
            }
        };
    }

}
