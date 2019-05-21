using BookingPlaceAPI.Enums;

namespace BookingPlaceAPI.Models
{
    public class OrganizationSocialNetworkUrlShortModel
    {
        public string Name { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public string Url { get; set; }
        public SocialNetworkType Type { get; set; }
    }
}
