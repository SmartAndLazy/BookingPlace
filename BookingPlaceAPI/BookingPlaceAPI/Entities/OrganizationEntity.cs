using System;

namespace BookingPlaceAPI.Entities
{
    public class OrganizationEntity
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string RuName { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public string Address { get; set; }
        public double? Rating { get; set; }
        public int WorkingStartHour { get; set; }
        public int WorkingStartMinute { get; set; }
        public int WorkingEndHour { get; set; }
        public int WorkingEndMinute { get; set; }
        public string Description { get; set; }
        public string Phone { get; set; }
        public int? CountOfAvailablePlacement { get; set; }
        public string Categories { get; set; }
        public string WorkingTimeStr { get; set; }
        public string Url { get; set; }

        public TimeSpan GetWorkingStart() => new TimeSpan(WorkingStartHour, WorkingStartMinute, 0);
        public TimeSpan GetWorkingEnd() => new TimeSpan(WorkingEndHour, WorkingEndMinute, 0);
    }
}
