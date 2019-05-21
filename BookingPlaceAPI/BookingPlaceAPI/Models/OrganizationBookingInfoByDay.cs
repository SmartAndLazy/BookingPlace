using System;

namespace BookingPlaceAPI.Models
{
    public class OrganizationBookingInfoByDay
    {
        public TimeSpan From { get; set; }
        public TimeSpan To { get; set; }
        public int NumberOfBuzyPlacement { get; set; }
        public int NumberOfFreePlacement { get; set; }
    }
}
