using System;
using System.Collections.Generic;

namespace BookingPlaceAPI.Models
{
    public class OrganizationBookingDetail
    {
        public TimeSpan StartTime { get; set; }
        public TimeSpan EndTime { get; set; }
        public int SummaryNumberOfPlace { get; set; }
        public List<OrganizationBookingInfoByDay> BookingPlaceInfo { get; set; }

        public List<OrganizationBookingInfoByDay> GetPosssibleBookingPlaces(TimeSpan from)
        {
            var result = new List<OrganizationBookingInfoByDay>();

            if (BookingPlaceInfo == null || BookingPlaceInfo.Count == 0)
                return result;

            var to = from + new TimeSpan(2, 0, 0);
            for(int index = 0; index < BookingPlaceInfo.Count; index++)
            {
                var info = BookingPlaceInfo[index];
                if (from >= info.From)
                    result.Add(info);

                if (to <= info.To)
                    return result;
            }
            return result;
        }
    }
}
