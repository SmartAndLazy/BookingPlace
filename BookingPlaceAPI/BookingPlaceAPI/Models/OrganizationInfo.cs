using System.Collections.Generic;

namespace BookingPlaceAPI.Models
{
    public class OrganizationInfo
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string RuName { get; set; }
        public string Address { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public double Rating { get; set; }
        public WorkingTimes WorkingTimes { get; set; }
        public string Description { get; set; }
        public string Phone { get; set; }
        public int CountOfAvailablePlacement { get; set; }
        public int CountOfFreePlacement { get; set; }
        public string Categories { get; set; }
        public string WorkingTimeStr { get; set; }
        public string Url { get; set; }

        public IEnumerable<OrganizationSocialNetwork> SocialNeгtworkAccounts { get; set; }
        public IEnumerable<UserRating> UserRatings { get; set; }
    }
}
