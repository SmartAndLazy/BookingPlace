using System;

namespace BookingPlaceAPI.Entities
{
    public class OrganizationBookingPlaceEntity
    {
        public int Id { get; set; }
        public int OrganizationId { get; set; }
        public DateTime Date { get; set; }
        public string Phone { get; set; }
        public string Name { get; set; }
        public string Surname { get; set; }
        public int BookingStartHour { get; set; }
        public int BookingStartMinute { get; set; }
        public int NumberOfTable { get; set; }

        public TimeSpan GetBookingStart() => new TimeSpan(BookingStartHour, BookingStartMinute, 0);
        public TimeSpan GetBookingEnd() => GetBookingStart() + new TimeSpan(2, 0, 0);
    }
}
