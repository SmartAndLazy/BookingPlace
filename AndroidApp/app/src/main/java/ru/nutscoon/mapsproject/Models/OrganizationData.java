package ru.nutscoon.mapsproject.Models;

public class OrganizationData {

    private int id;
    private String name;
    private double rating;
    private WorkingTimes workingTimes;
    private String description;
    private String address;

    private String phone;
    private int countOfAvailablePlacement;
    private int countOfFreePlacement;
    private UserRating[] userRatings;

    public OrganizationSocialNetwork[] getOrganizationSocialNetworks() {
        return socialNeгtworkAccounts;
    }

    private OrganizationSocialNetwork[] socialNeгtworkAccounts;

    public UserRating[] getUserRatings() {
        return userRatings;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public WorkingTimes getWorkingTimes() {
        return workingTimes;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public int getCountOfAvailablePlacement() {
        return countOfAvailablePlacement;
    }

    public int getTotalSpaceCount() {
        return countOfAvailablePlacement + countOfFreePlacement;
    }

    public String getAddress() {
        return address;
    }

    public class OrganizationSocialNetwork{
        private int type;
        private String url;

        public int getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }
    }


    public class WorkingTimes{
        private String from;
        private String to;

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }
    }
}
