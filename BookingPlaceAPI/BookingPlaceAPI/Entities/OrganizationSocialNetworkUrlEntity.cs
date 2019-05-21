using BookingPlaceAPI.Enums;

namespace BookingPlaceAPI.Entities
{
    public class OrganizationSocialNetworkUrlEntity
    {
        public int Id { get; set; }
        public int OrganizationId { get; set; }
        public string Url { get; set; }
        public SocialNetworkType Type { get; set; }
    }
}
